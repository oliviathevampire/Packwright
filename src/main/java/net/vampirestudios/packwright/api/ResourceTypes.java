package net.vampirestudios.packwright.api;

import net.vampirestudios.packwright.assets.atlas.Atlas;
import net.vampirestudios.packwright.assets.blockstates.BlockState;
import net.vampirestudios.packwright.assets.font.Font;
import net.vampirestudios.packwright.assets.equipment.EquipmentModel;
import net.vampirestudios.packwright.assets.equipment.TrimMaterial;
import net.vampirestudios.packwright.assets.equipment.TrimPattern;
import net.vampirestudios.packwright.assets.item.ItemModelDefinition;
import net.vampirestudios.packwright.assets.lang.Lang;
import net.vampirestudios.packwright.assets.models.Model;
import net.vampirestudios.packwright.assets.timeline.Timeline;
import net.vampirestudios.packwright.data.advancement.Advancement;
import net.vampirestudios.packwright.data.entity.CatSoundVariant;
import net.vampirestudios.packwright.data.entity.CatVariant;
import net.vampirestudios.packwright.data.entity.ChickenSoundVariant;
import net.vampirestudios.packwright.data.entity.ChickenVariant;
import net.vampirestudios.packwright.data.entity.CowSoundVariant;
import net.vampirestudios.packwright.data.entity.CowVariant;
import net.vampirestudios.packwright.data.entity.FrogVariant;
import net.vampirestudios.packwright.data.entity.PaintingVariant;
import net.vampirestudios.packwright.data.entity.PigSoundVariant;
import net.vampirestudios.packwright.data.entity.PigVariant;
import net.vampirestudios.packwright.data.entity.WolfSoundVariant;
import net.vampirestudios.packwright.data.entity.WolfVariant;
import net.vampirestudios.packwright.data.entity.ZombieNautilusVariant;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.LootTable;
import net.vampirestudios.packwright.data.recipe.Recipe;
import net.vampirestudios.packwright.data.registry.BannerPattern;
import net.vampirestudios.packwright.data.registry.ChatType;
import net.vampirestudios.packwright.data.registry.EnchantmentProvider;
import net.vampirestudios.packwright.data.registry.DamageType;
import net.vampirestudios.packwright.data.registry.DecoratedPotPattern;
import net.vampirestudios.packwright.data.registry.dialog.Dialog;
import net.vampirestudios.packwright.data.registry.Enchantment;
import net.vampirestudios.packwright.data.registry.Instrument;
import net.vampirestudios.packwright.data.registry.JukeboxSong;
import net.vampirestudios.packwright.data.registry.TradeSet;
import net.vampirestudios.packwright.data.registry.VillagerTrade;
import net.vampirestudios.packwright.data.registry.WorldClock;
import net.vampirestudios.packwright.data.tags.Tag;
import net.vampirestudios.packwright.data.worldgen.Carver;
import net.vampirestudios.packwright.data.worldgen.FlatLevelGeneratorPreset;
import net.vampirestudios.packwright.data.worldgen.ProcessorList;
import net.vampirestudios.packwright.data.worldgen.TemplatePool;
import net.vampirestudios.packwright.data.worldgen.WorldPreset;
import net.vampirestudios.packwright.data.worldgen.biome.Biome;
import net.vampirestudios.packwright.data.worldgen.dimension.Dimension;
import net.vampirestudios.packwright.data.worldgen.dimension.DimensionType;
import net.vampirestudios.packwright.data.worldgen.feature.Feature;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.packwright.data.worldgen.noise.DensityFunction;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseParameters;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.packwright.data.worldgen.structure.Structure;
import net.vampirestudios.packwright.data.worldgen.structure.StructureSet;

/**
 * Catalog of the {@link ResourceType}s Packwright knows how to serialize. Each constant pairs a
 * pack directory with the codec of its Packwright data class; pass one to
 * {@link RuntimeResourcePack#add(ResourceType, net.minecraft.resources.Identifier, Object)}.
 */
public final class ResourceTypes {
	private ResourceTypes() {
	}

