package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

public final class FeatureListFeatureBuilder extends FeatureBuilder {
	private final List<PlacedFeature> features = new ArrayList<>();

	public FeatureListFeatureBuilder(String type) {
		super(type);
	}

	public FeatureListFeatureBuilder feature(PlacedFeature feature) {
		this.features.add(feature);
		this.feature.property("features", this.features, PlacedFeature.CODEC);
		return this;
	}

	public FeatureListFeatureBuilder defaultFeature(PlacedFeature feature) {
		this.feature.property("default", PlacedFeature.CODEC, feature);
		return this;
	}

	public FeatureListFeatureBuilder featureTrue(PlacedFeature feature) {
		this.feature.property("feature_true", PlacedFeature.CODEC, feature);
		return this;
	}

	public FeatureListFeatureBuilder featureFalse(PlacedFeature feature) {
		this.feature.property("feature_false", PlacedFeature.CODEC, feature);
		return this;
	}
}
