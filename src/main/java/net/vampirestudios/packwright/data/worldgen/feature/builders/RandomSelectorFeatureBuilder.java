package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import java.util.ArrayList;
import java.util.List;

public final class RandomSelectorFeatureBuilder extends FeatureBuilder {
	private final List<WeightedPlacedFeature> features = new ArrayList<>();

	public RandomSelectorFeatureBuilder() {
		super("minecraft:random_selector");
	}

	public RandomSelectorFeatureBuilder feature(PlacedFeature feature, float chance) {
		features.add(new WeightedPlacedFeature(feature, chance));
		this.feature.property("features", features, WeightedPlacedFeature.CODEC);
		return this;
	}

	public RandomSelectorFeatureBuilder defaultFeature(PlacedFeature feature) {
		this.feature.property("default", PlacedFeature.CODEC, feature);
		return this;
	}
}
