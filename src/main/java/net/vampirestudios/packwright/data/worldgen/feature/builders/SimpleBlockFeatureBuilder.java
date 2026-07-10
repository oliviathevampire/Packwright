package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;

public final class SimpleBlockFeatureBuilder extends FeatureBuilder {
	public SimpleBlockFeatureBuilder() {
		super("minecraft:simple_block");
	}

	public SimpleBlockFeatureBuilder toPlace(Identifier block) {
		feature.property("to_place", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}
}
