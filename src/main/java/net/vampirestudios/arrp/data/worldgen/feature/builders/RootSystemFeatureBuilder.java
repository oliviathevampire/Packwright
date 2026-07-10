package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;

public final class RootSystemFeatureBuilder extends FeatureBuilder {
	public RootSystemFeatureBuilder() {
		super("minecraft:root_system");
	}

	public RootSystemFeatureBuilder feature(PlacedFeature feature) {
		this.feature.property("feature", PlacedFeature.CODEC, feature);
		return this;
	}

	public RootSystemFeatureBuilder requiredVerticalSpaceForTree(int value) { feature.property("required_vertical_space_for_tree", value); return this; }
	public RootSystemFeatureBuilder rootRadius(int value) { feature.property("root_radius", value); return this; }
	public RootSystemFeatureBuilder rootReplaceableTag(Identifier tag) { feature.property("root_replaceable", "#" + tag); return this; }
	public RootSystemFeatureBuilder rootStateProvider(BlockStateProvider provider) { feature.property("root_state_provider", BlockStateProvider.CODEC, provider); return this; }
	public RootSystemFeatureBuilder rootPlacementAttempts(int value) { feature.property("root_placement_attempts", value); return this; }
	public RootSystemFeatureBuilder rootColumnMaxHeight(int value) { feature.property("root_column_max_height", value); return this; }
	public RootSystemFeatureBuilder hangingRootRadius(int value) { feature.property("hanging_root_radius", value); return this; }
	public RootSystemFeatureBuilder hangingRootsVerticalSpan(int value) { feature.property("hanging_roots_vertical_span", value); return this; }
	public RootSystemFeatureBuilder hangingRootStateProvider(BlockStateProvider provider) { feature.property("hanging_root_state_provider", BlockStateProvider.CODEC, provider); return this; }
	public RootSystemFeatureBuilder hangingRootPlacementAttempts(int value) { feature.property("hanging_root_placement_attempts", value); return this; }
	public RootSystemFeatureBuilder allowedVerticalWaterForTree(int value) { feature.property("allowed_vertical_water_for_tree", value); return this; }
	public RootSystemFeatureBuilder allowedTreePosition(PlacedFeature.BlockPredicate predicate) { feature.property("allowed_tree_position", PlacedFeature.BlockPredicate.CODEC, predicate); return this; }
	public RootSystemFeatureBuilder rootColumnHeight(IntProvider value) { feature.property("root_column_height", IntProvider.CODEC, value); return this; }
}
