package test;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EasingType;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.assets.timeline.Timeline;
import net.vampirestudios.arrp.data.worldgen.*;
import net.vampirestudios.arrp.data.worldgen.biome.Biome;
import net.vampirestudios.arrp.data.worldgen.dimension.Dimension;
import net.vampirestudios.arrp.data.worldgen.dimension.DimensionType;
import net.vampirestudios.arrp.data.worldgen.feature.Feature;
import net.vampirestudios.arrp.data.worldgen.feature.Features;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.data.worldgen.material.MaterialCondition;
import net.vampirestudios.arrp.data.worldgen.material.MaterialRule;
import net.vampirestudios.arrp.data.worldgen.noise.NoiseSettings;
import net.vampirestudios.arrp.data.worldgen.structure.Structure;
import net.vampirestudios.arrp.data.worldgen.structure.StructureSet;
import net.vampirestudios.arrp.util.JsonBytes;
import net.vampirestudios.arrp.util.VanillaIds;

import java.nio.file.Path;
import java.util.HashMap;

import static net.vampirestudios.arrp.util.ResourceHelpers.customId;
import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

/**
 * A complete custom dimension: the Ember Wastes, a scorched basalt flat scarred by
 * lava deltas and ash storms. Builds every piece a dimension needs — dimension type,
 * noise settings, biome, carvers, features, structures — and, unlike
 * {@link SkyIslandsWorldgen}, leans on the 26.3 additions: the surface pipeline lives
 * in the {@code worldgen/material_rule} registry and is referenced by ID from the
 * noise settings, carvers use the flattened format with the new int provider types,
 * and the central sanctum is placed with {@code minecraft:dimension_origin}.
 * <p>
 * {@link #registerAll(RuntimeResourcePack)} adds everything to a pack;
 * {@link #main()} dumps each JSON for eyeballing.
 */
public class EmberWastesWorldgen {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	/* ----------------------------------------------------------
	 * 1) Timeline: mymod:ember_wastes_sky_cycle
	 *    A slow ember glow instead of a day/night cycle.
	 * ---------------------------------------------------------- */

	public static Timeline buildSkyCycle() {
		return new Timeline()
				.setPeriodTicks(24000)
				.addTrack(EnvironmentAttributes.SKY_COLOR, t -> t
						.ease(EasingType.LINEAR)
						.addKeyframe(0,     "#2b0f0a")  // smoulder
						.addKeyframe(8000,  "#7a2d12")  // flare
						.addKeyframe(16000, "#4a1a0d")  // fade
				)
				.addTrack(EnvironmentAttributes.FOG_COLOR, t -> t
						.ease(EasingType.LINEAR)
						.addKeyframe(0,     "#33201a")
						.addKeyframe(8000,  "#59301f")
						.addKeyframe(16000, "#40251c")
				);
	}

	/* ----------------------------------------------------------
	 * 2) Material rule & condition registry entries (26.3)
	 *    Blackstone crust on the surface, a noisy basalt->blackstone
	 *    gradient below, magma near the lava sea in the hollows.
	 * ---------------------------------------------------------- */

	public static MaterialCondition buildNearLavaSeaCondition() {
		return MaterialCondition.yAbove(VerticalAnchor.absolute(38), 0, false);
	}

	public static MaterialRule buildSurfaceRule() {
		return MaterialRule.sequence(
				// exposed floors: blackstone crust, magma where the ground dips to the lava sea
				MaterialRule.condition(
						MaterialCondition.stoneDepth(0, false, "floor"),
						MaterialRule.sequence(
								MaterialRule.condition(
										MaterialCondition.not(buildNearLavaSeaCondition()),
										MaterialRule.block(vanillaId("magma_block"))),
								MaterialRule.block(vanillaId("blackstone")))),
				// a few blocks of blackstone under the crust
				MaterialRule.condition(
						MaterialCondition.stoneDepth(0, true, 4, "floor"),
						MaterialRule.block(vanillaId("blackstone"))),
				// noisy transition into basalt bedrock-wards
				MaterialRule.condition(
						MaterialCondition.verticalGradient("mymod:basalt_gradient",
								VerticalAnchor.aboveBottom(24), VerticalAnchor.aboveBottom(48)),
						MaterialRule.block(vanillaId("basalt"))),
				MaterialRule.block(vanillaId("blackstone"))
		);
	}

	/* ----------------------------------------------------------
	 * 3) Noise settings: mymod:ember_wastes
	 *    References the material rule registry entry by ID.
	 * ---------------------------------------------------------- */

