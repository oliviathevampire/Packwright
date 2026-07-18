package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

/** the ice-spike-tower feature; {@code minecraft:spike} in vanilla (formerly {@code ice_spike}) */
public final class SpikeFeatureBuilder extends FeatureBuilder {
	public SpikeFeatureBuilder() {
		super("minecraft:spike");
	}

	public SpikeFeatureBuilder state(Identifier block) {
		feature.property("state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public SpikeFeatureBuilder canPlaceOn(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_place_on", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public SpikeFeatureBuilder canReplace(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_replace", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}
}
