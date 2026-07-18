package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

public final class BlockBlobFeatureBuilder extends FeatureBuilder {
	public BlockBlobFeatureBuilder() {
		super("minecraft:block_blob");
	}

	public BlockBlobFeatureBuilder state(Identifier block) {
		feature.property("state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public BlockBlobFeatureBuilder canPlaceOn(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_place_on", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}
}
