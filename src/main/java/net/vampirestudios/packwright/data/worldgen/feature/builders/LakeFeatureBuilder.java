package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

public final class LakeFeatureBuilder extends FeatureBuilder {
	public LakeFeatureBuilder() {
		super("minecraft:lake");
	}

	public LakeFeatureBuilder fluid(Identifier block) {
		feature.property("fluid", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public LakeFeatureBuilder barrier(Identifier block) {
		feature.property("barrier", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}
}
