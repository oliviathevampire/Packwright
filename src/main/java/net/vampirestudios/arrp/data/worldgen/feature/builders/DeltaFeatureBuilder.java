package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

public final class DeltaFeatureBuilder extends FeatureBuilder {
	public DeltaFeatureBuilder() {
		super("minecraft:delta_feature");
	}

	public DeltaFeatureBuilder contents(Identifier block) {
		feature.property("contents", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public DeltaFeatureBuilder rim(Identifier block) {
		feature.property("rim", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public DeltaFeatureBuilder size(IntProvider size) {
		feature.property("size", IntProvider.CODEC, size);
		return this;
	}

	public DeltaFeatureBuilder rimSize(IntProvider size) {
		feature.property("rim_size", IntProvider.CODEC, size);
		return this;
	}
}
