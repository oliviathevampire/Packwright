package net.vampirestudios.packwright.api;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.vampirestudios.packwright.assets.animation.Animation;
import net.vampirestudios.packwright.assets.atlas.Atlas;
import net.vampirestudios.packwright.assets.blockstates.BlockState;
import net.vampirestudios.packwright.assets.equipment.EquipmentModel;
import net.vampirestudios.packwright.assets.equipment.TrimMaterial;
import net.vampirestudios.packwright.assets.equipment.TrimPattern;
import net.vampirestudios.packwright.assets.font.Font;
import net.vampirestudios.packwright.assets.item.ItemModelDefinition;
import net.vampirestudios.packwright.assets.lang.Lang;
import net.vampirestudios.packwright.assets.models.Model;
import net.vampirestudios.packwright.assets.texture.TextureMeta;
import net.vampirestudios.packwright.assets.timeline.Timeline;
import net.vampirestudios.packwright.data.advancement.Advancement;
import net.vampirestudios.packwright.data.entity.*;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.LootTable;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.recipe.Recipe;
import net.vampirestudios.packwright.data.registry.*;
import net.vampirestudios.packwright.data.registry.dialog.Dialog;
import net.vampirestudios.packwright.data.tags.Tag;
import net.vampirestudios.packwright.data.worldgen.*;
import net.vampirestudios.packwright.data.worldgen.biome.Biome;
import net.vampirestudios.packwright.data.worldgen.dimension.Dimension;
import net.vampirestudios.packwright.data.worldgen.dimension.DimensionType;
import net.vampirestudios.packwright.data.worldgen.feature.Feature;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.packwright.data.worldgen.material.MaterialCondition;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;
import net.vampirestudios.packwright.data.worldgen.noise.DensityFunction;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseParameters;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.packwright.data.worldgen.structure.Structure;
import net.vampirestudios.packwright.data.worldgen.structure.StructureSet;
import net.vampirestudios.packwright.impl.RuntimeResourcePackImpl;
import net.vampirestudios.packwright.util.CallableFunction;
import net.vampirestudios.packwright.util.ImageUtils;
import org.jetbrains.annotations.Contract;

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
 * @see PackwrightCallback
 */
public interface RuntimeResourcePack extends PackResources {
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
	 * serializes the value with the resource type's codec and adds it at
	 * {@code <side>/<namespace>/<directory>/<path>.<extension>}
	 * <p>
	 * this is the generic entry point behind all the {@code addX} convenience methods; use it with
	 * a constant from {@link ResourceTypes}, or with a custom {@link ResourceType} for content this
	 * library has no dedicated method for
	 *
	 * @param type  the resource type describing directory, pack side and codec
	 * @param id    the identifier of the resource; directory prefix and file extension are appended automatically
	 * @param value the value to serialize
	 * @return the serialized resource
	 */
	<T> byte[] add(ResourceType<T> type, Identifier id, T value);

//	<T> byte[] add(PackResourceKey<T> key, T value);
//
//	boolean contains(PackResourceKey<?> key);
//
//	void remove(PackResourceKey<?> key);
//
//	<T> Optional<T> getValue(PackResourceKey<T> key);

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
	default byte[] addLang(Identifier identifier, Lang lang) {
		return this.add(ResourceTypes.LANG, identifier, lang);
	}

	/**
	 * Multiple calls to this method with the same identifier will merge them into one lang file
	 */
	void mergeLang(Identifier identifier, Lang lang);

	/**
	 * adds a loot table
	 */
	default byte[] addLootTable(Identifier identifier, LootTable table) {
		return this.add(ResourceTypes.LOOT_TABLE, identifier, table);
	}

	default byte[] addEnchantment(Identifier id, Enchantment enchantment) {
		return this.add(ResourceTypes.ENCHANTMENT, id, enchantment);
	}

	default byte[] addWolfVariant(Identifier id, WolfVariant variant) {
		return this.add(ResourceTypes.WOLF_VARIANT, id, variant);
	}

	default byte[] addZombieNautilusVariant(Identifier id, ZombieNautilusVariant variant) {
		return this.add(ResourceTypes.ZOMBIE_NAUTILUS_VARIANT, id, variant);
	}

	default byte[] addChickenVariant(Identifier id, ChickenVariant variant) {
		return this.add(ResourceTypes.CHICKEN_VARIANT, id, variant);
	}

	default byte[] addCowVariant(Identifier id, CowVariant variant) {
		return this.add(ResourceTypes.COW_VARIANT, id, variant);
	}

