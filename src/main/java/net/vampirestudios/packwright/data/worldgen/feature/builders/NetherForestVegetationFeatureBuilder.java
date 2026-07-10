package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;

public final class NetherForestVegetationFeatureBuilder extends FeatureBuilder {
	public NetherForestVegetationFeatureBuilder() {
		super("minecraft:nether_forest_vegetation");
	}

	public NetherForestVegetationFeatureBuilder stateProvider(Identifier block) {
		feature.property("state_provider", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public NetherForestVegetationFeatureBuilder spreadWidth(int width) {
		feature.property("spread_width", width);
		return this;
	}

	public NetherForestVegetationFeatureBuilder spreadHeight(int height) {
		feature.property("spread_height", height);
		return this;
	}
}
