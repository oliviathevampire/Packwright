package test;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EasingType;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.assets.timeline.Timeline;
import net.vampirestudios.packwright.data.worldgen.*;
import net.vampirestudios.packwright.data.worldgen.biome.Biome;
import net.vampirestudios.packwright.data.worldgen.dimension.Dimension;
import net.vampirestudios.packwright.data.worldgen.dimension.DimensionType;
import net.vampirestudios.packwright.data.worldgen.dimension.biomeSources.BiomeSource;
import net.vampirestudios.packwright.data.worldgen.feature.Feature;
import net.vampirestudios.packwright.data.worldgen.feature.Features;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.packwright.data.worldgen.material.MaterialCondition;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;
import net.vampirestudios.packwright.data.worldgen.noise.*;
import net.vampirestudios.packwright.data.worldgen.structure.Structure;
import net.vampirestudios.packwright.data.worldgen.structure.StructureSet;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.HashMap;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;
import static net.vampirestudios.packwright.util.ResourceHelpers.vanillaId;

/**
 * A complete floating-islands dimension example.
 *
 * <p>The dimension uses two vertically separated island fields, registered
 * noise parameters, a referenced material rule, identifier-backed configured
 * features, and a guaranteed origin structure that can serve as a landing
 * platform.</p>
 */
public final class SkyIslandsWorldgen {
	private static final Identifier SKY_CYCLE = id("sky_islands_sky_cycle");
	private static final Identifier DIMENSION_TYPE = id("sky_islands_type");
	private static final Identifier NOISE_SETTINGS = id("sky_islands");
	private static final Identifier DIMENSION = id("sky_islands");
	private static final Identifier BIOME = id("sky_islands_biome");

	private static final Identifier LOWER_ISLANDS_NOISE = id("sky_islands/lower_islands");
	private static final Identifier UPPER_ISLANDS_NOISE = id("sky_islands/upper_islands");
	private static final Identifier ISLAND_DETAIL_NOISE = id("sky_islands/island_detail");
	private static final Identifier SURFACE_PATCH_NOISE = id("sky_islands/surface_patch");

	private static final Identifier SURFACE_PATCH_CONDITION = id("sky_islands_surface_patch");
	private static final Identifier SURFACE_RULE = id("sky_islands_surface");

	private static final Identifier TREES = id("sky_islands_trees");
	private static final Identifier COPPER_ORE = id("sky_islands_copper_ore");
	private static final Identifier FLOWERS = id("sky_islands_flowers");

	private static final Identifier SKY_RUIN = id("sky_ruin");
	private static final Identifier SKY_RUIN_START_POOL = id("sky_ruin/start_pool");
	private static final Identifier SKY_RUIN_START_TEMPLATE = id("sky_ruin/start");

	private SkyIslandsWorldgen() {
	}

	private static Identifier id(String path) {
		return customId("mymod", path);
	}

	// -------------------------------------------------------------------------
	// Timeline
	// -------------------------------------------------------------------------

	public static Timeline buildSkyCycle() {
		return new Timeline()
				.setPeriodTicks(24000)
				.addTrack(EnvironmentAttributes.SKY_COLOR, track -> track
						.ease(EasingType.LINEAR)
						.addKeyframe(0, "#8ec5ff")
						.addKeyframe(6000, "#6eb1ff")
						.addKeyframe(12000, "#ffad7a")
						.addKeyframe(18000, "#0b0f26")
						.addKeyframe(23000, "#38496e")
				)
				.addTrack(EnvironmentAttributes.FOG_COLOR, track -> track
						.ease(EasingType.LINEAR)
						.addKeyframe(0, "#d8e7ff")
						.addKeyframe(6000, "#c0d8ff")
						.addKeyframe(12000, "#d8c7ff")
						.addKeyframe(18000, "#151a26")
						.addKeyframe(23000, "#56627a")
				)
				.addTrack(EnvironmentAttributes.FOG_END_DISTANCE, track -> track
						.ease(EasingType.LINEAR)
						.addKeyframe(0, 240.0F)
						.addKeyframe(6000, 288.0F)
						.addKeyframe(12000, 224.0F)
						.addKeyframe(18000, 160.0F)
						.addKeyframe(23000, 192.0F)
				);
	}

