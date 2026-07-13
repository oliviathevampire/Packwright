package test;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EasingType;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.assets.timeline.Timeline;
import net.vampirestudios.packwright.data.worldgen.*;
import net.vampirestudios.packwright.data.worldgen.biome.Biome;
import net.vampirestudios.packwright.data.worldgen.dimension.Dimension;
import net.vampirestudios.packwright.data.worldgen.dimension.DimensionType;
import net.vampirestudios.packwright.data.worldgen.dimension.Parameter;
import net.vampirestudios.packwright.data.worldgen.dimension.Parameters;
import net.vampirestudios.packwright.data.worldgen.dimension.biomeSources.BiomeSource;
import net.vampirestudios.packwright.data.worldgen.feature.Feature;
import net.vampirestudios.packwright.data.worldgen.feature.Features;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.packwright.data.worldgen.material.MaterialCondition;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;
import net.vampirestudios.packwright.data.worldgen.noise.DensityFunction;
import net.vampirestudios.packwright.data.worldgen.noise.DensityFunctions;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseParameters;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseRouter;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.packwright.data.worldgen.structure.Structure;
import net.vampirestudios.packwright.data.worldgen.structure.StructureSet;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;
import static net.vampirestudios.packwright.util.ResourceHelpers.vanillaId;

/**
 * A complete Ember Wastes dimension with:
 *
 * <ul>
 *     <li>two-dimensional macro terrain noises,</li>
 *     <li>narrow volcanic ridges and broad lava basins,</li>
 *     <li>bounded terrain and density-function caverns,</li>
 *     <li>rare tube/rift carvers rather than excessive overlapping caves,</li>
 *     <li>registered and referenced material conditions,</li>
 *     <li>identifier-backed configured-feature references,</li>
 *     <li>four climate-selected biomes, and</li>
 *     <li>a non-empty, surface-projected origin sanctum.</li>
 * </ul>
 */
public final class EmberWastesWorldgen {
	private static final Identifier SKY_CYCLE = myModId("ember_wastes_sky_cycle");
	private static final Identifier MATERIAL_RULE = myModId("ember_wastes_surface");
	private static final Identifier ABOVE_MAGMA_BAND = myModId("above_magma_band");
	private static final Identifier BELOW_MAGMA_BAND = myModId("below_magma_band");
	private static final Identifier MAGMA_PATCH = myModId("magma_patch");
	private static final Identifier CONTINENTS_NOISE = myModId("ember_continents");
	private static final Identifier RIDGES_NOISE = myModId("ember_ridges");
	private static final Identifier BASINS_NOISE = myModId("ember_basins");
	private static final Identifier CAVERNS_NOISE = myModId("ember_caverns");
	private static final Identifier CAVE_LAYERS_NOISE = myModId("ember_cave_layers");
	private static final Identifier SURFACE_NOISE = myModId("ember_surface");

