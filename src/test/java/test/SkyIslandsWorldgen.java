package test;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EasingType;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.assets.timeline.Timeline;
import net.vampirestudios.packwright.data.worldgen.*;
import net.vampirestudios.packwright.data.worldgen.material.MaterialCondition;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;
import net.vampirestudios.packwright.data.worldgen.biome.Biome;
import net.vampirestudios.packwright.data.worldgen.dimension.Dimension;
import net.vampirestudios.packwright.data.worldgen.dimension.DimensionType;
import net.vampirestudios.packwright.data.worldgen.feature.Feature;
import net.vampirestudios.packwright.data.worldgen.feature.Features;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.packwright.data.worldgen.structure.Structure;
import net.vampirestudios.packwright.data.worldgen.structure.StructureSet;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;
import static net.vampirestudios.packwright.util.ResourceHelpers.vanillaId;

public class SkyIslandsWorldgen {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	/* ----------------------------------------------------------
	 * 1) Timeline: my_mod:sky_islands_sky_cycle
	 * ---------------------------------------------------------- */

	public static Timeline buildSkyIslandsSkyTimeline() {
		return new Timeline()
				.setPeriodTicks(24000)
				.addTrack(EnvironmentAttributes.SKY_COLOR, t -> t
						.ease(EasingType.LINEAR)
						.addKeyframe(0,     "#0b0f26")  // night
						.addKeyframe(6000,  "#ffbf80")  // sunrise
						.addKeyframe(12000, "#6eb1ff")  // noon
						.addKeyframe(18000, "#141a33")  // dusk
				)
				.addTrack(EnvironmentAttributes.FOG_COLOR, t -> t
						.ease(EasingType.LINEAR)
						.addKeyframe(0,     "#151a26")
						.addKeyframe(6000,  "#d8c7ff")
						.addKeyframe(12000, "#c0d8ff")
						.addKeyframe(18000, "#202535")
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
		// note: attribute keys must exist in the environment_attribute registry —
		// custom namespaced attributes only work if a mod registers them in code
		var attribMap = new HashMap<Identifier, AttributeValue>();
		attribMap.put(vanillaId("visual/sky_color"), AttributeValue.ofString("#6eb1ff"));
		attribMap.put(vanillaId("visual/fog_color"), AttributeValue.ofString("#c0d8ff"));
		attribMap.put(vanillaId("visual/water_fog_start_distance"), AttributeValue.ofFloat(0.0f));
		attribMap.put(vanillaId("visual/water_fog_end_distance"), AttributeValue.ofFloat(96.0f));

		return Biome.biome()
				.hasPrecipitation(true)
				.temperature(0.8F)
				.downfall(0.4F)
				.effects(new Biome.Effects().waterColor(4159204))
				.attributes(attribMap)
				.spawnSettings(new Biome.SpawnSettings().setCreatureSpawnProbability(0.07F))
				.generation(new Biome.Generation()
						.addFeature(7, myModId("sky_islands_trees"))
						.addFeature(8, myModId("sky_islands_copper_ore"))
						.addFeature(9, myModId("sky_islands_flowers")));
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
				.attribute(vanillaId("visual/sky_color"), "#6eb1ff")
				.attribute(vanillaId("visual/fog_color"), "#c0d8ff")
				.attribute(vanillaId("visual/sky_light_color"), "#ffffff")
				.attribute(vanillaId("gameplay/sky_light_level"), 15.0f)
				.timeline(myModId("sky_islands_sky_cycle"))
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

	/**
	 * The surface pipeline ({@code surface_rule} in noise settings): grass on exposed
	 * floors, dirt just beneath, stone everywhere else.
	 */
	public static MaterialRule buildSkyIslandsSurfaceRule() {
		return MaterialRule.sequence(
				MaterialRule.condition(
						MaterialCondition.stoneDepth(0, false, "floor"),
						MaterialRule.condition(
								MaterialCondition.yAbove(VerticalAnchor.absolute(33), 1, false),
								MaterialRule.block(vanillaId("grass_block")))),
				MaterialRule.condition(
						MaterialCondition.stoneDepth(0, true, "floor"),
						MaterialRule.block(vanillaId("dirt"))),
				MaterialRule.block(VanillaIds.STONE)
		);
	}

	public static NoiseSettings buildSkyIslandsNoiseSettings() {
		return NoiseSettings.settings()
				.seaLevel(32)
				.legacyRandomSource(false)
				.defaultBlockId(VanillaIds.STONE)
				.defaultFluidId(VanillaIds.WATER)
				.noiseSimple(-64, 384, 2, 1)
				.aquifersEnabled(false)
				.oreVeinsEnabled(false)
				.disableMobGeneration(false)
				// solid below y=32, air above y=96
				.simpleNoiseRouterGradient(32, 96)
				.surfaceRule(buildSkyIslandsSurfaceRule());
	}

	public static void dumpSkyIslandsNoiseSettingsJson() {
		NoiseSettings settings = buildSkyIslandsNoiseSettings();
		System.out.println("Noise Settings JSON (my_mod:sky_islands):");
		System.out.println(JsonBytes.encodeToPrettyString(NoiseSettings.CODEC, settings));
		System.out.println("Material Rule JSON (my_mod:sky_islands_surface):");
		System.out.println(JsonBytes.encodeToPrettyString(MaterialRule.CODEC, buildSkyIslandsSurfaceRule()));
	}

	/* ----------------------------------------------------------
	 * 5) Dimension: my_mod:sky_islands
	 * ---------------------------------------------------------- */

	public static Dimension buildSkyIslandsDimension() {
		return Dimension.dimension()
				.type(myModId("sky_islands_type"))
				.noiseGenerator(myModId("sky_islands"))         // settings id
				.fixedBiome(myModId("sky_islands_biome"));      // fixed biome source
	}

	public static void dumpSkyIslandsDimensionJson() {
		Dimension dim = buildSkyIslandsDimension();
		System.out.println("Dimension JSON (my_mod:sky_islands):");
		System.out.println(JsonBytes.encodeToPrettyString(Dimension.CODEC, dim));
	}

	/* ----------------------------------------------------------
	 * 6) Feature: my_mod:sky_islands_trees
	 * ---------------------------------------------------------- */

	public static Feature buildSkyIslandsTreesFeature() {
		return Features.simpleTree(vanillaId("oak_log"), vanillaId("oak_leaves"))
				.ignoreVines(true)
				.build();
	}

	public static void dumpSkyIslandsTreesFeatureJson() {
		Feature cfg = buildSkyIslandsTreesFeature();
		System.out.println("Feature JSON (my_mod:sky_islands_trees):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, cfg));
	}

	/* ----------------------------------------------------------
	 * 7) Placed Feature: my_mod:sky_islands_trees
	 * ---------------------------------------------------------- */

	public static PlacedFeature buildSkyIslandsTreesPlaced() {
		return PlacedFeature.placed(buildSkyIslandsTreesFeature())
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

	public static Feature buildSkyIslandsCopperOreFeature() {
		return Features.ore(vanillaId("stone_ore_replaceables"), vanillaId("copper_ore"), 16)
				.build();
	}

	public static PlacedFeature buildSkyIslandsCopperOrePlaced() {
		return PlacedFeature.placed(buildSkyIslandsCopperOreFeature())
				.count(IntProvider.uniform(8, 16))
				.inSquare()
				.uniformHeight(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(96))
				.blockPredicateFilter(PlacedFeature.BlockPredicate.matchingBlockTag("minecraft:stone_ore_replaceables"))
				.biomeFilter();
	}

	public static Feature buildSkyIslandsFlowerFeature() {
		return Features.simpleBlock(vanillaId("dandelion")).build();
	}

	public static PlacedFeature buildSkyIslandsFlowerPlaced() {
		return PlacedFeature.placed(buildSkyIslandsFlowerFeature())
				.rarityFilter(3)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.randomOffset(IntProvider.uniform(0, 2), IntProvider.constant(1))
				.environmentScan("down", PlacedFeature.BlockPredicate.wouldSurvive("minecraft:dandelion"), 4)
				.biomeFilter();
	}

	/**
	 * random_patch was removed in 26.1, so flower "patches" are just single flowers
	 * scattered by count placement
	 */
	public static Feature buildSkyIslandsFlowerPatchFeature() {
		return Features.simpleBlock(vanillaId("dandelion")).build();
	}

	public static void dumpSkyIslandsFeatureExamplesJson() {
		System.out.println("Feature JSON (my_mod:sky_islands_copper_ore):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsCopperOreFeature()));
		System.out.println("Placed Feature JSON (my_mod:sky_islands_copper_ore):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildSkyIslandsCopperOrePlaced()));
		System.out.println("Feature JSON (my_mod:sky_islands_flower):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsFlowerFeature()));
		System.out.println("Placed Feature JSON (my_mod:sky_islands_flower):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildSkyIslandsFlowerPlaced()));
		System.out.println("Feature JSON (my_mod:sky_islands_flowers):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsFlowerPatchFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_clay_disk):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsClayDiskFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_water_spring):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsWaterSpringFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_random_tree):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsRandomTreeFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_leaf_pile):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsLeafPileFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_lake):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsLakeFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_huge_fungus):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsHugeFungusFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_iceberg):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsIcebergFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_replace_single_block):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsReplaceSingleBlockFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_blackstone_blobs):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsBlackstoneBlobsFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_huge_red_mushroom):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsHugeRedMushroomFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_nether_vegetation):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsNetherVegetationFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_delta):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsDeltaFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_basalt_columns):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsBasaltColumnsFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_fill_layer):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsFillLayerFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_sea_pickle):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsSeaPickleFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_bamboo):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsBambooFeature()));
		System.out.println("Feature JSON (my_mod:sky_islands_underwater_magma):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildSkyIslandsUnderwaterMagmaFeature()));
	}

	public static Feature buildSkyIslandsClayDiskFeature() {
		return Features.disk(VanillaIds.CLAY, VanillaIds.DIRT, 3)
				.radius(IntProvider.uniform(2, 4))
				.halfHeight(1)
				.build();
	}

	public static Feature buildSkyIslandsWaterSpringFeature() {
		return Features.spring(VanillaIds.WATER)
				.requiresBlockBelow(true)
				.rockCount(4)
				.holeCount(1)
				.validBlocks(List.of(VanillaIds.STONE, VanillaIds.DEEPSLATE))
				.build();
	}

	public static Feature buildSkyIslandsRandomTreeFeature() {
		return Features.randomSelector()
				.feature(buildSkyIslandsTreesPlaced(), 0.35F)
				.feature(PlacedFeature.placed(Features.simpleTree(vanillaId("birch_log"), vanillaId("birch_leaves"))
						.ignoreVines(true)
						.build()).count(1).inSquare().heightmap("MOTION_BLOCKING").biomeFilter(), 0.15F)
				.defaultFeature(PlacedFeature.placed(Features.simpleTree(vanillaId("oak_log"), vanillaId("oak_leaves"))
						.ignoreVines(true)
						.build()).count(1).inSquare().heightmap("MOTION_BLOCKING").biomeFilter())
				.build();
	}

	public static Feature buildSkyIslandsLeafPileFeature() {
		return Features.blockPile(vanillaId("oak_leaves")).build();
	}

	public static Feature buildSkyIslandsLakeFeature() {
		return Features.lake(VanillaIds.WATER, VanillaIds.STONE).build();
	}

	public static Feature buildSkyIslandsHugeFungusFeature() {
		return Features.hugeFungus()
				.validBaseBlock(VanillaIds.WARPED_NYLIUM)
				.stemState(VanillaIds.WARPED_STEM)
				.hatState(vanillaId("warped_wart_block"))
				.decorState(VanillaIds.SHROOMLIGHT)
				.planted(false)
				.build();
	}

	public static Feature buildSkyIslandsIcebergFeature() {
		return Features.iceberg(vanillaId("packed_ice")).build();
	}

	public static Feature buildSkyIslandsReplaceSingleBlockFeature() {
		return Features.replaceSingleBlock()
				.targetBlock(VanillaIds.STONE, VanillaIds.MOSSY_COBBLESTONE)
				.targetTag(VanillaIds.DIRT, VanillaIds.ROOTED_DIRT)
				.build();
	}

	/** replace_blobs no longer exists in the game; a block-targeted ore does the same job */
	public static Feature buildSkyIslandsBlackstoneBlobsFeature() {
		return Features.oreInBlock(vanillaId("netherrack"), vanillaId("blackstone"), 12)
				.build();
	}

	public static Feature buildSkyIslandsHugeRedMushroomFeature() {
		return Features.hugeRedMushroom(vanillaId("red_mushroom_block"), vanillaId("mushroom_stem"))
				.foliageRadius(2)
				.canPlaceOn(PlacedFeature.BlockPredicate.matchingBlocks(VanillaIds.MYCELIUM, VanillaIds.PODZOL))
				.build();
	}

	public static Feature buildSkyIslandsNetherVegetationFeature() {
		return Features.netherForestVegetation(vanillaId("warped_roots"))
				.spreadWidth(8)
				.spreadHeight(4)
				.build();
	}

	public static Feature buildSkyIslandsDeltaFeature() {
		return Features.deltaFeature(vanillaId("lava"), vanillaId("magma_block"))
				.size(IntProvider.uniform(3, 5))
				.rimSize(IntProvider.uniform(1, 2))
				.build();
	}

	public static Feature buildSkyIslandsBasaltColumnsFeature() {
		return Features.feature("minecraft:basalt_columns")
				.property("reach", IntProvider.CODEC, IntProvider.uniform(1, 2))
				.property("height", IntProvider.CODEC, IntProvider.uniform(4, 8))
				.build();
	}

	public static Feature buildSkyIslandsFillLayerFeature() {
		return Features.fillLayer(2, vanillaId("deepslate")).build();
	}

	public static Feature buildSkyIslandsSeaPickleFeature() {
		return Features.seaPickle(IntProvider.uniform(1, 4)).build();
	}

	public static Feature buildSkyIslandsBambooFeature() {
		return Features.bamboo(0.35F).build();
	}

	public static Feature buildSkyIslandsUnderwaterMagmaFeature() {
		return Features.underwaterMagma(5, 2, 0.35F).build();
	}

	/* ----------------------------------------------------------
	 * 8) Structure: my_mod:sky_ruin
	 * ---------------------------------------------------------- */

	public static Structure buildSkyRuinStructure() {
		return Structure.jigsaw("mymod:sky_ruin/start_pool")
				.biomesId(myModId("sky_islands_biome"))
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
				.randomSpread(myModId("sky_ruin"), 1, 1234567, 40, 8);
	}

	/** an empty start pool so the jigsaw structure's reference resolves */
	public static TemplatePool buildSkyRuinStartPool() {
		return TemplatePool.pool().fallback(vanillaId("empty"));
	}

	public static void dumpSkyRuinStructureSetJson() {
		StructureSet set = buildSkyRuinStructureSet();
		System.out.println("Structure Set JSON (my_mod:sky_ruin):");
		System.out.println(JsonBytes.encodeToPrettyString(StructureSet.CODEC, set));
	}

	/* ----------------------------------------------------------
	 * 10) Register everything into a runtime pack
	 * ---------------------------------------------------------- */

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addTimeline(myModId("sky_islands_sky_cycle"), buildSkyIslandsSkyTimeline());
		pack.addNoiseSettings(myModId("sky_islands"), buildSkyIslandsNoiseSettings());
		pack.addBiome(myModId("sky_islands_biome"), buildSkyIslandsBiome());
		pack.addDimensionType(myModId("sky_islands_type"), buildSkyIslandsDimensionType());
		pack.addFeature(myModId("sky_islands_trees"), buildSkyIslandsTreesFeature());
		pack.addPlacedFeature(myModId("sky_islands_trees"), buildSkyIslandsTreesPlaced());
		pack.addFeature(myModId("sky_islands_copper_ore"), buildSkyIslandsCopperOreFeature());
		pack.addPlacedFeature(myModId("sky_islands_copper_ore"), buildSkyIslandsCopperOrePlaced());
		pack.addFeature(myModId("sky_islands_flower"), buildSkyIslandsFlowerFeature());
		pack.addPlacedFeature(myModId("sky_islands_flower"), buildSkyIslandsFlowerPlaced());
		pack.addFeature(myModId("sky_islands_flowers"), buildSkyIslandsFlowerPatchFeature());
		pack.addPlacedFeature(myModId("sky_islands_flowers"), PlacedFeature.placed(buildSkyIslandsFlowerPatchFeature())
				.rarityFilter(2)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter());
		pack.addStructure(myModId("sky_ruin"), buildSkyRuinStructure());
		pack.addStructureSet(myModId("sky_ruin"), buildSkyRuinStructureSet());
		pack.addTemplatePool(myModId("sky_ruin/start_pool"), buildSkyRuinStartPool());
		pack.addDimension(myModId("sky_islands"), buildSkyIslandsDimension());
	}

	/* ----------------------------------------------------------
	 * 11) Main – dump everything
	 * ---------------------------------------------------------- */

	public static void main() {
		dumpSkyTimelineJson();
		dumpSkyIslandsBiomeJson();
		dumpSkyIslandsDimensionTypeJson();
		dumpSkyIslandsNoiseSettingsJson();
		dumpSkyIslandsDimensionJson();
		dumpSkyIslandsTreesFeatureJson();
		dumpSkyIslandsTreesPlacedJson();
		dumpSkyIslandsFeatureExamplesJson();
		dumpSkyRuinStructureJson();
		dumpSkyRuinStructureSetJson();

		// dump the whole dimension as its own datapack
		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:sky_islands");
		pack.addDataPackMcmeta("Sky Islands - floating isles dimension, generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/sky_islands"));
	}
}