	// -------------------------------------------------------------------------
	// Registered noise parameters
	// -------------------------------------------------------------------------

	public static NoiseParameters buildLowerIslandsNoise() {
		return NoiseParameters.of(-7, 1.0, 0.75, 0.35);
	}

	public static NoiseParameters buildUpperIslandsNoise() {
		return NoiseParameters.of(-6, 1.0, 0.6, 0.3);
	}

	public static NoiseParameters buildIslandDetailNoise() {
		return NoiseParameters.of(-4, 1.0, 0.5, 0.25);
	}

	public static NoiseParameters buildSurfacePatchNoise() {
		return NoiseParameters.of(-3, 1.0, 0.5);
	}

	// -------------------------------------------------------------------------
	// Surface material
	// -------------------------------------------------------------------------

	public static MaterialCondition buildSurfacePatchCondition() {
		return MaterialCondition.noiseThreshold(SURFACE_PATCH_NOISE, 0.38, 1.0);
	}

	public static MaterialRule buildSurfaceRule() {
		MaterialRule exposedFloor = MaterialRule.condition(
				MaterialCondition.stoneDepth(0, false, "floor"),
				MaterialRule.sequence(
						MaterialRule.condition(
								MaterialCondition.reference(SURFACE_PATCH_CONDITION),
								MaterialRule.block(vanillaId("moss_block"))
						),
						MaterialRule.block(vanillaId("grass_block"))
				)
		);

		MaterialRule subsurface = MaterialRule.condition(
				MaterialCondition.stoneDepth(0, true, 4, "floor"),
				MaterialRule.block(VanillaIds.DIRT)
		);

		return MaterialRule.sequence(
				exposedFloor,
				subsurface,
				MaterialRule.block(VanillaIds.STONE)
		);
	}

	// -------------------------------------------------------------------------
	// Density and noise settings
	// -------------------------------------------------------------------------

	public static NoiseSettings buildNoiseSettings() {
		DensityFunction lowerVerticalBand = DensityFunctions.min(
				DensityFunctions.yClampedGradient(32, 76, -1.25, 1.0),
				DensityFunctions.yClampedGradient(132, 180, 1.0, -1.25)
		);

		DensityFunction lowerHorizontalMask = DensityFunctions.add(
				DensityFunctions.flatCache(
						DensityFunctions.noise(LOWER_ISLANDS_NOISE, 0.18, 0.0)
				),
				DensityFunctions.constant(0.12)
		);

		DensityFunction lowerIslands = DensityFunctions.min(
				lowerVerticalBand,
				lowerHorizontalMask
		);

		DensityFunction upperVerticalBand = DensityFunctions.min(
				DensityFunctions.yClampedGradient(132, 164, -1.15, 0.8),
				DensityFunctions.yClampedGradient(204, 232, 0.8, -1.15)
		);

		DensityFunction upperHorizontalMask = DensityFunctions.add(
				DensityFunctions.flatCache(
						DensityFunctions.noise(UPPER_ISLANDS_NOISE, 0.32, 0.0)
				),
				DensityFunctions.constant(-0.16)
		);

		DensityFunction upperIslands = DensityFunctions.min(
				upperVerticalBand,
				upperHorizontalMask
		);

		DensityFunction combinedIslands = DensityFunctions.max(
				lowerIslands,
				upperIslands
		);

		DensityFunction detail = DensityFunctions.mul(
				DensityFunctions.constant(0.16),
				DensityFunctions.noise(ISLAND_DETAIL_NOISE, 0.85, 0.55)
		);

		DensityFunction finalDensity = DensityFunctions.interpolated(
				DensityFunctions.add(combinedIslands, detail)
		);

		return NoiseSettings.settings()
				.seaLevel(0)
				.legacyRandomSource(false)
				.defaultBlockId(VanillaIds.STONE)
				.defaultFluidId(VanillaIds.WATER)
				.noiseSimple(0, 256, 1, 2)
				.aquifersEnabled(false)
				.oreVeinsEnabled(false)
				.disableMobGeneration(false)
				.noiseRouter(NoiseRouter.simple(finalDensity))
				.materialRule(SURFACE_RULE);
	}

