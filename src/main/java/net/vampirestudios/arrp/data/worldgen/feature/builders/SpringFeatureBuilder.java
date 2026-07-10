package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.util.VanillaIds;

import java.util.List;

public final class SpringFeatureBuilder extends FeatureBuilder {
	public SpringFeatureBuilder() {
		super("minecraft:spring_feature");
		requiresBlockBelow(true).rockCount(4).holeCount(1).validBlocks(List.of(VanillaIds.STONE));
	}

	public SpringFeatureBuilder state(Identifier block) {
		feature.property("state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public SpringFeatureBuilder requiresBlockBelow(boolean value) {
		feature.property("requires_block_below", value);
		return this;
	}

	public SpringFeatureBuilder rockCount(int value) {
		feature.property("rock_count", value);
		return this;
	}

	public SpringFeatureBuilder holeCount(int value) {
		feature.property("hole_count", value);
		return this;
	}

	public SpringFeatureBuilder validBlocks(List<Identifier> blocks) {
		feature.property("valid_blocks", blocks, Identifier.CODEC);
		return this;
	}
}
