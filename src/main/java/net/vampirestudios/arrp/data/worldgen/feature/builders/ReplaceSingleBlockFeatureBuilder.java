package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

import java.util.ArrayList;
import java.util.List;

public final class ReplaceSingleBlockFeatureBuilder extends FeatureBuilder {
	private final List<OreTarget> targets = new ArrayList<>();

	public ReplaceSingleBlockFeatureBuilder() {
		super("minecraft:replace_single_block");
	}

	public ReplaceSingleBlockFeatureBuilder targetBlock(Identifier target, Identifier state) {
		targets.add(new OreTarget(RuleTest.block(target), WorldgenBlockState.blockState(state)));
		feature.property("targets", targets, OreTarget.CODEC);
		return this;
	}

	public ReplaceSingleBlockFeatureBuilder targetTag(Identifier target, Identifier state) {
		targets.add(new OreTarget(RuleTest.tag(target), WorldgenBlockState.blockState(state)));
		feature.property("targets", targets, OreTarget.CODEC);
		return this;
	}
}