	// -------------------------------------------------------------------------
	// Features
	// -------------------------------------------------------------------------

	public static Feature buildTreesFeature() {
		return Features.simpleTree(vanillaId("oak_log"), vanillaId("oak_leaves"))
				.ignoreVines(true)
				.build();
	}

	public static PlacedFeature buildTreesPlacedFeature() {
		return PlacedFeature.placed(TREES)
				.count(IntProvider.uniform(1, 3))
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter();
	}

	public static Feature buildCopperOreFeature() {
		return Features.ore(
				vanillaId("stone_ore_replaceables"),
				vanillaId("copper_ore"),
				12
		).build();
	}

	public static PlacedFeature buildCopperOrePlacedFeature() {
		return PlacedFeature.placed(COPPER_ORE)
				.count(IntProvider.uniform(5, 10))
				.inSquare()
				.uniformHeight(
						VerticalAnchor.absolute(40),
						VerticalAnchor.absolute(220)
				)
				.blockPredicateFilter(
						PlacedFeature.BlockPredicate.matchingBlockTag(
								"minecraft:stone_ore_replaceables"
						)
				)
				.biomeFilter();
	}

	public static Feature buildFlowersFeature() {
		return Features.projectedRandomPatchySquare(vanillaId("dandelion"))
				.projectThrough(PlacedFeature.BlockPredicate.replaceable())
				.size(IntProvider.uniform(2, 6))
				.maxProjectionHeight(3)
				.build();
	}

	public static PlacedFeature buildFlowersPlacedFeature() {
		return PlacedFeature.placed(FLOWERS)
				.rarityFilter(2)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter();
	}

	// -------------------------------------------------------------------------
	// Biome
	// -------------------------------------------------------------------------

	private static HashMap<Identifier, AttributeValue> buildBiomeAttributes() {
		var attributes = new HashMap<Identifier, AttributeValue>();
		attributes.put(vanillaId("visual/sky_color"), AttributeValue.ofString("#6eb1ff"));
		attributes.put(vanillaId("visual/fog_color"), AttributeValue.ofString("#c0d8ff"));
		attributes.put(vanillaId("visual/fog_end_distance"), AttributeValue.ofFloat(256.0F));
		attributes.put(vanillaId("visual/water_fog_start_distance"), AttributeValue.ofFloat(0.0F));
		attributes.put(vanillaId("visual/water_fog_end_distance"), AttributeValue.ofFloat(96.0F));
		return attributes;
	}

	public static Biome buildBiome() {
		return Biome.biome()
				.hasPrecipitation(true)
				.temperature(0.72F)
				.downfall(0.55F)
				.effects(new Biome.Effects().waterColor(0x3F76E4))
				.attributes(buildBiomeAttributes())
				.spawnSettings(
						new Biome.SpawnSettings().setCreatureSpawnProbability(0.07F)
				)
				.generation(
						new Biome.Generation()
								.addFeature(7, TREES)
								.addFeature(8, COPPER_ORE)
								.addFeature(9, FLOWERS)
				);
	}

	// -------------------------------------------------------------------------
	// Dimension type and dimension
	// -------------------------------------------------------------------------

	public static DimensionType buildDimensionType() {
		return DimensionType.dimensionType()
				.hasSkylight(true)
				.hasCeiling(false)
				.coordinateScale(1.0)
				.minY(0)
				.height(256)
				.logicalHeight(256)
				.infiniburn(BlockTags.INFINIBURN_OVERWORLD)
				.ambientLight(0.1F)
				.monsterSpawnLightUniform(0, 7)
				.monsterSpawnBlockLightLimit(0)
				.attribute(vanillaId("visual/sky_color"), "#6eb1ff")
				.attribute(vanillaId("visual/fog_color"), "#c0d8ff")
				.attribute(vanillaId("visual/sky_light_color"), "#ffffff")
				.attribute(vanillaId("gameplay/sky_light_level"), 15.0F)
				.timeline(SKY_CYCLE)
				.skybox(DimensionType.Skybox.OVERWORLD)
				.cardinalLight(DimensionType.CardinalLightType.DEFAULT)
				.hasFixedTime(false);
	}

