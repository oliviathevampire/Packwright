package net.vampirestudios.arrp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.vampirestudios.arrp.JsonSerializers;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.json.advancement.JAdvancement;
import net.vampirestudios.arrp.json.animation.JAnimation;
import net.vampirestudios.arrp.json.blockstate.JState;
import net.vampirestudios.arrp.json.entityVariants.*;
import net.vampirestudios.arrp.json.equipmentinfo.JEquipmentModel;
import net.vampirestudios.arrp.json.iteminfo.JItemInfo;
import net.vampirestudios.arrp.json.lang.JLang;
import net.vampirestudios.arrp.json.loot.JLootTable;
import net.vampirestudios.arrp.json.models.JModel;
import net.vampirestudios.arrp.json.recipe.JRecipe;
import net.vampirestudios.arrp.json.tags.JTag;
import net.vampirestudios.arrp.json.timeline.JTimeline;
import net.vampirestudios.arrp.json.worldgen.biome.JBiome;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimension;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimensionType;
import net.vampirestudios.arrp.json.worldgen.feature.JConfiguredFeature;
import net.vampirestudios.arrp.json.worldgen.feature.JPlacedFeature;
import net.vampirestudios.arrp.json.worldgen.noise.JNoiseSettings;
import net.vampirestudios.arrp.json.worldgen.structure.JStructure;
import net.vampirestudios.arrp.json.worldgen.structure.JStructureSet;
import net.vampirestudios.arrp.util.CallableFunction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * a resource pack whose assets and data are evaluated at runtime
 * <p>
 * remember to register it!
 * @see RRPCallback
 */
public interface RuntimeResourcePack extends PackResources {

	/**
	 * The GSONs used to serialize objects to JSON.
	 */
	Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(Identifier.class, JsonSerializers.IDENTIFIER)
			.registerTypeHierarchyAdapter(StringRepresentable.class, JsonSerializers.STRING_IDENTIFIABLE)
			.registerTypeHierarchyAdapter(Vector3f.class, JsonSerializers.VECTOR_3F)
			.registerTypeHierarchyAdapter(Either.class, JsonSerializers.EITHER)
			.enableComplexMapKeySerialization()
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.create();
	/**
	 * create a new runtime resource pack with the default supported resource pack version
	 */
	@Contract("_ -> new")
	static RuntimeResourcePack create(String id) {
		return new RuntimeResourcePackImpl(Identifier.tryParse(id));
	}

	@Contract("_, _ -> new")
	static RuntimeResourcePack create(String id, int version) {
		return new RuntimeResourcePackImpl(Identifier.tryParse(id), version);
	}

	@Contract("_ -> new")
	static RuntimeResourcePack create(Identifier id) {
		return new RuntimeResourcePackImpl(id);
	}

	@Contract("_, _ -> new")
	static RuntimeResourcePack create(Identifier id, int version) {
		return new RuntimeResourcePackImpl(id, version);
	}

	static Identifier id(String string) {return Identifier.tryParse(string);}

	static Identifier id(String namespace, String string) {return Identifier.tryBuild(namespace, string);}


	/**
	 * reads, clones, and recolors the texture at the given path, and puts the newly created image in the given id.
	 *
	 * <b>if your resource pack is registered at a higher priority than where you expect the texture to be in, mc will
	 * be unable to find the asset you are looking for</b>
	 *
	 * @param identifier the place to put the new texture
	 * @param target the input stream of the original texture
	 * @param pixel the pixel recolorer
	 */
	void addRecoloredImage(Identifier identifier, InputStream target, IntUnaryOperator pixel);

	/**
	 * add a lang file for the given language
	 *
	 * DO **NOT** CALL THIS METHOD MULTIPLE TIMES FOR THE SAME LANGUAGE, THEY WILL OVERRIDE EACH OTHER!
	 * <p>
	 * ex. addLang(MyMod.id("en_us"), lang().translate("something.something", "test"))
	 */
	byte[] addLang(Identifier identifier, JLang lang);

	/**
	 * Multiple calls to this method with the same identifier will merge them into one lang file
	 */
	void mergeLang(Identifier identifier, JLang lang);

	/**
	 * adds a loot table
	 */
	byte[] addLootTable(Identifier identifier, JLootTable table);

	byte[] addWolfVariant(Identifier id, JWolfVariant variant);

	byte[] addZombieNautilusVariant(Identifier id, JZombieNautilusVariant variant);

	byte[] addChickenVariant(Identifier id, JChickenVariant variant);
	byte[] addCowVariant(Identifier id, JCowVariant variant);
	byte[] addPigVariant(Identifier id, JPigVariant variant);
	byte[] addWolfSoundVariant(Identifier id, JWolfSoundVariant variant);

	byte[] addSimpleMobVariant(Identifier variantFolder, Identifier id, JSimpleMobVariant variant);

	byte[] addCatVariant(Identifier id, JSimpleMobVariant variant);

	byte[] addFrogVariant(Identifier id, JSimpleMobVariant variant);

