package test;

import com.google.gson.JsonPrimitive;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.assets.timeline.Timeline;
import net.vampirestudios.arrp.data.worldgen.BlockState;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.AttributeValue;
import net.vampirestudios.arrp.data.worldgen.VerticalAnchor;
import net.vampirestudios.arrp.data.worldgen.biome.Biome;
import net.vampirestudios.arrp.data.worldgen.dimension.Dimension;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;
import net.vampirestudios.arrp.data.worldgen.feature.ConfiguredFeature;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.data.worldgen.feature.config.*;
import net.vampirestudios.arrp.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.arrp.data.worldgen.structure.Structure;
import net.vampirestudios.arrp.data.worldgen.structure.StructureSet;
import net.minecraft.tags.BlockTags;

import java.util.HashMap;
import java.util.List;

public class SkyIslandsWorldgen {

	/* ----------------------------------------------------------
	 * 1) Timeline: my_mod:sky_islands_sky_cycle
	 * ---------------------------------------------------------- */

	public static Timeline buildSkyIslandsSkyTimeline() {
		return new Timeline()
				.period(24000)
				.track("minecraft:visual/sky_color",
						new Timeline.Track()
								.ease("linear")
								.addKeyframe(new Timeline.Keyframe(0,    new JsonPrimitive("#0b0f26")))  // night
								.addKeyframe(new Timeline.Keyframe(6000, new JsonPrimitive("#ffbf80")))  // sunrise
								.addKeyframe(new Timeline.Keyframe(12000,new JsonPrimitive("#6eb1ff")))  // noon
								.addKeyframe(new Timeline.Keyframe(18000,new JsonPrimitive("#141a33")))  // dusk
				)
				.track("minecraft:visual/fog_color",
						new Timeline.Track()
								.ease("linear")
								.addKeyframe(new Timeline.Keyframe(0,    new JsonPrimitive("#151a26")))
								.addKeyframe(new Timeline.Keyframe(6000, new JsonPrimitive("#d8c7ff")))
								.addKeyframe(new Timeline.Keyframe(12000,new JsonPrimitive("#c0d8ff")))
								.addKeyframe(new Timeline.Keyframe(18000,new JsonPrimitive("#202535")))
				);
	}

	public static void dumpSkyTimelineJson() {
		Timeline timeline = buildSkyIslandsSkyTimeline();
		System.out.println("Timeline JSON (my_mod:sky_islands_sky_cycle):");
		System.out.println(JsonBytes.encodeToPrettyString(Timeline.CODEC, timeline));
	}

	/* ----------------------------------------------------------
	 * 2) Biome: my_mod:sky_islands_biome
	 * ---------------------------------------------------------- */

	public static Biome buildSkyIslandsBiome() {
		var attribMap = new HashMap<String, AttributeValue>();
		attribMap.put("minecraft:visual/sky_color", AttributeValue.ofString("#6eb1ff"));
		attribMap.put("minecraft:visual/fog_color", AttributeValue.ofString("#c0d8ff"));
		attribMap.put("minecraft:visual/water_fog_start_distance", AttributeValue.ofFloat(0.0f));
		attribMap.put("minecraft:visual/water_fog_end_distance", AttributeValue.ofFloat(96.0f));
		attribMap.put("my_mod:gameplay/low_gravity_factor", AttributeValue.ofFloat(0.7f));

		return Biome.biome()
				.hasPrecipitation(true)
				.temperature(0.8F)
				.downfall(0.4F)
				.effects(new Biome.Effects().waterColor(4159204))
				.attributes(attribMap)
				.spawnSettings(new Biome.SpawnSettings().setCreatureSpawnProbability(0.07F))
				.generation(new Biome.Generation()
						.addFeature(7, "my_mod:sky_islands_trees")
						.addFeature(8, "my_mod:sky_islands_copper_ore")
						.addFeature(9, "my_mod:sky_islands_flowers"));
	}

	public static int hex(String hex) {
		if (hex == null) return 0;
		hex = hex.replace("#", "");
		if (hex.length() != 6) {
			throw new IllegalArgumentException("Invalid hex color: " + hex);
		}
		return Integer.parseInt(hex, 16);
	}

	public static void dumpSkyIslandsBiomeJson() {
		Biome biome = buildSkyIslandsBiome();
		System.out.println("Biome JSON (my_mod:sky_islands_biome):");
		System.out.println(JsonBytes.encodeToPrettyString(Biome.CODEC, biome));
	}

	/* ----------------------------------------------------------
	 * 3) Dimension Type: my_mod:sky_islands_type
	 * ---------------------------------------------------------- */

