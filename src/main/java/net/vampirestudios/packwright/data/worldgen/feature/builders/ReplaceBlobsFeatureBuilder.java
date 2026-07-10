package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

public final class ReplaceBlobsFeatureBuilder extends FeatureBuilder {
	public ReplaceBlobsFeatureBuilder() {
		super("minecraft:replace_blobs");
	}

	public ReplaceBlobsFeatureBuilder target(Identifier block) {
		feature.property("target", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public ReplaceBlobsFeatureBuilder state(Identifier block) {
		feature.property("state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block));
		return this;
	}

	public ReplaceBlobsFeatureBuilder radius(int radius) {
		return radius(IntProvider.constant(radius));
	}

	public ReplaceBlobsFeatureBuilder radius(IntProvider radius) {
		feature.property("radius", IntProvider.CODEC, radius);
		return this;
	}
}
