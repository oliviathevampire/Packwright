package test;

import com.google.gson.JsonPrimitive;
import net.vampirestudios.arrp.json.JsonBytes;
import net.vampirestudios.arrp.json.timeline.JTimeline;
import net.vampirestudios.arrp.json.worldgen.BlockState;
import net.vampirestudios.arrp.json.worldgen.IntProvider;
import net.vampirestudios.arrp.json.worldgen.JAttributeValue;
import net.vampirestudios.arrp.json.worldgen.VerticalAnchor;
import net.vampirestudios.arrp.json.worldgen.biome.JBiome;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimension;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimensionType;
import net.vampirestudios.arrp.json.worldgen.feature.JConfiguredFeature;
import net.vampirestudios.arrp.json.worldgen.feature.JPlacedFeature;
import net.vampirestudios.arrp.json.worldgen.feature.config.*;
import net.vampirestudios.arrp.json.worldgen.noise.JNoiseSettings;
import net.vampirestudios.arrp.json.worldgen.structure.JStructure;
import net.vampirestudios.arrp.json.worldgen.structure.JStructureSet;
import net.minecraft.tags.BlockTags;

import java.util.HashMap;
import java.util.List;

public class SkyIslandsWorldgen {

	/* ----------------------------------------------------------
	 * 1) Timeline: my_mod:sky_islands_sky_cycle
	 * ---------------------------------------------------------- */

	public static JTimeline buildSkyIslandsSkyTimeline() {
		return new JTimeline()
				.period(24000)
				.track("minecraft:visual/sky_color",
						new JTimeline.Track()
								.ease("linear")
								.addKeyframe(new JTimeline.Keyframe(0,    new JsonPrimitive("#0b0f26")))  // night
								.addKeyframe(new JTimeline.Keyframe(6000, new JsonPrimitive("#ffbf80")))  // sunrise
								.addKeyframe(new JTimeline.Keyframe(12000,new JsonPrimitive("#6eb1ff")))  // noon
								.addKeyframe(new JTimeline.Keyframe(18000,new JsonPrimitive("#141a33")))  // dusk
				)
				.track("minecraft:visual/fog_color",
						new JTimeline.Track()
								.ease("linear")
								.addKeyframe(new JTimeline.Keyframe(0,    new JsonPrimitive("#151a26")))
								.addKeyframe(new JTimeline.Keyframe(6000, new JsonPrimitive("#d8c7ff")))
								.addKeyframe(new JTimeline.Keyframe(12000,new JsonPrimitive("#c0d8ff")))
								.addKeyframe(new JTimeline.Keyframe(18000,new JsonPrimitive("#202535")))
				);
	}

	public static void dumpSkyTimelineJson() {
		JTimeline timeline = buildSkyIslandsSkyTimeline();
		System.out.println("Timeline JSON (my_mod:sky_islands_sky_cycle):");
		System.out.println(JsonBytes.encodeToPrettyString(JTimeline.CODEC, timeline));
	}

	/* ----------------------------------------------------------
	 * 2) Biome: my_mod:sky_islands_biome
	 * ---------------------------------------------------------- */

