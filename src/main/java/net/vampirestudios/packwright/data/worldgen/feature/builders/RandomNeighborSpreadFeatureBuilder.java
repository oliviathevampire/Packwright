package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;

import java.util.List;

public final class RandomNeighborSpreadFeatureBuilder extends FeatureBuilder {
	public RandomNeighborSpreadFeatureBuilder() {
		super("minecraft:random_neighbor_spread");
	}

	public RandomNeighborSpreadFeatureBuilder block(Identifier block) {
		feature.property("block", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public RandomNeighborSpreadFeatureBuilder acceptedNeighbors(Identifier... blocks) {
		feature.property("accepted_neighbors", List.of(blocks));
		return this;
	}

	public RandomNeighborSpreadFeatureBuilder acceptedNeighborTag(Identifier tag) {
		feature.property("accepted_neighbors", "#" + tag);
		return this;
	}

	public RandomNeighborSpreadFeatureBuilder canReplace(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_replace", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public RandomNeighborSpreadFeatureBuilder attempts(IntProvider attempts) {
		feature.property("attempts", IntProvider.CODEC, attempts);
		return this;
	}

	public RandomNeighborSpreadFeatureBuilder xzOffset(IntProvider offset) {
		feature.property("xz_offset", IntProvider.CODEC, offset);
		return this;
	}

	public RandomNeighborSpreadFeatureBuilder yOffset(IntProvider offset) {
		feature.property("y_offset", IntProvider.CODEC, offset);
		return this;
	}
}
