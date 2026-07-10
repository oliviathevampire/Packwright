package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

import java.util.List;

public final class SpringFeatureBuilder extends FeatureBuilder {
	public SpringFeatureBuilder() {
		super("minecraft:spring_feature");
		requiresBlockBelow(true).rockCount(4).holeCount(1).validBlocks(List.of(Identifier.withDefaultNamespace("stone")));
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