	/**
	 * adds an async resource, this is evaluated off-thread, this does not hold all resource retrieval unlike
	 *
	 * @see #async(Consumer)
	 */
	Future<byte[]> addAsyncResource(PackType type,
			Identifier identifier,
			CallableFunction<Identifier, byte[]> data);

	/**
	 * add a resource that is lazily evaluated
	 */
	void addLazyResource(PackType type, Identifier path, BiFunction<RuntimeResourcePack, Identifier, byte[]> data);

	/**
	 * add a raw resource
	 */
	byte[] addResource(PackType type, Identifier path, byte[] data);

	/**
	 * adds an async root resource, this is evaluated off-thread, this does not hold all resource retrieval unlike
	 *
	 * A root resource is something like pack.png, pack.mcmeta, etc. By default ARRP generates a default mcmeta
	 * @see #async(Consumer)
	 */
	Future<byte[]> addAsyncRootResource(String path,
			CallableFunction<String, byte[]> data);

	/**
	 * add a root resource that is lazily evaluated.
	 *
	 * A root resource is something like pack.png, pack.mcmeta, etc. By default ARRP generates a default mcmeta
	 */
	void addLazyRootResource(String path, BiFunction<RuntimeResourcePack, String, byte[]> data);

	/**
	 * add a raw resource to the root path
	 *
	 * A root resource is something like pack.png, pack.mcmeta, etc. By default, default mcmeta will be generated.
	 */
	byte[] addRootResource(String path, byte[] data);

	/**
	 * add a simple pack.mcmeta using the provided pack format version
	 */
	byte[] addPackMcmeta(String description, int packFormat);

	/**
	 * add a clientside resource
	 */
	byte[] addAsset(Identifier path, byte[] data);

	/**
	 * add a serverside resource
	 */
	byte[] addData(Identifier path, byte[] data);

	/**
	 * add a model, Items should go in item/... and Blocks in block/... ex. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	byte[] addAdvancement(JAdvancement model, Identifier path);

	/**
	 * add a model, Items should go in item/... and Blocks in block/... ex. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	byte[] addModel(JModel model, Identifier path);

	/**
	 * add a item model info, Goes in items/. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	byte[] addItemModelInfo(JItemInfo model, Identifier path);


	/**
	 * Write a vanilla-style equipment‐model JSON to
	 * assets/<namespace>/equipment/<path>.json
	 */
	byte[] addEquipmentModel(JEquipmentModel model, Identifier path);

	/**
	 * adds a blockstate json
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	byte[] addBlockState(JState state, Identifier path);

	/**
	 * adds a texture png
	 * <p>
	 * ".png" is automatically appended to the path
	 */
	byte[] addTexture(Identifier id, BufferedImage image);

	/**
	 * adds an animation json
	 * <p>
	 * ".png.mcmeta" is automatically appended to the path
	 */
	byte[] addAnimation(Identifier id, JAnimation animation);

	/**
	 * add a tag under the id
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	byte[] addTag(Identifier id, JTag tag);

	/**
	 * add a recipe
	 * <p>
	 * ".json" is automatically appended to the path
	 *
	 * @param id the {@linkplain Identifier} identifier of the recipe and that represents its directory
	 * @param recipe the recipe to add
	 * @return the new resource
	 */
	byte[] addRecipe(Identifier id, JRecipe recipe);

	byte[] addTimeline(Identifier id, JTimeline timeline);

	byte[] addBiome(Identifier id, JBiome biome);

	byte[] addDimension(Identifier id, JDimension dimension);

	byte[] addDimensionType(Identifier id, JDimensionType dimensionType);

	byte[] addConfiguredFeature(Identifier id, JConfiguredFeature configuredFeature);

	byte[] addPlacedFeature(Identifier id, JPlacedFeature placedFeature);

	byte[] addNoiseSettings(Identifier id, JNoiseSettings noiseSettings);

	byte[] addStructure(Identifier id, JStructure structure);

	byte[] addStructureSet(Identifier id, JStructureSet structureSet);

	/**
	 * invokes the action on the RRP executor, RRPs are thread-safe you can create expensive assets here, all resources
	 * are blocked until all async tasks are completed invokes the action on the RRP executor, RRPs are thread-safe you
	 * can create expensive assets here, all resources are blocked until all async tasks are completed
	 * <p>
	 * calling an this function from itself will result in a infinite loop
	 *
	 * @see #addAsyncResource(PackType, Identifier, CallableFunction)
	 */
	Future<?> async(Consumer<RuntimeResourcePack> action);

	Path DEFAULT_OUTPUT = Path.of("rrp.debug");

	/**
	 * forcefully dump all assets and data
	 */
	default void dump() {
		this.dump(DEFAULT_OUTPUT);
	}

	void dumpDirect(Path path);

	void load(Path path) throws IOException;

	/**
	 * forcefully dump all assets and data into `namespace;path/`, useful for debugging
	 */
	default void dump(Path path) {
		String id = this.location().id();
		Path folder = path.resolve(id);
		this.dumpDirect(folder);
	}

	/**
	 * @see ByteBufOutputStream
	 */
	void dump(ZipOutputStream stream) throws IOException;

	/**
	 * @see ByteBufInputStream
	 */
	void load(ZipInputStream stream) throws IOException;
}