	public static NoiseSettings buildNoiseSettings() {
		return NoiseSettings.settings()
				.seaLevel(40) // a sea of lava, thanks to default_fluid
				.legacyRandomSource(false)
				.defaultBlockId(vanillaId("blackstone"))
				.defaultFluidId(vanillaId("lava"))
				.noiseSimple(0, 256, 1, 2)
				.disableMobGeneration(false)
				.aquifersEnabled(false)
				.oreVeinsEnabled(false)
				// flat wastes: solid below y=44, air above y=72
				.simpleNoiseRouterGradient(44, 72)
				// ID reference into the worldgen/material_rule registry (since 26.3)
				.materialRule(myModId("ember_wastes_surface"));
	}

	/* ----------------------------------------------------------
	 * 4) Carvers: mymod:ember_tubes / mymod:ember_rifts
	 *    26.3 flattened carver format, no replaceable/lava_level.
	 * ---------------------------------------------------------- */

	public static Carver buildLavaTubesCarver() {
		return Carver.cave()
				.probability(0.1F)
				.y(HeightProvider.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(120)))
				.count(IntProvider.veryBiasedToBottom(1, 4))
				.thickness(FloatProvider.uniform(0.8F, 1.6F))
				.weirdThicknessBias(true)
				.roomVerticalRadiusMultiplier(FloatProvider.constant(0.6F));
	}

	public static Carver buildRiftsCarver() {
		return Carver.canyon()
				.probability(0.02F)
				.y(HeightProvider.uniform(VerticalAnchor.absolute(20), VerticalAnchor.absolute(70)))
				.verticalRotation(FloatProvider.uniform(-0.05F, 0.05F))
				.shape(Carver.CanyonShape.canyonShape()
						.distanceFactor(FloatProvider.uniform(0.8F, 1.0F))
						.horizontalRadiusFactor(FloatProvider.uniform(0.7F, 1.0F))
						.thickness(FloatProvider.trapezoid(0.0F, 8.0F, 3.0F))
						.yScale(4.0F)
						.verticalRadiusCenterFactor(0.0F)
						.verticalRadiusDefaultFactor(1.0F)
						.widthSmoothness(2));
	}

	/* ----------------------------------------------------------
	 * 5) Features & placed features
	 * ---------------------------------------------------------- */

	/** lava deltas rimmed with magma, the signature terrain scar */
	public static Feature buildLavaDeltaFeature() {
		return Features.deltaFeature(vanillaId("lava"), vanillaId("magma_block"))
				.size(IntProvider.uniform(4, 9))
				.rimSize(IntProvider.uniform(1, 3))
				.build();
	}

	public static PlacedFeature buildLavaDeltaPlaced() {
		return PlacedFeature.placed(buildLavaDeltaFeature())
				.count(IntProvider.uniform(2, 6))
				.inSquare()
				.heightmap("WORLD_SURFACE_WG")
				.biomeFilter();
	}

	/** clusters of stepped basalt columns rising from the crust */
	public static Feature buildBasaltSpiresFeature() {
		return Features.steppedColumnCluster(vanillaId("basalt"))
				.columnReach(IntProvider.uniform(0, 2))
				.columnCount(IntProvider.uniform(2, 8))
				.clusterReach(IntProvider.uniform(2, 3))
				.height(IntProvider.uniform(4, 9)) // capped at 10 by the game
				.build();
	}

	public static PlacedFeature buildBasaltSpiresPlaced() {
		return PlacedFeature.placed(buildBasaltSpiresFeature())
				.rarityFilter(4)
				.inSquare()
				.heightmap("WORLD_SURFACE_WG")
				.biomeFilter();
	}

	/** pockets of gold hiding in the blackstone */
	public static Feature buildGoldPocketFeature() {
		return Features.oreInBlock(vanillaId("blackstone"), vanillaId("gilded_blackstone"), 8)
				.build();
	}

	public static PlacedFeature buildGoldPocketPlaced() {
		return PlacedFeature.placed(buildGoldPocketFeature())
				.count(IntProvider.uniform(4, 10))
				.inSquare()
				.uniformHeight(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(100))
				.biomeFilter();
	}

	/**
	 * scattered dead bushes clinging to the ash; random_patch was removed in 26.1,
	 * projected_random_patchy_square (26.3) is the replacement for scattered vegetation
	 */
	public static Feature buildAshScrubFeature() {
		return Features.projectedRandomPatchySquare(vanillaId("dead_bush"))
				.projectThrough(PlacedFeature.BlockPredicate.replaceable())
				.size(IntProvider.uniform(3, 6))
				.maxProjectionHeight(4)
				.build();
	}

	public static PlacedFeature buildAshScrubPlaced() {
		return PlacedFeature.placed(buildAshScrubFeature())
				.rarityFilter(2)
				.inSquare()
				.heightmap("MOTION_BLOCKING")
				.biomeFilter();
	}

	/* ----------------------------------------------------------
	 * 6) Biome: mymod:ember_wastes
	 * ---------------------------------------------------------- */

	public static Biome buildBiome() {
		var attribMap = new HashMap<Identifier, AttributeValue>();
		attribMap.put(vanillaId("visual/sky_color"), AttributeValue.ofString("#4a1a0d"));
		attribMap.put(vanillaId("visual/fog_color"), AttributeValue.ofString("#40251c"));
		attribMap.put(vanillaId("visual/fog_end_distance"), AttributeValue.ofFloat(128.0f));

		return Biome.biome()
				.hasPrecipitation(false)
				.temperature(2.0F)
				.downfall(0.0F)
				.effects(new Biome.Effects().waterColor(0x62281a))
				.attributes(attribMap)
				.spawnSettings(new Biome.SpawnSettings().setCreatureSpawnProbability(0.0F))
				.generation(new Biome.Generation()
						.addCarver(myModId("ember_tubes"))
						.addCarver(myModId("ember_rifts"))
						.addFeature(2, myModId("ember_gold_pockets"))
						.addFeature(5, myModId("ember_lava_deltas"))
						.addFeature(6, myModId("ember_basalt_spires"))
						.addFeature(9, myModId("ember_ash_scrub")));
	}

	/* ----------------------------------------------------------
	 * 7) Dimension type: mymod:ember_wastes_type
	 *    Nether-ish rules expressed as environment attributes.
	 * ---------------------------------------------------------- */

	public static DimensionType buildDimensionType() {
		return DimensionType.dimensionType()
				.hasSkylight(true)
				.hasCeiling(false)
				.coordinateScale(4.0) // fast travel, like the Nether
				.minY(0)
				.height(256)
				.logicalHeight(256)
				.infiniburn(BlockTags.INFINIBURN_NETHER)
				.ambientLight(0.25f)
				.monsterSpawnLightUniform(0, 11)
				.monsterSpawnBlockLightLimit(15)
				.attributes(new EnvironmentAttributes()
						.waterEvaporates(true)
						.respawnAnchorWorks(true)
						// nether-style: no spawn point, no sleeping, bed explodes on use
						.bedRule(EnvironmentAttributes.BedRuleCondition.NEVER,
								EnvironmentAttributes.BedRuleCondition.NEVER, true, null)
						.strawBedRule(EnvironmentAttributes.BedRuleCondition.NEVER,
								EnvironmentAttributes.BedRuleCondition.NEVER, true, null)
						.monstersBurn(false)
						.fastLava(true)
						.canStartRaid(false)
						.piglinsZombify(false))
				.timeline(myModId("ember_wastes_sky_cycle"))
				.skybox(DimensionType.Skybox.END)
				.cardinalLight(DimensionType.CardinalLightType.DEFAULT)
				.hasFixedTime(true);
	}

	/* ----------------------------------------------------------
	 * 8) Structure: mymod:obsidian_sanctum, placed once at the
	 *    dimension origin (26.3 placement type) as the arrival point.
	 * ---------------------------------------------------------- */

	public static Structure buildSanctumStructure() {
		return Structure.jigsaw("mymod:obsidian_sanctum/start_pool")
				.biomesId(myModId("ember_wastes"))
				.step("surface_structures")
				.size(2)
				.maxDistanceFromCenter(96)
				.startHeightInt(0)
				.useExpansionHack(false);
	}

	public static StructureSet buildSanctumStructureSet() {
		return StructureSet.set()
				.addStructure(myModId("obsidian_sanctum"), 1)
				.dimensionOriginPlacement();
	}

	/** an empty start pool so the jigsaw structure's reference resolves */
	public static TemplatePool buildSanctumStartPool() {
		return TemplatePool.pool().fallback(vanillaId("empty"));
	}

	/* ----------------------------------------------------------
	 * 9) Dimension: mymod:ember_wastes
	 * ---------------------------------------------------------- */

	public static Dimension buildDimension() {
		return Dimension.dimension()
				.type(myModId("ember_wastes_type"))
				.noiseGenerator(myModId("ember_wastes"))
				.fixedBiome(myModId("ember_wastes"));
	}

	/* ----------------------------------------------------------
	 * 10) Register everything into a runtime pack
	 * ---------------------------------------------------------- */

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addTimeline(myModId("ember_wastes_sky_cycle"), buildSkyCycle());
		pack.addMaterialCondition(myModId("near_lava_sea"), buildNearLavaSeaCondition());
		pack.addMaterialRule(myModId("ember_wastes_surface"), buildSurfaceRule());
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
		pack.addBiome(myModId("ember_wastes"), buildBiome());
		pack.addDimensionType(myModId("ember_wastes_type"), buildDimensionType());
		pack.addStructure(myModId("obsidian_sanctum"), buildSanctumStructure());
		pack.addStructureSet(myModId("obsidian_sanctum"), buildSanctumStructureSet());
		pack.addTemplatePool(myModId("obsidian_sanctum/start_pool"), buildSanctumStartPool());
		pack.addDimension(myModId("ember_wastes"), buildDimension());
	}

	/* ----------------------------------------------------------
	 * 11) Main – dump everything
	 * ---------------------------------------------------------- */

	public static void main() {
		System.out.println("Timeline JSON (mymod:ember_wastes_sky_cycle):");
		System.out.println(JsonBytes.encodeToPrettyString(Timeline.CODEC, buildSkyCycle()));
		System.out.println("Material Condition JSON (mymod:near_lava_sea):");
		System.out.println(JsonBytes.encodeToPrettyString(MaterialCondition.CODEC, buildNearLavaSeaCondition()));
		System.out.println("Material Rule JSON (mymod:ember_wastes_surface):");
		System.out.println(JsonBytes.encodeToPrettyString(MaterialRule.CODEC, buildSurfaceRule()));
		System.out.println("Noise Settings JSON (mymod:ember_wastes):");
		System.out.println(JsonBytes.encodeToPrettyString(NoiseSettings.CODEC, buildNoiseSettings()));
		System.out.println("Carver JSON (mymod:ember_tubes):");
		System.out.println(JsonBytes.encodeToPrettyString(Carver.CODEC, buildLavaTubesCarver()));
		System.out.println("Carver JSON (mymod:ember_rifts):");
		System.out.println(JsonBytes.encodeToPrettyString(Carver.CODEC, buildRiftsCarver()));
		System.out.println("Feature JSON (mymod:ember_lava_deltas):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildLavaDeltaFeature()));
		System.out.println("Placed Feature JSON (mymod:ember_lava_deltas):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildLavaDeltaPlaced()));
		System.out.println("Feature JSON (mymod:ember_basalt_spires):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildBasaltSpiresFeature()));
		System.out.println("Placed Feature JSON (mymod:ember_basalt_spires):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildBasaltSpiresPlaced()));
		System.out.println("Feature JSON (mymod:ember_gold_pockets):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildGoldPocketFeature()));
		System.out.println("Placed Feature JSON (mymod:ember_gold_pockets):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildGoldPocketPlaced()));
		System.out.println("Feature JSON (mymod:ember_ash_scrub):");
		System.out.println(JsonBytes.encodeToPrettyString(Feature.CODEC, buildAshScrubFeature()));
		System.out.println("Placed Feature JSON (mymod:ember_ash_scrub):");
		System.out.println(JsonBytes.encodeToPrettyString(PlacedFeature.CODEC, buildAshScrubPlaced()));
		System.out.println("Biome JSON (mymod:ember_wastes):");
		System.out.println(JsonBytes.encodeToPrettyString(Biome.CODEC, buildBiome()));
		System.out.println("Dimension Type JSON (mymod:ember_wastes_type):");
		System.out.println(JsonBytes.encodeToPrettyString(DimensionType.CODEC, buildDimensionType()));
		System.out.println("Structure JSON (mymod:obsidian_sanctum):");
		System.out.println(JsonBytes.encodeToPrettyString(Structure.CODEC, buildSanctumStructure()));
		System.out.println("Structure Set JSON (mymod:obsidian_sanctum):");
		System.out.println(JsonBytes.encodeToPrettyString(StructureSet.CODEC, buildSanctumStructureSet()));
		System.out.println("Dimension JSON (mymod:ember_wastes):");
		System.out.println(JsonBytes.encodeToPrettyString(Dimension.CODEC, buildDimension()));

		// dump the whole dimension as its own datapack
		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:ember_wastes");
		pack.addDataPackMcmeta("The Ember Wastes - scorched basalt dimension, generated by ARRP");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/ember_wastes"));
	}
}