	public static Dimension buildDimension() {
		return Dimension.dimension()
				.type(DIMENSION_TYPE)
				.noiseGenerator(
						NOISE_SETTINGS,
						BiomeSource.fixed(BIOME)
				);
	}

	// -------------------------------------------------------------------------
	// Guaranteed origin landing ruin
	// -------------------------------------------------------------------------

	public static Structure buildSkyRuinStructure() {
		return Structure.jigsaw(SKY_RUIN_START_POOL.toString())
				.biomes(Structure.Biomes.biome(BIOME))
				.step("surface_structures")
				.size(1)
				.maxDistanceFromCenter(80)
				.startHeight(
						HeightProvider.constant(VerticalAnchor.absolute(128))
				)
				.useExpansionHack(false);
	}

	public static StructureSet buildSkyRuinStructureSet() {
		return StructureSet.set()
				.addStructure(SKY_RUIN, 1)
				.dimensionOriginPlacement();
	}

	public static TemplatePool buildSkyRuinStartPool() {
		return TemplatePool.pool()
				.fallback(vanillaId("empty"))
				.single(
						SKY_RUIN_START_TEMPLATE,
						vanillaId("empty"),
						TemplatePool.Projection.RIGID,
						1
				);
	}

	// -------------------------------------------------------------------------
	// Registration
	// -------------------------------------------------------------------------

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addTimeline(SKY_CYCLE, buildSkyCycle());

		pack.addNoise(LOWER_ISLANDS_NOISE, buildLowerIslandsNoise());
		pack.addNoise(UPPER_ISLANDS_NOISE, buildUpperIslandsNoise());
		pack.addNoise(ISLAND_DETAIL_NOISE, buildIslandDetailNoise());
		pack.addNoise(SURFACE_PATCH_NOISE, buildSurfacePatchNoise());

		pack.addMaterialCondition(SURFACE_PATCH_CONDITION, buildSurfacePatchCondition());
		pack.addMaterialRule(SURFACE_RULE, buildSurfaceRule());
		pack.addNoiseSettings(NOISE_SETTINGS, buildNoiseSettings());

		pack.addFeature(TREES, buildTreesFeature());
		pack.addPlacedFeature(TREES, buildTreesPlacedFeature());
		pack.addFeature(COPPER_ORE, buildCopperOreFeature());
		pack.addPlacedFeature(COPPER_ORE, buildCopperOrePlacedFeature());
		pack.addFeature(FLOWERS, buildFlowersFeature());
		pack.addPlacedFeature(FLOWERS, buildFlowersPlacedFeature());

		pack.addBiome(BIOME, buildBiome());
		pack.addDimensionType(DIMENSION_TYPE, buildDimensionType());

		pack.addTemplatePool(SKY_RUIN_START_POOL, buildSkyRuinStartPool());
		pack.addStructure(SKY_RUIN, buildSkyRuinStructure());
		pack.addStructureSet(SKY_RUIN, buildSkyRuinStructureSet());

		pack.addDimension(DIMENSION, buildDimension());
	}

	// -------------------------------------------------------------------------
	// Debug dump
	// -------------------------------------------------------------------------

	private static <T> void dump(String label, Codec<T> codec, T value) {
		System.out.println(label + ":");
		System.out.println(JsonBytes.encodeToPrettyString(codec, value));
	}

	public static void main() {
		dump("Noise Settings JSON", NoiseSettings.CODEC, buildNoiseSettings());
		dump("Material Rule JSON", MaterialRule.CODEC, buildSurfaceRule());
		dump("Dimension JSON", Dimension.CODEC, buildDimension());
		dump("Template Pool JSON", TemplatePool.CODEC, buildSkyRuinStartPool());

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:sky_islands");
		pack.addDataPackMcmeta("Sky Islands - floating isles generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/sky_islands"));
	}
}