	public static DimensionType buildSkyIslandsDimensionType() {
		return DimensionType.dimensionType()
				.hasSkylight(true)
				.hasCeiling(false)
				.coordinateScale(1.0)
				.minY(-64)
				.height(384)
				.logicalHeight(384)
				.infiniburn(BlockTags.INFINIBURN_OVERWORLD)
				.ambientLight(0.1f)
				.monsterSpawnLightUniform(0, 7)
				.monsterSpawnBlockLightLimit(0)
				.attribute("minecraft:visual/sky_color", "#6eb1ff")
				.attribute("minecraft:visual/fog_color", "#c0d8ff")
				.attribute("minecraft:visual/sky_light_color", "#ffffff")
				.attribute("minecraft:gameplay/sky_light_level", 15.0f)
				.attribute("my_mod:visual/cloud_density", 0.4f)
				.timelinesTag("my_mod:sky_islands_sky_cycle")
				.skybox(DimensionType.Skybox.OVERWORLD)
				.cardinalLight(DimensionType.CardinalLightType.DEFAULT)
				.hasFixedTime(false);
	}

	public static void dumpSkyIslandsDimensionTypeJson() {
		DimensionType type = buildSkyIslandsDimensionType();
		System.out.println("Dimension Type JSON (my_mod:sky_islands_type):");
		System.out.println(JsonBytes.encodeToPrettyString(DimensionType.CODEC, type));
	}

	/* ----------------------------------------------------------
	 * 4) Noise Settings: my_mod:sky_islands
	 * ---------------------------------------------------------- */

	public static NoiseSettings buildSkyIslandsNoiseSettings() {
		return NoiseSettings.settings()
				.seaLevel(32)
				.legacyRandomSource(false)
				.defaultBlockId("minecraft:stone")
				.defaultFluidId("minecraft:water")
				.noiseSimple(-64, 384, 2, 1);
	}

	public static void dumpSkyIslandsNoiseSettingsJson() {
		NoiseSettings settings = buildSkyIslandsNoiseSettings();
		System.out.println("Noise Settings JSON (my_mod:sky_islands):");
		System.out.println(JsonBytes.encodeToPrettyString(NoiseSettings.CODEC, settings));
	}

	/* ----------------------------------------------------------
	 * 5) Dimension: my_mod:sky_islands
	 * ---------------------------------------------------------- */

	public static Dimension buildSkyIslandsDimension() {
		return Dimension.dimension()
				.type("my_mod:sky_islands_type")
				.noiseGenerator("my_mod:sky_islands")         // settings id
				.fixedBiome("my_mod:sky_islands_biome");      // fixed biome source
	}

	public static void dumpSkyIslandsDimensionJson() {
		Dimension dim = buildSkyIslandsDimension();
		System.out.println("Dimension JSON (my_mod:sky_islands):");
		System.out.println(JsonBytes.encodeToPrettyString(Dimension.CODEC, dim));
	}

	/* ----------------------------------------------------------
	 * 6) Configured Feature: my_mod:sky_islands_trees
	 * ---------------------------------------------------------- */

	public static ConfiguredFeature buildSkyIslandsTreesConfigured() {
		return ConfiguredFeature.tree(SimpleTreeConfig
				.tree("minecraft:oak_log", "minecraft:oak_leaves")
				.ignoreVines(true)
		);
	}

