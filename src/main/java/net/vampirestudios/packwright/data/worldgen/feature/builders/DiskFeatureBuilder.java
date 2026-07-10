package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public final class DiskFeatureBuilder extends FeatureBuilder {
	public DiskFeatureBuilder() {
		super("minecraft:disk");
		halfHeight(1);
	}

	public DiskFeatureBuilder stateProvider(Identifier block) {
		feature.property("state_provider", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public DiskFeatureBuilder targetBlock(Identifier block) {
		feature.property("target", RuleTest.CODEC, RuleTest.block(block));
		return this;
	}

	public DiskFeatureBuilder targetTag(Identifier tag) {
		feature.property("target", RuleTest.CODEC, RuleTest.tag(tag));
		return this;
	}

	public DiskFeatureBuilder radius(int radius) {
		return radius(IntProvider.constant(radius));
	}

	public DiskFeatureBuilder radius(IntProvider radius) {
		feature.property("radius", IntProvider.CODEC, radius);
		return this;
	}

	public DiskFeatureBuilder halfHeight(int halfHeight) {
		feature.property("half_height", halfHeight);
		return this;
	}
}
