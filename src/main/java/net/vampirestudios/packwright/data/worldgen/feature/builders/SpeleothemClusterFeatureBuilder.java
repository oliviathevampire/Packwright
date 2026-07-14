package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.FloatProvider;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

import java.util.List;

/** dripstone-cave cluster generator; {@code minecraft:speleothem_cluster} in vanilla (renamed/reshaped from the old {@code dripstone_cluster}) */
public final class SpeleothemClusterFeatureBuilder extends FeatureBuilder {
	public SpeleothemClusterFeatureBuilder() {
		super("minecraft:speleothem_cluster");
	}

	public SpeleothemClusterFeatureBuilder baseBlock(Identifier block) { feature.property("base_block", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public SpeleothemClusterFeatureBuilder pointedBlock(Identifier block) { feature.property("pointed_block", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public SpeleothemClusterFeatureBuilder replaceableBlocks(List<Identifier> blocks) { feature.property("replaceable_blocks", blocks, Identifier.CODEC); return this; }
	public SpeleothemClusterFeatureBuilder replaceableBlocks(Identifier... blocks) { return replaceableBlocks(List.of(blocks)); }
	public SpeleothemClusterFeatureBuilder floorToCeilingSearchRange(int value) { feature.property("floor_to_ceiling_search_range", value); return this; }
	public SpeleothemClusterFeatureBuilder height(IntProvider value) { feature.property("height", IntProvider.CODEC, value); return this; }
	public SpeleothemClusterFeatureBuilder radius(IntProvider value) { feature.property("radius", IntProvider.CODEC, value); return this; }
	public SpeleothemClusterFeatureBuilder maxStalagmiteStalactiteHeightDiff(int value) { feature.property("max_stalagmite_stalactite_height_diff", value); return this; }
	public SpeleothemClusterFeatureBuilder heightDeviation(int value) { feature.property("height_deviation", value); return this; }
	public SpeleothemClusterFeatureBuilder speleothemBlockLayerThickness(IntProvider value) { feature.property("speleothem_block_layer_thickness", IntProvider.CODEC, value); return this; }
	public SpeleothemClusterFeatureBuilder density(FloatProvider value) { feature.property("density", FloatProvider.CODEC, value); return this; }
	public SpeleothemClusterFeatureBuilder wetness(FloatProvider value) { feature.property("wetness", FloatProvider.CODEC, value); return this; }
	public SpeleothemClusterFeatureBuilder chanceOfSpeleothemAtMaxDistanceFromCenter(float value) { feature.property("chance_of_speleothem_at_max_distance_from_center", value); return this; }
	public SpeleothemClusterFeatureBuilder maxDistanceFromEdgeAffectingChanceOfSpeleothem(int value) { feature.property("max_distance_from_edge_affecting_chance_of_speleothem", value); return this; }
	public SpeleothemClusterFeatureBuilder maxDistanceFromCenterAffectingHeightBias(int value) { feature.property("max_distance_from_center_affecting_height_bias", value); return this; }
}
