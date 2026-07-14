package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

import java.util.ArrayList;
import java.util.List;

public final class OreFeatureBuilder extends FeatureBuilder {
	private final List<OreTarget> targets = new ArrayList<>();

	public OreFeatureBuilder() {
		this("minecraft:ore");
	}

	/** shares its config shape with {@code minecraft:ore}; used for e.g. {@code minecraft:scattered_ore} */
	public OreFeatureBuilder(String type) {
		super(type);
		discardChanceOnAirExposure(0.0F);
	}

	public OreFeatureBuilder targetTag(Identifier replaceableTag, Identifier block) {
		targets.add(new OreTarget(RuleTest.tag(replaceableTag), WorldgenBlockState.blockState(block)));
		feature.property("targets", targets, OreTarget.CODEC);
		return this;
	}

	public OreFeatureBuilder targetBlock(Identifier replaceableBlock, Identifier block) {
		targets.add(new OreTarget(RuleTest.block(replaceableBlock), WorldgenBlockState.blockState(block)));
		feature.property("targets", targets, OreTarget.CODEC);
		return this;
	}

	public OreFeatureBuilder size(int size) {
		feature.property("size", size);
		return this;
	}

	public OreFeatureBuilder discardChanceOnAirExposure(float chance) {
		feature.property("discard_chance_on_air_exposure", chance);
		return this;
	}
}
