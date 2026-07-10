package net.vampirestudios.arrp.api;

import com.google.gson.JsonElement;
import net.vampirestudios.arrp.assets.blockstates.BlockState;
import net.vampirestudios.arrp.assets.equipment.EquipmentModel;
import net.vampirestudios.arrp.assets.equipment.TrimMaterial;
import net.vampirestudios.arrp.assets.equipment.TrimPattern;
import net.vampirestudios.arrp.assets.item.ItemModelDefinition;
import net.vampirestudios.arrp.assets.lang.Lang;
import net.vampirestudios.arrp.assets.models.Model;
import net.vampirestudios.arrp.assets.timeline.Timeline;
import net.vampirestudios.arrp.data.advancement.Advancement;
import net.vampirestudios.arrp.data.entity.CatSoundVariant;
import net.vampirestudios.arrp.data.entity.ChickenSoundVariant;
import net.vampirestudios.arrp.data.entity.ChickenVariant;
import net.vampirestudios.arrp.data.entity.CowSoundVariant;
import net.vampirestudios.arrp.data.entity.CowVariant;
import net.vampirestudios.arrp.data.entity.PaintingVariant;
import net.vampirestudios.arrp.data.entity.PigSoundVariant;
import net.vampirestudios.arrp.data.entity.PigVariant;
import net.vampirestudios.arrp.data.entity.SimpleMobVariant;
import net.vampirestudios.arrp.data.entity.WolfSoundVariant;
import net.vampirestudios.arrp.data.entity.WolfVariant;
import net.vampirestudios.arrp.data.entity.ZombieNautilusVariant;
import net.vampirestudios.arrp.data.loot.Condition;
import net.vampirestudios.arrp.data.loot.LootFunction;
import net.vampirestudios.arrp.data.loot.LootTable;
import net.vampirestudios.arrp.data.recipe.Recipe;
import net.vampirestudios.arrp.data.registry.BannerPattern;
import net.vampirestudios.arrp.data.registry.DamageType;
import net.vampirestudios.arrp.data.registry.DecoratedPotPattern;
import net.vampirestudios.arrp.data.registry.Dialog;
import net.vampirestudios.arrp.data.registry.Enchantment;
import net.vampirestudios.arrp.data.registry.Instrument;
import net.vampirestudios.arrp.data.registry.JukeboxSong;
import net.vampirestudios.arrp.data.registry.TradeSet;
import net.vampirestudios.arrp.data.registry.VillagerTrade;
import net.vampirestudios.arrp.data.registry.WorldClock;
import net.vampirestudios.arrp.data.tags.Tag;
import net.vampirestudios.arrp.data.worldgen.Carver;
import net.vampirestudios.arrp.data.worldgen.FlatLevelGeneratorPreset;
import net.vampirestudios.arrp.data.worldgen.ProcessorList;
import net.vampirestudios.arrp.data.worldgen.TemplatePool;
import net.vampirestudios.arrp.data.worldgen.WorldPreset;
import net.vampirestudios.arrp.data.worldgen.biome.Biome;
import net.vampirestudios.arrp.data.worldgen.dimension.Dimension;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;
import net.vampirestudios.arrp.data.worldgen.feature.Feature;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.arrp.data.worldgen.structure.Structure;
import net.vampirestudios.arrp.data.worldgen.structure.StructureSet;
import net.vampirestudios.arrp.impl.Codecs;

