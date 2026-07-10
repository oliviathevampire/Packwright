package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;

public final class VegetationPatchFeatureBuilder extends FeatureBuilder {
	public VegetationPatchFeatureBuilder(String type) {
		super(type);
	}

	public VegetationPatchFeatureBuilder replaceableTag(Identifier tag) {
		feature.property("replaceable", "#" + tag);
		return this;
	}

	public VegetationPatchFeatureBuilder groundState(Identifier block) {
		return groundState(BlockStateProvider.simple(block));
	}

	public VegetationPatchFeatureBuilder groundState(BlockStateProvider provider) {
		feature.property("ground_state", BlockStateProvider.CODEC, provider);
		return this;
	}

	public VegetationPatchFeatureBuilder vegetationFeature(PlacedFeature placedFeature) {
		feature.property("vegetation_feature", PlacedFeature.CODEC, placedFeature);
		return this;
	}

	public VegetationPatchFeatureBuilder surface(String surface) {
		feature.property("surface", surface);
		return this;
	}

	public VegetationPatchFeatureBuilder depth(IntProvider depth) {
		feature.property("depth", IntProvider.CODEC, depth);
		return this;
	}

	public VegetationPatchFeatureBuilder extraBottomBlockChance(float chance) {
		feature.property("extra_bottom_block_chance", chance);
		return this;
	}

	public VegetationPatchFeatureBuilder verticalRange(int range) {
		feature.property("vertical_range", range);
		return this;
	}

	public VegetationPatchFeatureBuilder vegetationChance(float chance) {
		feature.property("vegetation_chance", chance);
		return this;
	}

	public VegetationPatchFeatureBuilder xzRadius(IntProvider radius) {
		feature.property("xz_radius", IntProvider.CODEC, radius);
		return this;
	}

	public VegetationPatchFeatureBuilder extraEdgeColumnChance(float chance) {
		feature.property("extra_edge_column_chance", chance);
		return this;
	}
}