	default byte[] addPigVariant(Identifier id, PigVariant variant) {
		return this.add(ResourceTypes.PIG_VARIANT, id, variant);
	}

	default byte[] addWolfSoundVariant(Identifier id, WolfSoundVariant variant) {
		return this.add(ResourceTypes.WOLF_SOUND_VARIANT, id, variant);
	}

	default byte[] addCatSoundVariant(Identifier id, CatSoundVariant variant) {
		return this.add(ResourceTypes.CAT_SOUND_VARIANT, id, variant);
	}

	default byte[] addChickenSoundVariant(Identifier id, ChickenSoundVariant variant) {
		return this.add(ResourceTypes.CHICKEN_SOUND_VARIANT, id, variant);
	}

	default byte[] addCowSoundVariant(Identifier id, CowSoundVariant variant) {
		return this.add(ResourceTypes.COW_SOUND_VARIANT, id, variant);
	}

	default byte[] addPigSoundVariant(Identifier id, PigSoundVariant variant) {
		return this.add(ResourceTypes.PIG_SOUND_VARIANT, id, variant);
	}

	/**
	 * adds a mob variant under the given variant folder, e.g. {@code cat_variant} or {@code frog_variant}
	 */
	default byte[] addSimpleMobVariant(Identifier variantFolder, Identifier id, SimpleMobVariant variant) {
		return this.add(ResourceType.data(variantFolder.getPath(), SimpleMobVariant.CODEC), id, variant);
	}

	default byte[] addCatVariant(Identifier id, CatVariant variant) {
		return this.add(ResourceTypes.CAT_VARIANT, id, variant);
	}

	default byte[] addFrogVariant(Identifier id, FrogVariant variant) {
		return this.add(ResourceTypes.FROG_VARIANT, id, variant);
	}

	default byte[] addPaintingVariant(Identifier id, PaintingVariant variant) {
		return this.add(ResourceTypes.PAINTING_VARIANT, id, variant);
	}

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
	 * A root resource is something like pack.png, pack.mcmeta, etc. By default Packwright generates a default mcmeta
	 * @see #async(Consumer)
	 */
	Future<byte[]> addAsyncRootResource(String path,
			CallableFunction<String, byte[]> data);

	/**
	 * add a root resource that is lazily evaluated.
	 *
	 * A root resource is something like pack.png, pack.mcmeta, etc. By default Packwright generates a default mcmeta
	 */
	void addLazyRootResource(String path, BiFunction<RuntimeResourcePack, String, byte[]> data);

	/**
	 * add a raw resource to the root path
	 *
	 * A root resource is something like pack.png, pack.mcmeta, etc. By default, default mcmeta will be generated.
	 */
	byte[] addRootResource(String path, byte[] data);

	/** current data pack format major version (26.3 = {@code 110.0}) */
	int DATA_PACK_FORMAT = 110;
	/** current resource pack format major version (26.3 = {@code 91.0}) */
	int RESOURCE_PACK_FORMAT = 91;

	/**
	 * writes a legacy {@code pack.mcmeta} with a single integer {@code pack_format}
	 *
	 * @see #addPackMcmeta(String, int, int)
	 */
	byte[] addPackMcmeta(String description, int packFormat);

	/**
	 * writes a {@code pack.mcmeta} in the modern format, with {@code min_format} and
	 * {@code max_format} both set to {@code [formatMajor, formatMinor]}
	 */
	byte[] addPackMcmeta(String description, int formatMajor, int formatMinor);

	/** writes a {@code pack.mcmeta} for the current data pack format ({@value #DATA_PACK_FORMAT}.0) */
	default byte[] addDataPackMcmeta(String description) {
		return this.addPackMcmeta(description, DATA_PACK_FORMAT, 0);
	}

	/** writes a {@code pack.mcmeta} for the current resource pack format ({@value #RESOURCE_PACK_FORMAT}.0) */
	default byte[] addResourcePackMcmeta(String description) {
		return this.addPackMcmeta(description, RESOURCE_PACK_FORMAT, 0);
	}

	/**
	 * add a clientside resource
	 */
	byte[] addAsset(Identifier path, byte[] data);

	/**
	 * add a serverside resource
	 */
	byte[] addData(Identifier path, byte[] data);

	/**
	 * add an advancement; ".json" is automatically appended to the path
	 */
	default byte[] addAdvancement(Advancement advancement, Identifier path) {
		return this.add(ResourceTypes.ADVANCEMENT, path, advancement);
	}