/**
 * Catalog of the {@link ResourceType}s ARRP knows how to serialize. Each constant pairs a
 * pack directory with the codec of its ARRP data class; pass one to
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
	/** reusable number providers referenced by e.g. the {@code minecraft:compostable} component (since 26.3) */
	public static final ResourceType<JsonElement> NUMBER_PROVIDER = ResourceType.data("number_provider", Codecs.JSON);
	/** reusable loot conditions, referenced via {@code minecraft:reference} conditions */
	public static final ResourceType<Condition> PREDICATE = ResourceType.data("predicate", Condition.CODEC);
	/** reusable loot functions, applied via {@code /item modify} or {@code minecraft:reference} functions */
	public static final ResourceType<LootFunction> ITEM_MODIFIER = ResourceType.data("item_modifier", LootFunction.CODEC);
	public static final ResourceType<JsonElement> CHAT_TYPE = ResourceType.data("chat_type", Codecs.JSON);
	public static final ResourceType<JsonElement> ENCHANTMENT_PROVIDER = ResourceType.data("enchantment_provider", Codecs.JSON);
	/** reusable slot sources for {@code /item} and {@code /execute if slots|items} (since 26.3) */
	public static final ResourceType<JsonElement> SLOT_SOURCE = ResourceType.data("slot_source", Codecs.JSON);

	// entity variants
	public static final ResourceType<WolfVariant> WOLF_VARIANT = ResourceType.data("wolf_variant", WolfVariant.CODEC);
	public static final ResourceType<ZombieNautilusVariant> ZOMBIE_NAUTILUS_VARIANT = ResourceType.data("zombie_nautilus_variant", ZombieNautilusVariant.CODEC);
	public static final ResourceType<ChickenVariant> CHICKEN_VARIANT = ResourceType.data("chicken_variant", ChickenVariant.CODEC);
	public static final ResourceType<CowVariant> COW_VARIANT = ResourceType.data("cow_variant", CowVariant.CODEC);
	public static final ResourceType<PigVariant> PIG_VARIANT = ResourceType.data("pig_variant", PigVariant.CODEC);
	public static final ResourceType<SimpleMobVariant> CAT_VARIANT = ResourceType.data("cat_variant", SimpleMobVariant.CODEC);
	public static final ResourceType<SimpleMobVariant> FROG_VARIANT = ResourceType.data("frog_variant", SimpleMobVariant.CODEC);
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
	public static final ResourceType<Feature> FEATURE = ResourceType.data("worldgen/feature", Feature.CODEC);
	public static final ResourceType<PlacedFeature> PLACED_FEATURE = ResourceType.data("worldgen/placed_feature", PlacedFeature.CODEC);
	public static final ResourceType<Carver> CARVER = ResourceType.data("worldgen/carver", Carver.CODEC);
	public static final ResourceType<NoiseSettings> NOISE_SETTINGS = ResourceType.data("worldgen/noise_settings", NoiseSettings.CODEC);
	public static final ResourceType<JsonElement> NOISE = ResourceType.data("worldgen/noise", Codecs.JSON);
	public static final ResourceType<JsonElement> DENSITY_FUNCTION = ResourceType.data("worldgen/density_function", Codecs.JSON);
	public static final ResourceType<JsonElement> MATERIAL_RULE = ResourceType.data("worldgen/material_rule", Codecs.JSON);
	public static final ResourceType<JsonElement> MATERIAL_CONDITION = ResourceType.data("worldgen/material_condition", Codecs.JSON);
	public static final ResourceType<Structure> STRUCTURE = ResourceType.data("worldgen/structure", Structure.CODEC);
	public static final ResourceType<StructureSet> STRUCTURE_SET = ResourceType.data("worldgen/structure_set", StructureSet.CODEC);
	public static final ResourceType<ProcessorList> PROCESSOR_LIST = ResourceType.data("worldgen/processor_list", ProcessorList.CODEC);
	public static final ResourceType<TemplatePool> TEMPLATE_POOL = ResourceType.data("worldgen/template_pool", TemplatePool.CODEC);
	public static final ResourceType<WorldPreset> WORLD_PRESET = ResourceType.data("worldgen/world_preset", WorldPreset.CODEC);
	public static final ResourceType<FlatLevelGeneratorPreset> FLAT_LEVEL_GENERATOR_PRESET = ResourceType.data("worldgen/flat_level_generator_preset", FlatLevelGeneratorPreset.CODEC);
}