	// assets
	public static final ResourceType<Lang> LANG = ResourceType.asset("lang", Lang.CODEC);
	public static final ResourceType<Model> MODEL = ResourceType.asset("models", Model.CODEC);
	public static final ResourceType<ItemModelDefinition> ITEM_MODEL_DEFINITION = ResourceType.asset("items", ItemModelDefinition.CODEC);
	public static final ResourceType<EquipmentModel> EQUIPMENT_MODEL = ResourceType.asset("equipment", EquipmentModel.CODEC);
	public static final ResourceType<BlockState> BLOCK_STATE = ResourceType.asset("blockstates", BlockState.CODEC);
	public static final ResourceType<Atlas> ATLAS = ResourceType.asset("atlases", Atlas.CODEC);
	public static final ResourceType<Font> FONT = ResourceType.asset("font", Font.CODEC);

	// data
	public static final ResourceType<Advancement> ADVANCEMENT = ResourceType.data("advancement", Advancement.CODEC);
	public static final ResourceType<LootTable> LOOT_TABLE = ResourceType.data("loot_table", LootTable.CODEC);
	public static final ResourceType<Recipe> RECIPE = ResourceType.data("recipe", Recipe.CODEC);
	public static final ResourceType<Tag> TAG = ResourceType.data("tags", Tag.CODEC);
	public static final ResourceType<Enchantment> ENCHANTMENT = ResourceType.data("enchantment", Enchantment.CODEC);
	public static final ResourceType<DamageType> DAMAGE_TYPE = ResourceType.data("damage_type", DamageType.CODEC);
	public static final ResourceType<Instrument> INSTRUMENT = ResourceType.data("instrument", Instrument.CODEC);
	public static final ResourceType<JukeboxSong> JUKEBOX_SONG = ResourceType.data("jukebox_song", JukeboxSong.CODEC);
	public static final ResourceType<TrimMaterial> TRIM_MATERIAL = ResourceType.data("trim_material", TrimMaterial.CODEC);
	public static final ResourceType<TrimPattern> TRIM_PATTERN = ResourceType.data("trim_pattern", TrimPattern.CODEC);
	public static final ResourceType<BannerPattern> BANNER_PATTERN = ResourceType.data("banner_pattern", BannerPattern.CODEC);
	public static final ResourceType<DecoratedPotPattern> DECORATED_POT_PATTERN = ResourceType.data("decorated_pot_pattern", DecoratedPotPattern.CODEC);
	public static final ResourceType<TradeSet> TRADE_SET = ResourceType.data("trade_set", TradeSet.CODEC);
	public static final ResourceType<VillagerTrade> VILLAGER_TRADE = ResourceType.data("villager_trade", VillagerTrade.CODEC);
	public static final ResourceType<Dialog> DIALOG = ResourceType.data("dialog", Dialog.CODEC);
	public static final ResourceType<WorldClock> WORLD_CLOCK = ResourceType.data("world_clock", WorldClock.CODEC);
	// the registry is minecraft:timeline, so its data folder is singular
	public static final ResourceType<Timeline> TIMELINE = ResourceType.data("timeline", Timeline.CODEC);
	/** reusable loot conditions, referenced via {@code minecraft:reference} conditions */
	public static final ResourceType<Condition> PREDICATE = ResourceType.data("predicate", Condition.CODEC);
	/** reusable loot functions, applied via {@code /item modify} or {@code minecraft:reference} functions */
	public static final ResourceType<LootFunction> ITEM_MODIFIER = ResourceType.data("item_modifier", LootFunction.CODEC);
	public static final ResourceType<ChatType> CHAT_TYPE = ResourceType.data("chat_type", ChatType.CODEC);
	public static final ResourceType<EnchantmentProvider> ENCHANTMENT_PROVIDER = ResourceType.data("enchantment_provider", EnchantmentProvider.CODEC);