	/**
	 * add a model, Items should go in item/... and Blocks in block/... ex. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	default byte[] addModel(Model model, Identifier path) {
		return this.add(ResourceTypes.MODEL, path, model);
	}

	/**
	 * add a item model info, Goes in items/. mymod:items/my_item ".json" is
	 * automatically
	 * appended to the path
	 */
	default byte[] addItemModelInfo(ItemModelDefinition model, Identifier path) {
		return this.add(ResourceTypes.ITEM_MODEL_DEFINITION, path, model);
	}


	/**
	 * Write a vanilla-style equipment‐model JSON to
	 * assets/<namespace>/equipment/<path>.json
	 */
	default byte[] addEquipmentModel(EquipmentModel model, Identifier path) {
		return this.add(ResourceTypes.EQUIPMENT_MODEL, path, model);
	}

	default byte[] addTrimMaterial(Identifier id, TrimMaterial material) {
		return this.add(ResourceTypes.TRIM_MATERIAL, id, material);
	}

	default byte[] addTrimPattern(Identifier id, TrimPattern pattern) {
		return this.add(ResourceTypes.TRIM_PATTERN, id, pattern);
	}

	default byte[] addBannerPattern(Identifier id, BannerPattern pattern) {
		return this.add(ResourceTypes.BANNER_PATTERN, id, pattern);
	}

	default byte[] addDecoratedPotPattern(Identifier id, DecoratedPotPattern pattern) {
		return this.add(ResourceTypes.DECORATED_POT_PATTERN, id, pattern);
	}

	default byte[] addDamageType(Identifier id, DamageType damageType) {
		return this.add(ResourceTypes.DAMAGE_TYPE, id, damageType);
	}

	default byte[] addInstrument(Identifier id, Instrument instrument) {
		return this.add(ResourceTypes.INSTRUMENT, id, instrument);
	}

	default byte[] addJukeboxSong(Identifier id, JukeboxSong song) {
		return this.add(ResourceTypes.JUKEBOX_SONG, id, song);
	}

	default byte[] addCarver(Identifier id, Carver carver) {
		return this.add(ResourceTypes.CARVER, id, carver);
	}

	default byte[] addProcessorList(Identifier id, ProcessorList processorList) {
		return this.add(ResourceTypes.PROCESSOR_LIST, id, processorList);
	}

	default byte[] addTemplatePool(Identifier id, TemplatePool templatePool) {
		return this.add(ResourceTypes.TEMPLATE_POOL, id, templatePool);
	}

	default byte[] addWorldPreset(Identifier id, WorldPreset worldPreset) {
		return this.add(ResourceTypes.WORLD_PRESET, id, worldPreset);
	}

	default byte[] addFlatLevelGeneratorPreset(Identifier id, FlatLevelGeneratorPreset preset) {
		return this.add(ResourceTypes.FLAT_LEVEL_GENERATOR_PRESET, id, preset);
	}

	default byte[] addTradeSet(Identifier id, TradeSet tradeSet) {
		return this.add(ResourceTypes.TRADE_SET, id, tradeSet);
	}

	default byte[] addVillagerTrade(Identifier id, VillagerTrade trade) {
		return this.add(ResourceTypes.VILLAGER_TRADE, id, trade);
	}

	default byte[] addDialog(Identifier id, Dialog dialog) {
		return this.add(ResourceTypes.DIALOG, id, dialog);
	}

	default byte[] addWorldClock(Identifier id, WorldClock clock) {
		return this.add(ResourceTypes.WORLD_CLOCK, id, clock);
	}

	/**
	 * adds a blockstate json
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	default byte[] addBlockState(BlockState state, Identifier path) {
		return this.add(ResourceTypes.BLOCK_STATE, path, state);
	}

	/**
	 * adds a texture png
	 * <p>
	 * ".png" is automatically appended to the path
	 */
	byte[] addTexture(Identifier id, BufferedImage image);

	/**
	 * adds a texture png together with its animation mcmeta
	 *
	 * @see #addAnimation(Identifier, Animation)
	 */
	default byte[] addTexture(Identifier id, BufferedImage image, Animation animation) {
		this.addAnimation(id, animation);
		return this.addTexture(id, image);
	}

