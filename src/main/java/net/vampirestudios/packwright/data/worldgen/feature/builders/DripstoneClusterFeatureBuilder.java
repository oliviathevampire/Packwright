package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.FloatProvider;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public final class DripstoneClusterFeatureBuilder extends FeatureBuilder {
	public DripstoneClusterFeatureBuilder() {
		super("minecraft:dripstone_cluster");
	}

	public DripstoneClusterFeatureBuilder floorToCeilingSearchRange(int value) { feature.property("floor_to_ceiling_search_range", value); return this; }
	public DripstoneClusterFeatureBuilder height(IntProvider value) { feature.property("height", IntProvider.CODEC, value); return this; }
	public DripstoneClusterFeatureBuilder radius(IntProvider value) { feature.property("radius", IntProvider.CODEC, value); return this; }
	public DripstoneClusterFeatureBuilder maxStalagmiteStalactiteHeightDiff(int value) { feature.property("max_stalagmite_stalactite_height_diff", value); return this; }
	public DripstoneClusterFeatureBuilder heightDeviation(int value) { feature.property("height_deviation", value); return this; }
	public DripstoneClusterFeatureBuilder dripstoneBlockLayerThickness(IntProvider value) { feature.property("dripstone_block_layer_thickness", IntProvider.CODEC, value); return this; }
	public DripstoneClusterFeatureBuilder density(FloatProvider value) { feature.property("density", FloatProvider.CODEC, value); return this; }
	public DripstoneClusterFeatureBuilder wetness(FloatProvider value) { feature.property("wetness", FloatProvider.CODEC, value); return this; }
	public DripstoneClusterFeatureBuilder chanceOfDripstoneColumnAtMaxDistanceFromCenter(float value) { feature.property("chance_of_dripstone_column_at_max_distance_from_center", value); return this; }
	public DripstoneClusterFeatureBuilder maxDistanceFromEdgeAffectingChanceOfDripstoneColumn(int value) { feature.property("max_distance_from_edge_affecting_chance_of_dripstone_column", value); return this; }
	public DripstoneClusterFeatureBuilder maxDistanceFromCenterAffectingHeightBias(int value) { feature.property("max_distance_from_center_affecting_height_bias", value); return this; }
}
