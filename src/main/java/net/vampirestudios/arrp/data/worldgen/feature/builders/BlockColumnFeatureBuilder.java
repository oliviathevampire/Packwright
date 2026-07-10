package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.arrp.data.worldgen.feature.BlockColumnLayer;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

public final class BlockColumnFeatureBuilder extends FeatureBuilder {
	private final List<BlockColumnLayer> layers = new ArrayList<>();

	public BlockColumnFeatureBuilder() {
		super("minecraft:block_column");
	}

	public BlockColumnFeatureBuilder direction(String direction) {
		feature.property("direction", direction);
		return this;
	}

	public BlockColumnFeatureBuilder allowedPlacement(PlacedFeature.BlockPredicate predicate) {
		feature.property("allowed_placement", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public BlockColumnFeatureBuilder prioritizeTip(boolean prioritizeTip) {
		feature.property("prioritize_tip", prioritizeTip);
		return this;
	}

	public BlockColumnFeatureBuilder layer(IntProvider height, BlockStateProvider provider) {
		layers.add(new BlockColumnLayer(height, provider));
		feature.property("layers", layers, BlockColumnLayer.CODEC);
		return this;
	}
}
