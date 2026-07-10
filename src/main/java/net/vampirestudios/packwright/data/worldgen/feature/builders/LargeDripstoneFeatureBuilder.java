package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.FloatProvider;

public final class LargeDripstoneFeatureBuilder extends FeatureBuilder {
	public LargeDripstoneFeatureBuilder() {
		super("minecraft:large_dripstone");
	}

	public LargeDripstoneFeatureBuilder floorToCeilingSearchRange(int value) { feature.property("floor_to_ceiling_search_range", value); return this; }
	public LargeDripstoneFeatureBuilder columnRadius(FloatProvider value) { feature.property("column_radius", FloatProvider.CODEC, value); return this; }
	public LargeDripstoneFeatureBuilder heightScale(FloatProvider value) { feature.property("height_scale", FloatProvider.CODEC, value); return this; }
	public LargeDripstoneFeatureBuilder maxColumnRadiusToCaveHeightRatio(float value) { feature.property("max_column_radius_to_cave_height_ratio", value); return this; }
	public LargeDripstoneFeatureBuilder stalactiteBluntness(FloatProvider value) { feature.property("stalactite_bluntness", FloatProvider.CODEC, value); return this; }
	public LargeDripstoneFeatureBuilder stalagmiteBluntness(FloatProvider value) { feature.property("stalagmite_bluntness", FloatProvider.CODEC, value); return this; }
	public LargeDripstoneFeatureBuilder windSpeed(FloatProvider value) { feature.property("wind_speed", FloatProvider.CODEC, value); return this; }
	public LargeDripstoneFeatureBuilder minRadiusForWind(float value) { feature.property("min_radius_for_wind", value); return this; }
	public LargeDripstoneFeatureBuilder minBluntnessForWind(float value) { feature.property("min_bluntness_for_wind", value); return this; }
}
