package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;

public final class SingleBlockPillarFeatureBuilder extends FeatureBuilder {
	public SingleBlockPillarFeatureBuilder() {
		super("minecraft:single_block_pillar");
	}

	public SingleBlockPillarFeatureBuilder block(Identifier block) {
		feature.property("block", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public SingleBlockPillarFeatureBuilder canReplace(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_replace", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}

	public SingleBlockPillarFeatureBuilder direction(String direction) {
		feature.property("direction", direction);
		return this;
	}

	public SingleBlockPillarFeatureBuilder chanceToContinue(float chance) {
		feature.property("chance_to_continue", chance);
		return this;
	}

	public SingleBlockPillarFeatureBuilder capFeature(PlacedFeature feature) {
		this.feature.property("cap_feature", PlacedFeature.CODEC, feature);
		return this;
	}
}