	// entity variants
	public static final ResourceType<WolfVariant> WOLF_VARIANT = ResourceType.data("wolf_variant", WolfVariant.CODEC);
	public static final ResourceType<ZombieNautilusVariant> ZOMBIE_NAUTILUS_VARIANT = ResourceType.data("zombie_nautilus_variant", ZombieNautilusVariant.CODEC);
	public static final ResourceType<ChickenVariant> CHICKEN_VARIANT = ResourceType.data("chicken_variant", ChickenVariant.CODEC);
	public static final ResourceType<CowVariant> COW_VARIANT = ResourceType.data("cow_variant", CowVariant.CODEC);
	public static final ResourceType<PigVariant> PIG_VARIANT = ResourceType.data("pig_variant", PigVariant.CODEC);
	public static final ResourceType<CatVariant> CAT_VARIANT = ResourceType.data("cat_variant", CatVariant.CODEC);
	public static final ResourceType<FrogVariant> FROG_VARIANT = ResourceType.data("frog_variant", FrogVariant.CODEC);
	public static final ResourceType<PaintingVariant> PAINTING_VARIANT = ResourceType.data("painting_variant", PaintingVariant.CODEC);
	public static final ResourceType<WolfSoundVariant> WOLF_SOUND_VARIANT = ResourceType.data("wolf_sound_variant", WolfSoundVariant.CODEC);
	public static final ResourceType<CatSoundVariant> CAT_SOUND_VARIANT = ResourceType.data("cat_sound_variant", CatSoundVariant.CODEC);
	public static final ResourceType<ChickenSoundVariant> CHICKEN_SOUND_VARIANT = ResourceType.data("chicken_sound_variant", ChickenSoundVariant.CODEC);
	public static final ResourceType<CowSoundVariant> COW_SOUND_VARIANT = ResourceType.data("cow_sound_variant", CowSoundVariant.CODEC);
	public static final ResourceType<PigSoundVariant> PIG_SOUND_VARIANT = ResourceType.data("pig_sound_variant", PigSoundVariant.CODEC);

	// worldgen
	public static final ResourceType<Biome> BIOME = ResourceType.data("worldgen/biome", Biome.CODEC);
	public static final ResourceType<Dimension> DIMENSION = ResourceType.data("dimension", Dimension.CODEC);
	public static final ResourceType<DimensionType> DIMENSION_TYPE = ResourceType.data("dimension_type", DimensionType.CODEC);
	public static final ResourceType<Feature> FEATURE = ResourceType.data("worldgen/configured_feature", Feature.CODEC);
	public static final ResourceType<PlacedFeature> PLACED_FEATURE = ResourceType.data("worldgen/placed_feature", PlacedFeature.CODEC);
	public static final ResourceType<Carver> CARVER = ResourceType.data("worldgen/configured_carver", Carver.CODEC);
	public static final ResourceType<NoiseSettings> NOISE_SETTINGS = ResourceType.data("worldgen/noise_settings", NoiseSettings.CODEC);
	public static final ResourceType<NoiseParameters> NOISE = ResourceType.data("worldgen/noise", NoiseParameters.CODEC);
	public static final ResourceType<DensityFunction> DENSITY_FUNCTION = ResourceType.data("worldgen/density_function", DensityFunction.CODEC);
	public static final ResourceType<Structure> STRUCTURE = ResourceType.data("worldgen/structure", Structure.CODEC);
	public static final ResourceType<StructureSet> STRUCTURE_SET = ResourceType.data("worldgen/structure_set", StructureSet.CODEC);
	public static final ResourceType<ProcessorList> PROCESSOR_LIST = ResourceType.data("worldgen/processor_list", ProcessorList.CODEC);
	public static final ResourceType<TemplatePool> TEMPLATE_POOL = ResourceType.data("worldgen/template_pool", TemplatePool.CODEC);
	public static final ResourceType<WorldPreset> WORLD_PRESET = ResourceType.data("worldgen/world_preset", WorldPreset.CODEC);
	public static final ResourceType<FlatLevelGeneratorPreset> FLAT_LEVEL_GENERATOR_PRESET = ResourceType.data("worldgen/flat_level_generator_preset", FlatLevelGeneratorPreset.CODEC);
}
