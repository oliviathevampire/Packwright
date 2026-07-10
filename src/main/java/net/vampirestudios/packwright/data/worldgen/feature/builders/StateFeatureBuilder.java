package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

public final class StateFeatureBuilder extends FeatureBuilder {
	public StateFeatureBuilder(String type) {
		super(type);
	}

	public StateFeatureBuilder state(Identifier block) {
		feature.property("state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}
}