	public static void dumpSkyIslandsTreesConfiguredJson() {
		ConfiguredFeature cfg = buildSkyIslandsTreesConfigured();
		System.out.println("Configured Feature JSON (my_mod:sky_islands_trees):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, cfg));
	}

	/* ----------------------------------------------------------
	 * 7) Placed Feature: my_mod:sky_islands_trees
	 * ---------------------------------------------------------- */

	public static PlacedFeature buildSkyIslandsTreesPlaced() {
		return PlacedFeature.placed("my_mod:sky_islands_trees")
				.count(3)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter();
	}

	public static void dumpSkyIslandsTreesPlacedJson() {
		PlacedFeature placed = buildSkyIslandsTreesPlaced();
		System.out.println("Placed Feature JSON (my_mod:sky_islands_trees):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, placed));
	}

	/* ----------------------------------------------------------
	 * 7b) Feature examples: ore and random patch
	 * ---------------------------------------------------------- */

	public static ConfiguredFeature buildSkyIslandsCopperOreConfigured() {
		return ConfiguredFeature.ore(OreConfig.ore("#minecraft:stone_ore_replaceables", "minecraft:copper_ore", 16));
	}

	public static PlacedFeature buildSkyIslandsCopperOrePlaced() {
		return PlacedFeature.placed("my_mod:sky_islands_copper_ore")
				.count(IntProvider.uniform(8, 16))
				.inSquare()
				.uniformHeight(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(96))
				.blockPredicateFilter(PlacedFeature.BlockPredicate.matchingBlockTag("minecraft:stone_ore_replaceables"))
				.biomeFilter();
	}

	public static ConfiguredFeature buildSkyIslandsFlowerConfigured() {
		return ConfiguredFeature.simpleBlock(SimpleBlockConfig.simpleBlock("minecraft:dandelion"));
	}

	public static PlacedFeature buildSkyIslandsFlowerPlaced() {
		return PlacedFeature.placed("my_mod:sky_islands_flower")
				.rarityFilter(3)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.randomOffset(IntProvider.uniform(0, 2), IntProvider.constant(1))
				.environmentScan("down", PlacedFeature.BlockPredicate.wouldSurvive("minecraft:dandelion"), 4)
				.biomeFilter();
	}

	public static ConfiguredFeature buildSkyIslandsFlowerPatchConfigured() {
		return ConfiguredFeature.randomPatch(RandomPatchConfig.randomPatch("my_mod:sky_islands_flower")
				.tries(48)
				.xzSpread(6)
				.ySpread(2));
	}

	public static void dumpSkyIslandsFeatureExamplesJson() {
		System.out.println("Configured Feature JSON (my_mod:sky_islands_copper_ore):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsCopperOreConfigured()));
		System.out.println("Placed Feature JSON (my_mod:sky_islands_copper_ore):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildSkyIslandsCopperOrePlaced()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_flower):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsFlowerConfigured()));
		System.out.println("Placed Feature JSON (my_mod:sky_islands_flower):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildSkyIslandsFlowerPlaced()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_flowers):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsFlowerPatchConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_clay_disk):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsClayDiskConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_water_spring):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsWaterSpringConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_random_tree):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsRandomTreeConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_leaf_pile):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsLeafPileConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_lake):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsLakeConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_huge_fungus):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsHugeFungusConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_iceberg):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsIcebergConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_replace_single_block):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsReplaceSingleBlockConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_blackstone_blobs):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsBlackstoneBlobsConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_huge_red_mushroom):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsHugeRedMushroomConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_nether_vegetation):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsNetherVegetationConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_delta):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsDeltaConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_basalt_columns):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsBasaltColumnsConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_fill_layer):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsFillLayerConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_sea_pickle):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsSeaPickleConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_bamboo):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsBambooConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_underwater_magma):");
		System.out.println(JsonBytes.encodeToPrettyString(ConfiguredFeature.CODEC, buildSkyIslandsUnderwaterMagmaConfigured()));
	}

	public static ConfiguredFeature buildSkyIslandsClayDiskConfigured() {
		return ConfiguredFeature.disk(DiskConfig.disk("minecraft:clay", "minecraft:dirt", 3)
				.radius(IntProvider.uniform(2, 4))
				.halfHeight(1));
	}

	public static ConfiguredFeature buildSkyIslandsWaterSpringConfigured() {
		return ConfiguredFeature.spring(SpringConfig.spring("minecraft:water")
				.requiresBlockBelow(true)
				.rockCount(4)
				.holeCount(1)
				.validBlocks(List.of("minecraft:stone", "minecraft:deepslate")));
	}

	public static ConfiguredFeature buildSkyIslandsRandomTreeConfigured() {
		return ConfiguredFeature.randomSelector(new RandomSelectorConfig()
				.feature("my_mod:sky_islands_trees", 0.35F)
				.feature("minecraft:fancy_oak", 0.15F)
				.defaultFeature("minecraft:oak"));
	}

	public static ConfiguredFeature buildSkyIslandsLeafPileConfigured() {
		return ConfiguredFeature.blockPile(BlockPileConfig.blockPile("minecraft:oak_leaves"));
	}

	public static ConfiguredFeature buildSkyIslandsLakeConfigured() {
		return ConfiguredFeature.lake(LakeConfig.lake("minecraft:water", "minecraft:stone"));
	}

	public static ConfiguredFeature buildSkyIslandsHugeFungusConfigured() {
		return ConfiguredFeature.hugeFungus(new HugeFungusConfig()
				.validBaseBlock(BlockState.blockState("minecraft:warped_nylium"))
				.stemState(BlockState.blockState("minecraft:warped_stem"))
				.hatState(BlockState.blockState("minecraft:warped_wart_block"))
				.decorState(BlockState.blockState("minecraft:shroomlight"))
				.planted(false));
	}

	public static ConfiguredFeature buildSkyIslandsIcebergConfigured() {
		return ConfiguredFeature.iceberg(BlockStateConfig.state("minecraft:packed_ice"));
	}

	public static ConfiguredFeature buildSkyIslandsReplaceSingleBlockConfigured() {
		return ConfiguredFeature.replaceSingleBlock(ReplaceBlockConfig
				.replace("minecraft:stone", "minecraft:mossy_cobblestone")
				.target(RuleTest.tag("minecraft:dirt"), BlockState.blockState("minecraft:rooted_dirt")));
	}

	public static ConfiguredFeature buildSkyIslandsBlackstoneBlobsConfigured() {
		return ConfiguredFeature.replaceBlobs(ReplaceSphereConfig
				.replaceSphere("minecraft:netherrack", "minecraft:blackstone", 3)
				.radius(IntProvider.uniform(2, 5)));
	}

	public static ConfiguredFeature buildSkyIslandsHugeRedMushroomConfigured() {
		return ConfiguredFeature.hugeRedMushroom(HugeMushroomConfig
				.hugeMushroom("minecraft:red_mushroom_block", "minecraft:mushroom_stem")
				.foliageRadius(2)
				.canPlaceOn(PlacedFeature.BlockPredicate.matchingBlocks("minecraft:mycelium", "minecraft:podzol")));
	}

	public static ConfiguredFeature buildSkyIslandsNetherVegetationConfigured() {
		return ConfiguredFeature.netherForestVegetation(NetherForestVegetationConfig
				.vegetation("minecraft:warped_roots")
				.spreadWidth(8)
				.spreadHeight(4));
	}

	public static ConfiguredFeature buildSkyIslandsDeltaConfigured() {
		return ConfiguredFeature.deltaFeature(DeltaConfig
				.delta("minecraft:lava", "minecraft:magma_block")
				.size(IntProvider.uniform(3, 5))
				.rimSize(IntProvider.uniform(1, 2)));
	}

	public static ConfiguredFeature buildSkyIslandsBasaltColumnsConfigured() {
		return ConfiguredFeature.basaltColumns(ColumnConfig
				.column(1, 5)
				.reach(IntProvider.uniform(1, 2))
				.height(IntProvider.uniform(4, 8)));
	}

	public static ConfiguredFeature buildSkyIslandsFillLayerConfigured() {
		return ConfiguredFeature.fillLayer(LayerConfig.layer(2, "minecraft:deepslate"));
	}

	public static ConfiguredFeature buildSkyIslandsSeaPickleConfigured() {
		return ConfiguredFeature.seaPickle(new CountConfig().count(IntProvider.uniform(1, 4)));
	}

	public static ConfiguredFeature buildSkyIslandsBambooConfigured() {
		return ConfiguredFeature.bamboo(ProbabilityConfig.of(0.35F));
	}

	public static ConfiguredFeature buildSkyIslandsUnderwaterMagmaConfigured() {
		return ConfiguredFeature.underwaterMagma(UnderwaterMagmaConfig.underwaterMagma(5, 2, 0.35F));
	}

	/* ----------------------------------------------------------
	 * 8) Structure: my_mod:sky_ruin
	 * ---------------------------------------------------------- */

	public static Structure buildSkyRuinStructure() {
		return Structure.jigsaw("my_mod:sky_ruin/start_pool")
				.biomesId("my_mod:sky_islands_biome")
				.step("surface_structures")
				.size(1)
				.maxDistanceFromCenter(80)
				.startHeightInt(0)
				.useExpansionHack(false);
	}

	public static void dumpSkyRuinStructureJson() {
		Structure structure = buildSkyRuinStructure();
		System.out.println("Structure JSON (my_mod:sky_ruin):");
		System.out.println(JsonBytes.encodeToPrettyString(Structure.CODEC, structure));
	}

	/* ----------------------------------------------------------
	 * 9) Structure Set: my_mod:sky_ruin
	 * ---------------------------------------------------------- */

	public static StructureSet buildSkyRuinStructureSet() {
		return StructureSet
				.randomSpread("my_mod:sky_ruin", 1, 1234567, 40, 8);
	}

	public static void dumpSkyRuinStructureSetJson() {
		StructureSet set = buildSkyRuinStructureSet();
		System.out.println("Structure Set JSON (my_mod:sky_ruin):");
		System.out.println(JsonBytes.encodeToPrettyString(StructureSet.CODEC, set));
	}

	/* ----------------------------------------------------------
	 * 10) Main – dump everything
	 * ---------------------------------------------------------- */

	public static void main(String[] args) {
		dumpSkyTimelineJson();
		dumpSkyIslandsBiomeJson();
		dumpSkyIslandsDimensionTypeJson();
		dumpSkyIslandsNoiseSettingsJson();
		dumpSkyIslandsDimensionJson();
		dumpSkyIslandsTreesConfiguredJson();
		dumpSkyIslandsTreesPlacedJson();
		dumpSkyIslandsFeatureExamplesJson();
		dumpSkyRuinStructureJson();
		dumpSkyRuinStructureSetJson();
	}
}
