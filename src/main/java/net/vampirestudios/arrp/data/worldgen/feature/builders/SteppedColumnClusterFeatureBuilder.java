package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public final class SteppedColumnClusterFeatureBuilder extends FeatureBuilder {
	/**
	 * Prefilled with sensible defaults for the fields the game requires:
	 * {@code column_reach}, {@code column_count}, {@code cannot_place_on},
	 * {@code can_replace} and {@code continue_through}. Note that {@code height}
	 * providers are capped at {@code 10} by the game.
	 */
	public SteppedColumnClusterFeatureBuilder() {
		super("minecraft:stepped_column_cluster");
		columnReach(IntProvider.uniform(0, 2));
		columnCount(IntProvider.uniform(1, 4));
		feature.property("cannot_place_on", "#minecraft:cannot_place_basalt_pillar_on");
		canReplace(PlacedFeature.BlockPredicate.replaceable());
		continueThrough(PlacedFeature.BlockPredicate.matchingBlocks(Identifier.withDefaultNamespace("lava")));
	}

	public SteppedColumnClusterFeatureBuilder block(Identifier block) {
		feature.property("block", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public SteppedColumnClusterFeatureBuilder canReplace(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_replace", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public SteppedColumnClusterFeatureBuilder continueThrough(PlacedFeature.BlockPredicate predicate) {
		feature.property("continue_through", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public SteppedColumnClusterFeatureBuilder cannotPlaceOn(Identifier block) {
		feature.property("cannot_place_on", block);
		return this;
	}

	public SteppedColumnClusterFeatureBuilder columnReach(IntProvider reach) {
		feature.property("column_reach", IntProvider.CODEC, reach);
		return this;
	}

	public SteppedColumnClusterFeatureBuilder columnReach(int reach) {
		return columnReach(IntProvider.constant(reach));
	}

	public SteppedColumnClusterFeatureBuilder columnCount(IntProvider count) {
		feature.property("column_count", IntProvider.CODEC, count);
		return this;
	}

	public SteppedColumnClusterFeatureBuilder clusterReach(IntProvider reach) {
		feature.property("cluster_reach", IntProvider.CODEC, reach);
		return this;
	}

	public SteppedColumnClusterFeatureBuilder height(IntProvider height) {
		feature.property("height", IntProvider.CODEC, height);
		return this;
	}
}
