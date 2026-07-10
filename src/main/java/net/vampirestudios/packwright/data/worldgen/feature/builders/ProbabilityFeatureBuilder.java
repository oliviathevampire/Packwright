package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

public final class ProbabilityFeatureBuilder extends FeatureBuilder {
	public ProbabilityFeatureBuilder(String type) {
		super(type);
	}

	public ProbabilityFeatureBuilder probability(float probability) {
		feature.property("probability", probability);
		return this;
	}
}