	/**
	 * adds a texture png together with its full mcmeta
	 *
	 * @see #addTextureMeta(Identifier, TextureMeta)
	 */
	default byte[] addTexture(Identifier id, BufferedImage image, TextureMeta meta) {
		this.addTextureMeta(id, meta);
		return this.addTexture(id, image);
	}

	/**
	 * stitches the given equally-sized frames vertically into one animation
	 * strip and adds it together with the animation mcmeta
	 *
	 * @see ImageUtils#stitchFrames(BufferedImage...)
	 */
	default byte[] addAnimatedTexture(Identifier id, Animation animation, BufferedImage... frames) {
		return this.addTexture(id, ImageUtils.stitchFrames(frames), animation);
	}

	/**
	 * adds a texture recolored from the given source image; a convenience for
	 * {@code addTexture(id, ImageUtils.recolor(source, pixel))}
	 *
	 * @see #addRecoloredImage(Identifier, InputStream, IntUnaryOperator)
	 * @see ImageUtils#recolor(BufferedImage, IntUnaryOperator)
	 */
	default byte[] addRecoloredTexture(Identifier id, BufferedImage source, IntUnaryOperator pixel) {
		return this.addTexture(id, ImageUtils.recolor(source, pixel));
	}

	/**
	 * adds a texture created by multiplying the source's color channels with
	 * the given color — the usual way to make colored variants of a white or
	 * grayscale template
	 *
	 * @param color RGB color, e.g. {@code 0xFF8800}
	 * @see ImageUtils#tint(BufferedImage, int)
	 */
	default byte[] addTintedTexture(Identifier id, BufferedImage source, int color) {
		return this.addTexture(id, ImageUtils.tint(source, color));
	}

	/**
	 * adds a texture created by mapping the grayscale template's luminance
	 * onto the given color ramp (index 0 = darkest)
	 *
	 * @see ImageUtils#grayscaleToPalette(BufferedImage, int[])
	 */
	default byte[] addPaletteSwappedTexture(Identifier id, BufferedImage grayscaleTemplate, int[] palette) {
		return this.addTexture(id, ImageUtils.grayscaleToPalette(grayscaleTemplate, palette));
	}

	/**
	 * adds an animation json
	 * <p>
	 * ".png.mcmeta" is automatically appended to the path
	 */
	byte[] addAnimation(Identifier id, Animation animation);

	/**
	 * adds a full texture mcmeta, which can combine the {@code animation},
	 * {@code texture}, {@code gui} and {@code villager} sections
	 * <p>
	 * ".png.mcmeta" is automatically appended to the path
	 */
	byte[] addTextureMeta(Identifier id, TextureMeta meta);

	/**
	 * adds an atlas configuration at {@code assets/<namespace>/atlases/<path>.json};
	 * atlas files of the same name are merged across packs, so sources are usually
	 * added to a vanilla atlas id like {@code minecraft:blocks}
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	default byte[] addAtlas(Identifier id, Atlas atlas) {
		return this.add(ResourceTypes.ATLAS, id, atlas);
	}

	/**
	 * adds a font definition at {@code assets/<namespace>/font/<path>.json}
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	default byte[] addFont(Identifier id, Font font) {
		return this.add(ResourceTypes.FONT, id, font);
	}

	/**
	 * sets the pack icon by writing the image to {@code pack.png}
	 */
	default byte[] setIcon(BufferedImage icon) {
		return this.addRootResource("pack.png", ImageUtils.toPngBytes(icon));
	}

	/**
	 * add a tag under the id
	 * <p>
	 * ".json" is automatically appended to the path
	 */
	default byte[] addTag(Identifier id, Tag tag) {
		return this.add(ResourceTypes.TAG, id, tag);
	}

	/**
	 * add a recipe
	 * <p>
	 * ".json" is automatically appended to the path
	 *
	 * @param id the {@linkplain Identifier} identifier of the recipe and that represents its directory
	 * @param recipe the recipe to add
	 * @return the new resource
	 */
	default byte[] addRecipe(Identifier id, Recipe recipe) {
		return this.add(ResourceTypes.RECIPE, id, recipe);
	}

	byte[] addRecipe(Identifier id, RecipeBuilder recipe);

	default void addRecipes(java.util.Map<Identifier, Recipe> recipes) {
		recipes.forEach(this::addRecipe);
	}

	default void addTags(java.util.Map<Identifier, Tag> tags) {
		tags.forEach(this::addTag);
	}

	default byte[] addTimeline(Identifier id, Timeline timeline) {
		return this.add(ResourceTypes.TIMELINE, id, timeline);
	}

