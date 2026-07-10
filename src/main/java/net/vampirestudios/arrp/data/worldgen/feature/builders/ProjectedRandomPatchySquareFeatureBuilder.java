package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;

public final class ProjectedRandomPatchySquareFeatureBuilder extends FeatureBuilder {
	public ProjectedRandomPatchySquareFeatureBuilder() {
		super("minecraft:projected_random_patchy_square");
	}

	public ProjectedRandomPatchySquareFeatureBuilder block(Identifier block) {
		feature.property("block", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public ProjectedRandomPatchySquareFeatureBuilder projectThrough(PlacedFeature.BlockPredicate predicate) {
		feature.property("project_through", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public ProjectedRandomPatchySquareFeatureBuilder size(IntProvider size) {
		feature.property("size", IntProvider.CODEC, size);
		return this;
	}

	public ProjectedRandomPatchySquareFeatureBuilder maxProjectionHeight(int height) {
		feature.property("max_projection_height", height);
		return this;
	}
}
