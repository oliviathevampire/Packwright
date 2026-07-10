package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.vampirestudios.arrp.data.worldgen.IntProvider;

public final class CountFeatureBuilder extends FeatureBuilder {
	public CountFeatureBuilder(String type) {
		super(type);
	}

	public CountFeatureBuilder count(IntProvider count) {
		feature.property("count", IntProvider.CODEC, count);
		return this;
	}
}
