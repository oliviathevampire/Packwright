package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;

public final class BlockPileFeatureBuilder extends FeatureBuilder {
	public BlockPileFeatureBuilder() {
		super("minecraft:block_pile");
	}

	public BlockPileFeatureBuilder stateProvider(Identifier block) {
		feature.property("state_provider", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}
}