	private static Identifier myModId(String path) {
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
						.addKeyframe(0, "#2b0f0a")
						.addKeyframe(8000, "#7a2d12")
						.addKeyframe(16000, "#4a1a0d")
				)
				.addTrack(EnvironmentAttributes.FOG_COLOR, track -> track
						.ease(EasingType.LINEAR)
						.addKeyframe(0, "#33201a")
						.addKeyframe(8000, "#59301f")
						.addKeyframe(16000, "#40251c")
				)
				.addTrack(EnvironmentAttributes.FOG_END_DISTANCE, track -> track
						.ease(EasingType.LINEAR)
						.addKeyframe(0, 144.0F)
						.addKeyframe(8000, 88.0F)
						.addKeyframe(16000, 120.0F)
				);
	}

	// -------------------------------------------------------------------------
	// Custom worldgen noises
	// -------------------------------------------------------------------------

	public static NoiseParameters buildContinentsNoise() {
		return NoiseParameters.of(-7, 1.0, 1.0, 0.5, 0.25);
	}

	public static NoiseParameters buildRidgesNoise() {
		return NoiseParameters.of(-6, 1.0, 0.8, 0.4);
	}

	public static NoiseParameters buildBasinsNoise() {
		return NoiseParameters.of(-8, 1.0, 0.6);
	}

	public static NoiseParameters buildCavernsNoise() {
		return NoiseParameters.of(-5, 1.0, 0.7, 0.35);
	}

	public static NoiseParameters buildCaveLayersNoise() {
		return NoiseParameters.of(-3, 1.0, 1.0, 0.5);
	}

	public static NoiseParameters buildSurfaceNoise() {
		return NoiseParameters.of(-4, 1.0, 0.5, 0.25);
	}

	// -------------------------------------------------------------------------
	// Material conditions and rule
	// -------------------------------------------------------------------------

	public static MaterialCondition buildAboveMagmaBandCondition() {
		return MaterialCondition.yAbove(VerticalAnchor.absolute(34), 0, false);
	}

	public static MaterialCondition buildBelowMagmaBandCondition() {
		return MaterialCondition.not(MaterialCondition.yAbove(VerticalAnchor.absolute(45), 0, false));
	}

	public static MaterialCondition buildMagmaPatchCondition() {
		return MaterialCondition.noiseThreshold(SURFACE_NOISE, 0.35, 1.0);
	}

	public static MaterialRule buildSurfaceRule() {
		MaterialRule magmaNearLava = MaterialRule.condition(
				MaterialCondition.reference(ABOVE_MAGMA_BAND),
				MaterialRule.condition(
						MaterialCondition.reference(BELOW_MAGMA_BAND),
						MaterialRule.condition(
								MaterialCondition.reference(MAGMA_PATCH),
								MaterialRule.block(vanillaId("magma_block"))
						)
				)
		);

		MaterialRule exposedFloor = MaterialRule.condition(
				MaterialCondition.stoneDepth(0, false, "floor"),
				MaterialRule.sequence(
						magmaNearLava,
						MaterialRule.condition(
								MaterialCondition.biome(myModId("lava_deltas")),
								MaterialRule.block(vanillaId("basalt"))
						),
						MaterialRule.condition(
								MaterialCondition.biome(myModId("basalt_highlands")),
								MaterialRule.block(vanillaId("smooth_basalt"))
						),
						MaterialRule.block(vanillaId("blackstone"))
				)
		);

		return MaterialRule.sequence(
				exposedFloor,
				MaterialRule.condition(
						MaterialCondition.stoneDepth(
								0,
								true,
								4,
								"floor"
						),
						MaterialRule.block(vanillaId("blackstone"))
				),
				MaterialRule.condition(
						MaterialCondition.verticalGradient(
								"mymod:basalt_gradient",
								VerticalAnchor.aboveBottom(24),
								VerticalAnchor.aboveBottom(52)
						),
						MaterialRule.block(vanillaId("basalt"))
				),
				MaterialRule.block(vanillaId("blackstone"))
		);
	}

	// -------------------------------------------------------------------------
	// Noise settings
	// -------------------------------------------------------------------------

	public static NoiseSettings buildNoiseSettings() {
		DensityFunction baseShape =
				DensityFunctions.yClampedGradient(24, 112, 1.45, -1.15);

		/*
		 * All three macro noises are flat-cached and have a Y scale of zero.
		 * They alter terrain height without producing internal horizontal
		 * strata or changing the biome mask at different heights.
		 */
		DensityFunction continents = DensityFunctions.flatCache(
				DensityFunctions.noise(CONTINENTS_NOISE, 0.18, 0.0)
		);

		DensityFunction broadTerrain = DensityFunctions.mul(
				DensityFunctions.constant(0.95),
				continents
		);

		DensityFunction basinSelector = DensityFunctions.flatCache(
				DensityFunctions.noise(BASINS_NOISE, 0.07, 0.0)
		);

		DensityFunction lavaBasins = DensityFunctions.rangeChoice(
				basinSelector,
				-1.0,
				-0.36,
				DensityFunctions.constant(-0.92),
				DensityFunctions.constant(0.0)
		);

		DensityFunction ridgeNoise = DensityFunctions.flatCache(
				DensityFunctions.noise(RIDGES_NOISE, 0.30, 0.0)
		);

		/*
		 * Ridge shape: square(clamp(0.58 - abs(noise), 0, 0.58)).
		 * This creates narrow ridges near the noise's zero crossings.
		 */
		DensityFunction ridgeShape = DensityFunctions.square(DensityFunctions.clamp(DensityFunctions.add(
				DensityFunctions.constant(0.58),
				DensityFunctions.mul(DensityFunctions.constant(-1.0), DensityFunctions.abs(ridgeNoise))
		), 0.0, 0.58));

		DensityFunction volcanicRidges = DensityFunctions.mul(DensityFunctions.constant(4.0), ridgeShape);

		DensityFunction surfaceShape = DensityFunctions.add(
				baseShape,
				DensityFunctions.add(broadTerrain, DensityFunctions.add(lavaBasins, volcanicRidges))
		);

		/*
		 * Density caves provide the large chambers. Carvers are kept rare and
		 * are responsible only for occasional tubes and surface rifts.
		 */
		DensityFunction caveLayers = DensityFunctions.mul(
				DensityFunctions.constant(3.25),
				DensityFunctions.square(DensityFunctions.noise(CAVE_LAYERS_NOISE, 0.85, 3.2))
		);
		DensityFunction cavernField = DensityFunctions.clamp(DensityFunctions.add(
				DensityFunctions.constant(0.18),
				DensityFunctions.noise(CAVERNS_NOISE, 0.72, 0.58)
		), -1.0, 1.0);
		DensityFunction caves = DensityFunctions.add(caveLayers, cavernField);
		DensityFunction carvedTerrain = DensityFunctions.min(surfaceShape, caves);
		DensityFunction bottomFloor =
				DensityFunctions.yClampedGradient(0, 14, 1.25, -1.0);
		DensityFunction upperLimit =
				DensityFunctions.yClampedGradient(148, 188, 1.0, -1.25);
		DensityFunction boundedTerrain = DensityFunctions.min(
				DensityFunctions.max(carvedTerrain, bottomFloor),
				upperLimit
		);
		DensityFunction finalDensity = DensityFunctions.interpolated(boundedTerrain);

		return NoiseSettings.settings()
				.seaLevel(40)
				.legacyRandomSource(false)
				.defaultBlockId(vanillaId("blackstone"))
				.defaultFluidId(vanillaId("lava"))
				.noiseSimple(0, 256, 1, 2)
				.disableMobGeneration(false)
				.aquifersEnabled(false)
				.oreVeinsEnabled(false)
				.noiseRouter(NoiseRouter.simple(finalDensity))
				.materialRule(MATERIAL_RULE);
	}

	// -------------------------------------------------------------------------
	// Carvers
	// -------------------------------------------------------------------------

	public static Carver buildLavaTubesCarver() {
		return Carver.cave()
				.probability(0.035F)
				.y(HeightProvider.veryBiasedToBottom(VerticalAnchor.aboveBottom(12), VerticalAnchor.absolute(104), 8))
				.count(IntProvider.uniform(1, 2))
				.thickness(FloatProvider.uniform(0.6F, 1.2F))
				.weirdThicknessBias(true)
				.roomVerticalRadiusMultiplier(FloatProvider.constant(0.45F));
	}

	public static Carver buildRiftsCarver() {
		return Carver.canyon()
				.probability(0.008F)
				.y(HeightProvider.trapezoid(VerticalAnchor.absolute(26), VerticalAnchor.absolute(82), 18))
				.verticalRotation(FloatProvider.uniform(-0.035F, 0.035F))
				.shape(Carver.CanyonShape.canyonShape()
						.distanceFactor(FloatProvider.uniform(0.85F, 1.05F))
						.horizontalRadiusFactor(FloatProvider.uniform(0.65F, 0.95F))
						.thickness(FloatProvider.trapezoid(0.0F, 6.0F, 2.5F))
						.yScale(3.6F)
						.verticalRadiusCenterFactor(0.0F)
						.verticalRadiusDefaultFactor(1.0F)
						.widthSmoothness(3)
				);
	}

	// -------------------------------------------------------------------------
	// Features and placed features
	// -------------------------------------------------------------------------

	public static Feature buildLavaDeltaFeature() {
		return Features.deltaFeature(vanillaId("lava"),vanillaId("magma_block"))
				.size(IntProvider.uniform(4, 9))
				.rimSize(IntProvider.uniform(1, 3))
				.build();
	}

	public static PlacedFeature buildLavaDeltaPlaced() {
		return PlacedFeature.placed(myModId("ember_lava_deltas"))
				.count(IntProvider.uniform(1, 4))
				.inSquare()
				.heightmap("WORLD_SURFACE_WG")
				.biomeFilter();
	}

	public static Feature buildBasaltSpiresFeature() {
		return Features.steppedColumnCluster(vanillaId("basalt"))
				.columnReach(IntProvider.uniform(0, 2))
				.columnCount(IntProvider.uniform(2, 8))
				.clusterReach(IntProvider.uniform(2, 3))
				.height(IntProvider.uniform(4, 9))
				.build();
	}

	public static PlacedFeature buildBasaltSpiresPlaced() {
		return PlacedFeature.placed(myModId("ember_basalt_spires"))
				.rarityFilter(4)
				.inSquare()
				.heightmap("WORLD_SURFACE_WG")
				.biomeFilter();
	}

	public static Feature buildGoldPocketFeature() {
		return Features.oreInBlock(vanillaId("blackstone"), vanillaId("gilded_blackstone"), 8).build();
	}

	public static PlacedFeature buildGoldPocketPlaced() {
		return PlacedFeature.placed(myModId("ember_gold_pockets"))
				.count(IntProvider.uniform(3, 8))
				.inSquare()
				.uniformHeight(VerticalAnchor.aboveBottom(10), VerticalAnchor.absolute(112))
				.biomeFilter();
	}

	public static Feature buildAshScrubFeature() {
		return Features.projectedRandomPatchySquare(vanillaId("dead_bush"))
				.projectThrough(PlacedFeature.BlockPredicate.replaceable())
				.size(IntProvider.uniform(3, 6))
				.maxProjectionHeight(4)
				.build();
	}

	public static PlacedFeature buildAshScrubPlaced() {
		return PlacedFeature.placed(myModId("ember_ash_scrub"))
				.rarityFilter(3)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter();
	}

	// -------------------------------------------------------------------------
	// Biomes
	// -------------------------------------------------------------------------

	private static HashMap<Identifier, AttributeValue> biomeAttributes(String sky, String fog, float fogDistance) {
		var attributes = new HashMap<Identifier, AttributeValue>();
		attributes.put(vanillaId("visual/sky_color"), AttributeValue.ofString(sky));
		attributes.put(vanillaId("visual/fog_color"), AttributeValue.ofString(fog));
		attributes.put(vanillaId("visual/fog_end_distance"), AttributeValue.ofFloat(fogDistance));
		return attributes;
	}

	private static Biome baseBiome(String sky, String fog, float fogDistance, int waterColor, Biome.Generation generation) {
		return Biome.biome()
				.hasPrecipitation(false)
				.temperature(2.0F)
				.downfall(0.0F)
				.effects(new Biome.Effects().waterColor(waterColor))
				.attributes(biomeAttributes(sky, fog, fogDistance))
				.spawnSettings(new Biome.SpawnSettings().setCreatureSpawnProbability(0.0F))
				.generation(generation);
	}

	public static Biome buildEmberWastesBiome() {
		return baseBiome(
				"#4a1a0d",
				"#40251c",
				128.0F,
				0x62281a,
				new Biome.Generation()
						.addCarver(myModId("ember_tubes"))
						.addCarver(myModId("ember_rifts"))
						.addFeature(2, myModId("ember_gold_pockets"))
						.addFeature(5, myModId("ember_lava_deltas"))
						.addFeature(9, myModId("ember_ash_scrub"))
		);
	}

	public static Biome buildBasaltHighlandsBiome() {
		return baseBiome(
				"#37100a",
				"#332019",
				144.0F,
				0x4a2018,
				new Biome.Generation()
						.addCarver(myModId("ember_tubes"))
						.addFeature(2, myModId("ember_gold_pockets"))
						.addFeature(6, myModId("ember_basalt_spires"))
		);
	}

	public static Biome buildAshBarrensBiome() {
		return baseBiome(
				"#321816",
				"#332b28",
				88.0F,
				0x49302a,
				new Biome.Generation()
						.addCarver(myModId("ember_tubes"))
						.addFeature(2, myModId("ember_gold_pockets"))
						.addFeature(9, myModId("ember_ash_scrub"))
		);
	}

	public static Biome buildLavaDeltasBiome() {
		return baseBiome(
				"#6a1c08",
				"#542015",
				104.0F,
				0x7a2418,
				new Biome.Generation()
						.addCarver(myModId("ember_rifts"))
						.addFeature(5, myModId("ember_lava_deltas"))
						.addFeature(6, myModId("ember_basalt_spires"))
		);
	}

	public static BiomeSource buildBiomeSource() {
		return BiomeSource.multiNoiseBuilder()
				.add(
						myModId("lava_deltas"),
						Parameters.of(
								Parameter.span(0.45F, 1.0F),
								Parameter.full(),
								Parameter.span(-1.0F, -0.35F),
								Parameter.span(-1.0F, 0.15F),
								Parameter.full(),
								Parameter.full()
						)
				)
				.add(
						myModId("basalt_highlands"),
						Parameters.of(
								Parameter.full(),
								Parameter.full(),
								Parameter.span(0.15F, 1.0F),
								Parameter.span(-1.0F, -0.2F),
								Parameter.full(),
								Parameter.span(0.2F, 1.0F)
						)
				)
				.add(
						myModId("ash_barrens"),
						Parameters.of(
								Parameter.span(-1.0F, 0.2F),
								Parameter.full(),
								Parameter.full(),
								Parameter.span(0.25F, 1.0F),
								Parameter.full(),
								Parameter.full()
						)
				)
				.add(
						myModId("ember_wastes"),
						Parameters.of(
								Parameter.span(0.2F, 0.45F),
								Parameter.full(),
								Parameter.span(-0.35F, 0.15F),
								Parameter.span(-0.2F, 0.25F),
								Parameter.full(),
								Parameter.span(-1.0F, 0.2F)
						)
				)
				.build();
	}

	// -------------------------------------------------------------------------
	// Dimension type
	// -------------------------------------------------------------------------

	public static DimensionType buildDimensionType() {
		return DimensionType.dimensionType()
				.hasSkylight(true)
				.hasCeiling(false)
				.coordinateScale(4.0)
				.minY(0)
				.height(256)
				.logicalHeight(256)
				.infiniburn(BlockTags.INFINIBURN_NETHER)
				.ambientLight(0.25F)
				.monsterSpawnLightUniform(0, 11)
				.monsterSpawnBlockLightLimit(15)
				.attributes(new EnvironmentAttributes()
						.waterEvaporates(true)
						.respawnAnchorWorks(true)
						.bedRule(BedRule.DESTROY_ON_USE)
						.strawBedRule(BedRule.DESTROY_ON_USE)
						.monstersBurn(false)
						.fastLava(true)
						.canStartRaid(false)
						.piglinsZombify(false)
				)
				.timeline(SKY_CYCLE)
				.skybox(DimensionType.Skybox.END)
				.cardinalLight(DimensionType.CardinalLightType.DEFAULT)
				.hasFixedTime(true);
	}

	// -------------------------------------------------------------------------
	// Guaranteed origin sanctum
	// -------------------------------------------------------------------------

	public static Structure buildSanctumStructure() {
		return Structure.jigsaw("mymod:obsidian_sanctum/start_pool")
				.biomes(Structure.Biomes.list(List.of(
						myModId("ember_wastes"),
						myModId("basalt_highlands"),
						myModId("ash_barrens"),
						myModId("lava_deltas")
				)))
				.step("surface_structures")
				.size(2)
				.maxDistanceFromCenter(96)
				.startHeight(HeightProvider.constant(VerticalAnchor.absolute(64)))
				.projectStartToHeightmap("WORLD_SURFACE_WG")
				.useExpansionHack(false);
	}

	public static StructureSet buildSanctumStructureSet() {
		return StructureSet.set()
				.addStructure(myModId("obsidian_sanctum"), 1)
				.dimensionOriginPlacement();
	}

	public static TemplatePool buildSanctumStartPool() {
		return TemplatePool.pool()
				.fallback(vanillaId("empty"))
				.single(
						myModId("obsidian_sanctum/start"),
						vanillaId("empty"),
						TemplatePool.Projection.RIGID,
						1
				);
	}

	// -------------------------------------------------------------------------
	// Dimension
	// -------------------------------------------------------------------------

	public static Dimension buildDimension() {
		return Dimension.dimension()
				.type(myModId("ember_wastes_type"))
				.noiseGenerator(myModId("ember_wastes"), buildBiomeSource());
	}

	// -------------------------------------------------------------------------
	// Registration
	// -------------------------------------------------------------------------

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addTimeline(SKY_CYCLE, buildSkyCycle());

		pack.addNoise(CONTINENTS_NOISE, buildContinentsNoise());
		pack.addNoise(RIDGES_NOISE, buildRidgesNoise());
		pack.addNoise(BASINS_NOISE, buildBasinsNoise());
		pack.addNoise(CAVERNS_NOISE, buildCavernsNoise());
		pack.addNoise(CAVE_LAYERS_NOISE, buildCaveLayersNoise());
		pack.addNoise(SURFACE_NOISE, buildSurfaceNoise());

		pack.addMaterialCondition(ABOVE_MAGMA_BAND, buildAboveMagmaBandCondition());
		pack.addMaterialCondition(BELOW_MAGMA_BAND, buildBelowMagmaBandCondition());
		pack.addMaterialCondition(MAGMA_PATCH, buildMagmaPatchCondition());
		pack.addMaterialRule(MATERIAL_RULE, buildSurfaceRule());

		pack.addNoiseSettings(myModId("ember_wastes"), buildNoiseSettings());

		pack.addCarver(myModId("ember_tubes"), buildLavaTubesCarver());
		pack.addCarver(myModId("ember_rifts"), buildRiftsCarver());

		pack.addFeature(myModId("ember_lava_deltas"), buildLavaDeltaFeature());
		pack.addPlacedFeature(myModId("ember_lava_deltas"), buildLavaDeltaPlaced());

		pack.addFeature(myModId("ember_basalt_spires"), buildBasaltSpiresFeature());
		pack.addPlacedFeature(myModId("ember_basalt_spires"), buildBasaltSpiresPlaced());

		pack.addFeature(myModId("ember_gold_pockets"), buildGoldPocketFeature());
		pack.addPlacedFeature(myModId("ember_gold_pockets"), buildGoldPocketPlaced());

		pack.addFeature(myModId("ember_ash_scrub"), buildAshScrubFeature());
		pack.addPlacedFeature(myModId("ember_ash_scrub"), buildAshScrubPlaced());

		pack.addBiome(myModId("ember_wastes"), buildEmberWastesBiome());
		pack.addBiome(myModId("basalt_highlands"), buildBasaltHighlandsBiome());
		pack.addBiome(myModId("ash_barrens"), buildAshBarrensBiome());
		pack.addBiome(myModId("lava_deltas"), buildLavaDeltasBiome());

		pack.addDimensionType(myModId("ember_wastes_type"), buildDimensionType());
		pack.addTemplatePool(myModId("obsidian_sanctum/start_pool"), buildSanctumStartPool());
		pack.addStructure(myModId("obsidian_sanctum"), buildSanctumStructure());
		pack.addStructureSet(myModId("obsidian_sanctum"), buildSanctumStructureSet());
		pack.addDimension(myModId("ember_wastes"), buildDimension());
	}

	// -------------------------------------------------------------------------
	// Debug dump
	// -------------------------------------------------------------------------

	public static void main() {
		System.out.println("Noise Settings JSON:");
		System.out.println(JsonBytes.encodeToPrettyString(NoiseSettings.CODEC, buildNoiseSettings()));

		System.out.println("Biome Source JSON:");
		System.out.println(JsonBytes.encodeToPrettyString(BiomeSource.CODEC, buildBiomeSource()));

		System.out.println("Template Pool JSON:");
		System.out.println(JsonBytes.encodeToPrettyString(TemplatePool.CODEC, buildSanctumStartPool()));

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:ember_wastes");
		pack.addDataPackMcmeta("The Ember Wastes - volcanic badlands generated by Packwright");
		registerAll(pack);

		pack.dumpDirect(Path.of("dumps/ember_wastes"));
	}
}