package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.vampirestudios.packwright.data.worldgen.IntProvider;

public final class CountFeatureBuilder extends FeatureBuilder {
	public CountFeatureBuilder(String type) {
		super(type);
	}

	public CountFeatureBuilder count(IntProvider count) {
		feature.property("count", IntProvider.CODEC, count);
		return this;
	}
}
