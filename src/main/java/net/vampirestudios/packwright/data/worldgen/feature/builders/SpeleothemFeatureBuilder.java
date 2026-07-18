package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

import java.util.List;

/** a single stalactite/stalagmite formation; {@code minecraft:speleothem} in vanilla */
public final class SpeleothemFeatureBuilder extends FeatureBuilder {
	public SpeleothemFeatureBuilder() {
		super("minecraft:speleothem");
		chanceOfTallerGeneration(0.2F);
		chanceOfDirectionalSpread(0.7F);
		chanceOfSpreadRadius2(0.5F);
		chanceOfSpreadRadius3(0.5F);
	}

	public SpeleothemFeatureBuilder baseBlock(Identifier block) { feature.property("base_block", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public SpeleothemFeatureBuilder pointedBlock(Identifier block) { feature.property("pointed_block", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public SpeleothemFeatureBuilder replaceableBlocks(List<Identifier> blocks) { feature.property("replaceable_blocks", blocks, Identifier.CODEC); return this; }
	public SpeleothemFeatureBuilder replaceableBlocks(Identifier... blocks) { return replaceableBlocks(List.of(blocks)); }
	public SpeleothemFeatureBuilder chanceOfTallerGeneration(float value) { feature.property("chance_of_taller_generation", value); return this; }
	public SpeleothemFeatureBuilder chanceOfDirectionalSpread(float value) { feature.property("chance_of_directional_spread", value); return this; }
	public SpeleothemFeatureBuilder chanceOfSpreadRadius2(float value) { feature.property("chance_of_spread_radius2", value); return this; }
	public SpeleothemFeatureBuilder chanceOfSpreadRadius3(float value) { feature.property("chance_of_spread_radius3", value); return this; }
}