	default byte[] addBiome(Identifier id, Biome biome) {
		return this.add(ResourceTypes.BIOME, id, biome);
	}

	default byte[] addDimension(Identifier id, Dimension dimension) {
		return this.add(ResourceTypes.DIMENSION, id, dimension);
	}

	default byte[] addDimensionType(Identifier id, DimensionType dimensionType) {
		return this.add(ResourceTypes.DIMENSION_TYPE, id, dimensionType);
	}

	default byte[] addFeature(Identifier id, Feature feature) {
		return this.add(ResourceTypes.FEATURE, id, feature);
	}

	default byte[] addPlacedFeature(Identifier id, PlacedFeature placedFeature) {
		return this.add(ResourceTypes.PLACED_FEATURE, id, placedFeature);
	}

	default byte[] addNoiseSettings(Identifier id, NoiseSettings noiseSettings) {
		return this.add(ResourceTypes.NOISE_SETTINGS, id, noiseSettings);
	}

	default byte[] addNoise(Identifier id, NoiseParameters noise) {
		return this.add(ResourceTypes.NOISE, id, noise);
	}

	default byte[] addDensityFunction(Identifier id, DensityFunction densityFunction) {
		return this.add(ResourceTypes.DENSITY_FUNCTION, id, densityFunction);
	}

	/** adds a reusable loot condition to the {@code predicate} registry */
	default byte[] addPredicate(Identifier id, Condition predicate) {
		return this.add(ResourceTypes.PREDICATE, id, predicate);
	}

	/** adds a reusable loot function to the {@code item_modifier} registry */
	default byte[] addItemModifier(Identifier id, LootFunction modifier) {
		return this.add(ResourceTypes.ITEM_MODIFIER, id, modifier);
	}

	default byte[] addChatType(Identifier id, ChatType chatType) {
		return this.add(ResourceTypes.CHAT_TYPE, id, chatType);
	}

	default byte[] addEnchantmentProvider(Identifier id, EnchantmentProvider enchantmentProvider) {
		return this.add(ResourceTypes.ENCHANTMENT_PROVIDER, id, enchantmentProvider);
	}

	/** adds a reusable slot source (since 26.3) */
	default byte[] addSlotSource(Identifier id, SlotSource slotSource) {
		return this.add(ResourceTypes.SLOT_SOURCE, id, slotSource);
	}

	/**
	 * adds a function at {@code data/<namespace>/function/<path>.mcfunction},
	 * one command per list entry
	 */
	default byte[] addMcFunction(Identifier id, java.util.List<String> commands) {
		byte[] bytes = (String.join("\n", commands) + "\n").getBytes(java.nio.charset.StandardCharsets.UTF_8);
		return this.addData(Identifier.fromNamespaceAndPath(id.getNamespace(), "function/" + id.getPath() + ".mcfunction"), bytes);
	}

	/** adds an element of the {@code number_provider} registry (since 26.3) */
	default byte[] addNumberProvider(Identifier id, NumberProvider numberProvider) {
		return this.add(ResourceTypes.NUMBER_PROVIDER, id, numberProvider);
	}

	default byte[] addMaterialRule(Identifier id, MaterialRule materialRule) {
		return this.add(ResourceTypes.MATERIAL_RULE, id, materialRule);
	}

	default byte[] addMaterialCondition(Identifier id, MaterialCondition materialCondition) {
		return this.add(ResourceTypes.MATERIAL_CONDITION, id, materialCondition);
	}

	default byte[] addStructure(Identifier id, Structure structure) {
		return this.add(ResourceTypes.STRUCTURE, id, structure);
	}

	default byte[] addStructureSet(Identifier id, StructureSet structureSet) {
		return this.add(ResourceTypes.STRUCTURE_SET, id, structureSet);
	}

	/**
	 * invokes the action on the Packwright executor, RRPs are thread-safe you can create expensive assets here, all resources
	 * are blocked until all async tasks are completed
	 * <p>
	 * calling an this function from itself will result in a infinite loop
	 *
	 * @see #addAsyncResource(PackType, Identifier, CallableFunction)
	 */
	Future<?> async(Consumer<RuntimeResourcePack> action);

	/**
	 * forcefully dump all assets and data into the configured dump directory
	 * ({@code dump_directory} in {@code config/packwright.properties}, {@code packwright.debug} by default)
	 */
	default void dump() {
		this.dump(RuntimeResourcePackImpl.CONFIG.dumpDirectory);
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
