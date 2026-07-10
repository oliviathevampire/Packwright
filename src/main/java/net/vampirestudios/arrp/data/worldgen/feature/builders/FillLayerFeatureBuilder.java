package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

public final class FillLayerFeatureBuilder extends FeatureBuilder {
	public FillLayerFeatureBuilder() {
		super("minecraft:fill_layer");
	}

	public FillLayerFeatureBuilder height(int height) {
		feature.property("height", height);
		return this;
	}

	public FillLayerFeatureBuilder state(Identifier block) {
		feature.property("state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}
}
