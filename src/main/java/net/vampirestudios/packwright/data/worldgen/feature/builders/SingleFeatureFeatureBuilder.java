package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

/** shared shape for feature types whose only config is a single nested {@code feature} field, e.g. {@code coral_claw}/{@code coral_tree} */
public final class SingleFeatureFeatureBuilder extends FeatureBuilder {
	public SingleFeatureFeatureBuilder(String type) {
		super(type);
	}

	public SingleFeatureFeatureBuilder feature(PlacedFeature feature) {
		this.feature.property("feature", PlacedFeature.CODEC, feature);
		return this;
	}
}
