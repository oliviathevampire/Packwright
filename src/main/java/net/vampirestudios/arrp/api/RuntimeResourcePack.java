package net.vampirestudios.arrp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.StringRepresentable;
import net.vampirestudios.arrp.JsonSerializers;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.data.advancement.Advancement;
import net.vampirestudios.arrp.assets.animation.Animation;
import net.vampirestudios.arrp.assets.blockstates.BlockState;
import net.vampirestudios.arrp.data.entity.*;
import net.vampirestudios.arrp.assets.equipment.EquipmentModel;
import net.vampirestudios.arrp.assets.equipment.TrimMaterial;
import net.vampirestudios.arrp.assets.equipment.TrimPattern;
import net.vampirestudios.arrp.assets.item.ItemModelDefinition;
import net.vampirestudios.arrp.assets.lang.Lang;
import net.vampirestudios.arrp.data.loot.LootTable;
import net.vampirestudios.arrp.assets.models.Model;
import net.vampirestudios.arrp.data.recipe.Recipe;
import net.vampirestudios.arrp.data.registry.*;
import net.vampirestudios.arrp.data.tags.Tag;
import net.vampirestudios.arrp.assets.timeline.Timeline;
import net.vampirestudios.arrp.data.worldgen.*;
import net.vampirestudios.arrp.data.worldgen.biome.Biome;
import net.vampirestudios.arrp.data.worldgen.dimension.Dimension;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;
import net.vampirestudios.arrp.data.worldgen.feature.ConfiguredFeature;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.arrp.data.worldgen.structure.Structure;
import net.vampirestudios.arrp.data.worldgen.structure.StructureSet;
import net.vampirestudios.arrp.util.CallableFunction;
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
	byte[] addLang(Identifier identifier, Lang lang);

	/**
	 * Multiple calls to this method with the same identifier will merge them into one lang file
	 */
	void mergeLang(Identifier identifier, Lang lang);

	/**
	 * adds a loot table
	 */
	byte[] addLootTable(Identifier identifier, LootTable table);

	byte[] addEnchantment(Identifier id, Enchantment enchantment);

	byte[] addWolfVariant(Identifier id, WolfVariant variant);

	byte[] addZombieNautilusVariant(Identifier id, ZombieNautilusVariant variant);

	byte[] addChickenVariant(Identifier id, ChickenVariant variant);
	byte[] addCowVariant(Identifier id, CowVariant variant);
	byte[] addPigVariant(Identifier id, PigVariant variant);
	byte[] addWolfSoundVariant(Identifier id, WolfSoundVariant variant);

	byte[] addCatSoundVariant(Identifier id, CatSoundVariant variant);

	byte[] addChickenSoundVariant(Identifier id, ChickenSoundVariant variant);

	byte[] addCowSoundVariant(Identifier id, CowSoundVariant variant);

	byte[] addPigSoundVariant(Identifier id, PigSoundVariant variant);

	byte[] addSimpleMobVariant(Identifier variantFolder, Identifier id, SimpleMobVariant variant);

	byte[] addCatVariant(Identifier id, SimpleMobVariant variant);

	byte[] addFrogVariant(Identifier id, SimpleMobVariant variant);

	byte[] addPaintingVariant(Identifier id, PaintingVariant variant);

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
	byte[] addAdvancement(Advancement model, Identifier path);

	/**
	 * add a model, Items should go in item/... and Blocks in block/... ex. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	byte[] addModel(Model model, Identifier path);

	/**
	 * add a item model info, Goes in items/. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	byte[] addItemModelInfo(ItemModelDefinition model, Identifier path);


	/**
	 * Write a vanilla-style equipment‐model JSON to
	 * assets/<namespace>/equipment/<path>.json
	 */
	byte[] addEquipmentModel(EquipmentModel model, Identifier path);

	byte[] addTrimMaterial(Identifier id, TrimMaterial material);

	byte[] addTrimPattern(Identifier id, TrimPattern pattern);

	byte[] addBannerPattern(Identifier id, BannerPattern pattern);

	byte[] addDecoratedPotPattern(Identifier id, DecoratedPotPattern pattern);

	byte[] addDamageType(Identifier id, DamageType damageType);

	byte[] addInstrument(Identifier id, Instrument instrument);

	byte[] addJukeboxSong(Identifier id, JukeboxSong song);

	byte[] addConfiguredCarver(Identifier id, ConfiguredCarver configuredCarver);

	byte[] addProcessorList(Identifier id, ProcessorList processorList);

	byte[] addTemplatePool(Identifier id, TemplatePool templatePool);

	byte[] addWorldPreset(Identifier id, WorldPreset worldPreset);

	byte[] addFlatLevelGeneratorPreset(Identifier id, FlatLevelGeneratorPreset preset);

	byte[] addTradeSet(Identifier id, TradeSet tradeSet);

	byte[] addVillagerTrade(Identifier id, VillagerTrade trade);

	byte[] addDialog(Identifier id, Dialog dialog);

	byte[] addWorldClock(Identifier id, WorldClock clock);

	/**
	 * adds a blockstate json
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	byte[] addBlockState(BlockState state, Identifier path);

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
	byte[] addAnimation(Identifier id, Animation animation);

	/**
	 * add a tag under the id
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	byte[] addTag(Identifier id, Tag tag);

	/**
	 * add a recipe
	 * <p>
	 * ".json" is automatically appended to the path
	 *
	 * @param id the {@linkplain Identifier} identifier of the recipe and that represents its directory
	 * @param recipe the recipe to add
	 * @return the new resource
	 */
	byte[] addRecipe(Identifier id, Recipe recipe);

	byte[] addRecipe(Identifier id, RecipeBuilder recipe);

	default void addRecipes(java.util.Map<Identifier, Recipe> recipes) {
		recipes.forEach(this::addRecipe);
	}

	default void addTags(java.util.Map<Identifier, Tag> tags) {
		tags.forEach(this::addTag);
	}

	byte[] addTimeline(Identifier id, Timeline timeline);

	byte[] addBiome(Identifier id, Biome biome);

	byte[] addDimension(Identifier id, Dimension dimension);

	byte[] addDimensionType(Identifier id, DimensionType dimensionType);

	byte[] addConfiguredFeature(Identifier id, ConfiguredFeature configuredFeature);

	byte[] addPlacedFeature(Identifier id, PlacedFeature placedFeature);

	byte[] addNoiseSettings(Identifier id, NoiseSettings noiseSettings);

	byte[] addStructure(Identifier id, Structure structure);

	byte[] addStructureSet(Identifier id, StructureSet structureSet);

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
