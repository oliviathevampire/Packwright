package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

public final class UnderwaterMagmaFeatureBuilder extends FeatureBuilder {
	public UnderwaterMagmaFeatureBuilder() {
		super("minecraft:underwater_magma");
	}

	public UnderwaterMagmaFeatureBuilder floorSearchRange(int value) {
		feature.property("floor_search_range", value);
		return this;
	}

	public UnderwaterMagmaFeatureBuilder placementRadiusAroundFloor(int value) {
		feature.property("placement_radius_around_floor", value);
		return this;
	}

	public UnderwaterMagmaFeatureBuilder placementProbabilityPerValidPosition(float value) {
		feature.property("placement_probability_per_valid_position", value);
		return this;
	}
}
