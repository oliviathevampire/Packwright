package test;

import com.google.gson.JsonPrimitive;
import net.vampirestudios.arrp.json.JsonBytes;
import net.vampirestudios.arrp.json.timeline.JTimeline;
import net.vampirestudios.arrp.json.worldgen.JAttributeValue;
import net.vampirestudios.arrp.json.worldgen.biome.JBiome;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimension;
import net.vampirestudios.arrp.json.worldgen.dimension.JDimensionType;
import net.vampirestudios.arrp.json.worldgen.feature.JConfiguredFeature;
import net.vampirestudios.arrp.json.worldgen.feature.JPlacedFeature;
import net.vampirestudios.arrp.json.worldgen.noise.JNoiseSettings;
import net.vampirestudios.arrp.json.worldgen.structure.JStructure;
import net.vampirestudios.arrp.json.worldgen.structure.JStructureSet;
import net.minecraft.tags.BlockTags;

import java.util.HashMap;

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
				.generation(new JBiome.Generation());
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
		return JConfiguredFeature
				.tree("minecraft:oak_log", "minecraft:oak_leaves")
				.ignoreVines(true);
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
		dumpSkyRuinStructureJson();
		dumpSkyRuinStructureSetJson();
	}
}
