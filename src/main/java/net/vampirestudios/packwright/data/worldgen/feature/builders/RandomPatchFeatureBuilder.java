package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

public final class RandomPatchFeatureBuilder extends FeatureBuilder {
	public RandomPatchFeatureBuilder() {
		super("minecraft:random_patch");
		tries(64).xzSpread(7).ySpread(3);
	}

	public RandomPatchFeatureBuilder tries(int tries) {
		feature.property("tries", tries);
		return this;
	}

	public RandomPatchFeatureBuilder xzSpread(int spread) {
		feature.property("xz_spread", spread);
		return this;
	}

	public RandomPatchFeatureBuilder ySpread(int spread) {
		feature.property("y_spread", spread);
		return this;
	}

	public RandomPatchFeatureBuilder feature(PlacedFeature placedFeature) {
		feature.property("feature", PlacedFeature.CODEC, placedFeature);
		return this;
	}
}