	public static JBiome buildSkyIslandsBiome() {
		var attribMap = new HashMap<String, JAttributeValue>();
		attribMap.put("minecraft:visual/sky_color", JAttributeValue.ofString("#6eb1ff"));
		attribMap.put("minecraft:visual/fog_color", JAttributeValue.ofString("#c0d8ff"));
		attribMap.put("minecraft:visual/water_fog_start_distance", JAttributeValue.ofFloat(0.0f));
		attribMap.put("minecraft:visual/water_fog_end_distance", JAttributeValue.ofFloat(96.0f));
		attribMap.put("my_mod:gameplay/low_gravity_factor", JAttributeValue.ofFloat(0.7f));

		return JBiome.biome()
				.hasPrecipitation(true)
				.temperature(0.8F)
				.downfall(0.4F)
				.effects(new JBiome.Effects().waterColor(4159204))
				.attributes(attribMap)
				.spawnSettings(new JBiome.SpawnSettings().setCreatureSpawnProbability(0.07F))
				.generation(new JBiome.Generation()
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
		JBiome biome = buildSkyIslandsBiome();
		System.out.println("Biome JSON (my_mod:sky_islands_biome):");
		System.out.println(JsonBytes.encodeToPrettyString(JBiome.CODEC, biome));
	}

	/* ----------------------------------------------------------
	 * 3) Dimension Type: my_mod:sky_islands_type
	 * ---------------------------------------------------------- */

	public static JDimensionType buildSkyIslandsDimensionType() {
		return JDimensionType.dimensionType()
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
				.skybox(JDimensionType.Skybox.OVERWORLD)
				.cardinalLight(JDimensionType.CardinalLightType.DEFAULT)
				.hasFixedTime(false);
	}

	public static void dumpSkyIslandsDimensionTypeJson() {
		JDimensionType type = buildSkyIslandsDimensionType();
		System.out.println("Dimension Type JSON (my_mod:sky_islands_type):");
		System.out.println(JsonBytes.encodeToPrettyString(JDimensionType.CODEC, type));
	}

	/* ----------------------------------------------------------
	 * 4) Noise Settings: my_mod:sky_islands
	 * ---------------------------------------------------------- */

	public static JNoiseSettings buildSkyIslandsNoiseSettings() {
		return JNoiseSettings.settings()
				.seaLevel(32)
				.legacyRandomSource(false)
				.defaultBlockId("minecraft:stone")
				.defaultFluidId("minecraft:water")
				.noiseSimple(-64, 384, 2, 1);
	}

	public static void dumpSkyIslandsNoiseSettingsJson() {
		JNoiseSettings settings = buildSkyIslandsNoiseSettings();
		System.out.println("Noise Settings JSON (my_mod:sky_islands):");
		System.out.println(JsonBytes.encodeToPrettyString(JNoiseSettings.CODEC, settings));
	}

	/* ----------------------------------------------------------
	 * 5) Dimension: my_mod:sky_islands
	 * ---------------------------------------------------------- */

	public static JDimension buildSkyIslandsDimension() {
		return JDimension.dimension()
				.type("my_mod:sky_islands_type")
				.noiseGenerator("my_mod:sky_islands")         // settings id
				.fixedBiome("my_mod:sky_islands_biome");      // fixed biome source
	}

	public static void dumpSkyIslandsDimensionJson() {
		JDimension dim = buildSkyIslandsDimension();
		System.out.println("Dimension JSON (my_mod:sky_islands):");
		System.out.println(JsonBytes.encodeToPrettyString(JDimension.CODEC, dim));
	}

	/* ----------------------------------------------------------
	 * 6) Configured Feature: my_mod:sky_islands_trees
	 * ---------------------------------------------------------- */

	public static JConfiguredFeature buildSkyIslandsTreesConfigured() {
		return JConfiguredFeature.tree(SimpleTreeConfig
				.tree("minecraft:oak_log", "minecraft:oak_leaves")
				.ignoreVines(true)
		);
	}

	public static void dumpSkyIslandsTreesConfiguredJson() {
		JConfiguredFeature cfg = buildSkyIslandsTreesConfigured();
		System.out.println("Configured Feature JSON (my_mod:sky_islands_trees):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, cfg));
	}

	/* ----------------------------------------------------------
	 * 7) Placed Feature: my_mod:sky_islands_trees
	 * ---------------------------------------------------------- */

	public static JPlacedFeature buildSkyIslandsTreesPlaced() {
		return JPlacedFeature.placed("my_mod:sky_islands_trees")
				.count(3)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter();
	}

	public static void dumpSkyIslandsTreesPlacedJson() {
		JPlacedFeature placed = buildSkyIslandsTreesPlaced();
		System.out.println("Placed Feature JSON (my_mod:sky_islands_trees):");
		System.out.println(JsonBytes.encodeToPrettyString(JPlacedFeature.CODEC, placed));
	}

	/* ----------------------------------------------------------
	 * 7b) Feature examples: ore and random patch
	 * ---------------------------------------------------------- */

	public static JConfiguredFeature buildSkyIslandsCopperOreConfigured() {
		return JConfiguredFeature.ore(OreConfig.ore("#minecraft:stone_ore_replaceables", "minecraft:copper_ore", 16));
	}

	public static JPlacedFeature buildSkyIslandsCopperOrePlaced() {
		return JPlacedFeature.placed("my_mod:sky_islands_copper_ore")
				.count(IntProvider.uniform(8, 16))
				.inSquare()
				.uniformHeight(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(96))
				.blockPredicateFilter(JPlacedFeature.BlockPredicate.matchingBlockTag("minecraft:stone_ore_replaceables"))
				.biomeFilter();
	}

	public static JConfiguredFeature buildSkyIslandsFlowerConfigured() {
		return JConfiguredFeature.simpleBlock(SimpleBlockConfig.simpleBlock("minecraft:dandelion"));
	}

	public static JPlacedFeature buildSkyIslandsFlowerPlaced() {
		return JPlacedFeature.placed("my_mod:sky_islands_flower")
				.rarityFilter(3)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.randomOffset(IntProvider.uniform(0, 2), IntProvider.constant(1))
				.environmentScan("down", JPlacedFeature.BlockPredicate.wouldSurvive("minecraft:dandelion"), 4)
				.biomeFilter();
	}

	public static JConfiguredFeature buildSkyIslandsFlowerPatchConfigured() {
		return JConfiguredFeature.randomPatch(RandomPatchConfig.randomPatch("my_mod:sky_islands_flower")
				.tries(48)
				.xzSpread(6)
				.ySpread(2));
	}

	public static void dumpSkyIslandsFeatureExamplesJson() {
		System.out.println("Configured Feature JSON (my_mod:sky_islands_copper_ore):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsCopperOreConfigured()));
		System.out.println("Placed Feature JSON (my_mod:sky_islands_copper_ore):");
		System.out.println(JsonBytes.encodeToPrettyString(JPlacedFeature.CODEC, buildSkyIslandsCopperOrePlaced()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_flower):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsFlowerConfigured()));
		System.out.println("Placed Feature JSON (my_mod:sky_islands_flower):");
		System.out.println(JsonBytes.encodeToPrettyString(JPlacedFeature.CODEC, buildSkyIslandsFlowerPlaced()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_flowers):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsFlowerPatchConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_clay_disk):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsClayDiskConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_water_spring):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsWaterSpringConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_random_tree):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsRandomTreeConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_leaf_pile):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsLeafPileConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_lake):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsLakeConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_huge_fungus):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsHugeFungusConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_iceberg):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsIcebergConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_replace_single_block):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsReplaceSingleBlockConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_blackstone_blobs):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsBlackstoneBlobsConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_huge_red_mushroom):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsHugeRedMushroomConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_nether_vegetation):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsNetherVegetationConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_delta):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsDeltaConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_basalt_columns):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsBasaltColumnsConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_fill_layer):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsFillLayerConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_sea_pickle):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsSeaPickleConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_bamboo):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsBambooConfigured()));
		System.out.println("Configured Feature JSON (my_mod:sky_islands_underwater_magma):");
		System.out.println(JsonBytes.encodeToPrettyString(JConfiguredFeature.CODEC, buildSkyIslandsUnderwaterMagmaConfigured()));
	}

	public static JConfiguredFeature buildSkyIslandsClayDiskConfigured() {
		return JConfiguredFeature.disk(DiskConfig.disk("minecraft:clay", "minecraft:dirt", 3)
				.radius(IntProvider.uniform(2, 4))
				.halfHeight(1));
	}

	public static JConfiguredFeature buildSkyIslandsWaterSpringConfigured() {
		return JConfiguredFeature.spring(SpringConfig.spring("minecraft:water")
				.requiresBlockBelow(true)
				.rockCount(4)
				.holeCount(1)
				.validBlocks(List.of("minecraft:stone", "minecraft:deepslate")));
	}

	public static JConfiguredFeature buildSkyIslandsRandomTreeConfigured() {
		return JConfiguredFeature.randomSelector(new RandomSelectorConfig()
				.feature("my_mod:sky_islands_trees", 0.35F)
				.feature("minecraft:fancy_oak", 0.15F)
				.defaultFeature("minecraft:oak"));
	}

	public static JConfiguredFeature buildSkyIslandsLeafPileConfigured() {
		return JConfiguredFeature.blockPile(BlockPileConfig.blockPile("minecraft:oak_leaves"));
	}

	public static JConfiguredFeature buildSkyIslandsLakeConfigured() {
		return JConfiguredFeature.lake(LakeConfig.lake("minecraft:water", "minecraft:stone"));
	}

	public static JConfiguredFeature buildSkyIslandsHugeFungusConfigured() {
		return JConfiguredFeature.hugeFungus(new HugeFungusConfig()
				.validBaseBlock(BlockState.blockState("minecraft:warped_nylium"))
				.stemState(BlockState.blockState("minecraft:warped_stem"))
				.hatState(BlockState.blockState("minecraft:warped_wart_block"))
				.decorState(BlockState.blockState("minecraft:shroomlight"))
				.planted(false));
	}

	public static JConfiguredFeature buildSkyIslandsIcebergConfigured() {
		return JConfiguredFeature.iceberg(BlockStateConfig.state("minecraft:packed_ice"));
	}

	public static JConfiguredFeature buildSkyIslandsReplaceSingleBlockConfigured() {
		return JConfiguredFeature.replaceSingleBlock(ReplaceBlockConfig
				.replace("minecraft:stone", "minecraft:mossy_cobblestone")
				.target(RuleTest.tag("minecraft:dirt"), BlockState.blockState("minecraft:rooted_dirt")));
	}

	public static JConfiguredFeature buildSkyIslandsBlackstoneBlobsConfigured() {
		return JConfiguredFeature.replaceBlobs(ReplaceSphereConfig
				.replaceSphere("minecraft:netherrack", "minecraft:blackstone", 3)
				.radius(IntProvider.uniform(2, 5)));
	}

	public static JConfiguredFeature buildSkyIslandsHugeRedMushroomConfigured() {
		return JConfiguredFeature.hugeRedMushroom(HugeMushroomConfig
				.hugeMushroom("minecraft:red_mushroom_block", "minecraft:mushroom_stem")
				.foliageRadius(2)
				.canPlaceOn(JPlacedFeature.BlockPredicate.matchingBlocks("minecraft:mycelium", "minecraft:podzol")));
	}

	public static JConfiguredFeature buildSkyIslandsNetherVegetationConfigured() {
		return JConfiguredFeature.netherForestVegetation(NetherForestVegetationConfig
				.vegetation("minecraft:warped_roots")
				.spreadWidth(8)
				.spreadHeight(4));
	}

	public static JConfiguredFeature buildSkyIslandsDeltaConfigured() {
		return JConfiguredFeature.deltaFeature(DeltaConfig
				.delta("minecraft:lava", "minecraft:magma_block")
				.size(IntProvider.uniform(3, 5))
				.rimSize(IntProvider.uniform(1, 2)));
	}

	public static JConfiguredFeature buildSkyIslandsBasaltColumnsConfigured() {
		return JConfiguredFeature.basaltColumns(ColumnConfig
				.column(1, 5)
				.reach(IntProvider.uniform(1, 2))
				.height(IntProvider.uniform(4, 8)));
	}

	public static JConfiguredFeature buildSkyIslandsFillLayerConfigured() {
		return JConfiguredFeature.fillLayer(LayerConfig.layer(2, "minecraft:deepslate"));
	}

	public static JConfiguredFeature buildSkyIslandsSeaPickleConfigured() {
		return JConfiguredFeature.seaPickle(new CountConfig().count(IntProvider.uniform(1, 4)));
	}

	public static JConfiguredFeature buildSkyIslandsBambooConfigured() {
		return JConfiguredFeature.bamboo(ProbabilityConfig.of(0.35F));
	}

	public static JConfiguredFeature buildSkyIslandsUnderwaterMagmaConfigured() {
		return JConfiguredFeature.underwaterMagma(UnderwaterMagmaConfig.underwaterMagma(5, 2, 0.35F));
	}

	/* ----------------------------------------------------------
	 * 8) Structure: my_mod:sky_ruin
	 * ---------------------------------------------------------- */

	public static JStructure buildSkyRuinStructure() {
		return JStructure.jigsaw("my_mod:sky_ruin/start_pool")
				.biomesId("my_mod:sky_islands_biome")
				.step("surface_structures")
				.size(1)
				.maxDistanceFromCenter(80)
				.startHeightInt(0)
				.useExpansionHack(false);
	}

	public static void dumpSkyRuinStructureJson() {
		JStructure structure = buildSkyRuinStructure();
		System.out.println("Structure JSON (my_mod:sky_ruin):");
		System.out.println(JsonBytes.encodeToPrettyString(JStructure.CODEC, structure));
	}

	/* ----------------------------------------------------------
	 * 9) Structure Set: my_mod:sky_ruin
	 * ---------------------------------------------------------- */

	public static JStructureSet buildSkyRuinStructureSet() {
		return JStructureSet
				.randomSpread("my_mod:sky_ruin", 1, 1234567, 40, 8);
	}

	public static void dumpSkyRuinStructureSetJson() {
		JStructureSet set = buildSkyRuinStructureSet();
		System.out.println("Structure Set JSON (my_mod:sky_ruin):");
		System.out.println(JsonBytes.encodeToPrettyString(JStructureSet.CODEC, set));
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
