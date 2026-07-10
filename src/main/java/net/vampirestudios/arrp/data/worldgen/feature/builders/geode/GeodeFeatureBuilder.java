package net.vampirestudios.arrp.data.worldgen.feature.builders.geode;

import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;
import net.vampirestudios.arrp.data.worldgen.feature.builders.FeatureBuilder;

import java.util.List;

public final class GeodeFeatureBuilder extends FeatureBuilder {
	public GeodeFeatureBuilder() {
		super("minecraft:geode");
	}

	public GeodeFeatureBuilder blocks(Identifier filling, Identifier innerLayer, Identifier alternateInnerLayer, Identifier middleLayer, Identifier outerLayer) {
		feature.property("blocks", GeodeBlocks.CODEC, new GeodeBlocks(
				BlockStateProvider.simple(filling),
				BlockStateProvider.simple(innerLayer),
				BlockStateProvider.simple(alternateInnerLayer),
				BlockStateProvider.simple(middleLayer),
				BlockStateProvider.simple(outerLayer),
				List.of()
		));
		return this;
	}

	public GeodeFeatureBuilder layers(double filling, double innerLayer, double middleLayer, double outerLayer) {
		feature.property("layers", GeodeLayers.CODEC, new GeodeLayers(filling, innerLayer, middleLayer, outerLayer));
		return this;
	}

	public GeodeFeatureBuilder crack(double generateCrackChance, double baseCrackSize, int crackPointOffset) {
		feature.property("crack", GeodeCrack.CODEC, new GeodeCrack(generateCrackChance, baseCrackSize, crackPointOffset));
		return this;
	}

	public GeodeFeatureBuilder usePotentialPlacementsChance(double value) { feature.property("use_potential_placements_chance", (float) value); return this; }
	public GeodeFeatureBuilder useAlternateLayer0Chance(double value) { feature.property("use_alternate_layer0_chance", (float) value); return this; }
	public GeodeFeatureBuilder placementsRequireLayer0Alternate(boolean value) { feature.property("placements_require_layer0_alternate", value); return this; }
	public GeodeFeatureBuilder outerWallDistance(IntProvider value) { feature.property("outer_wall_distance", IntProvider.CODEC, value); return this; }
	public GeodeFeatureBuilder distributionPoints(IntProvider value) { feature.property("distribution_points", IntProvider.CODEC, value); return this; }
	public GeodeFeatureBuilder pointOffset(IntProvider value) { feature.property("point_offset", IntProvider.CODEC, value); return this; }
	public GeodeFeatureBuilder minGenOffset(int value) { feature.property("min_gen_offset", value); return this; }
	public GeodeFeatureBuilder maxGenOffset(int value) { feature.property("max_gen_offset", value); return this; }
	public GeodeFeatureBuilder invalidBlocksThreshold(int value) { feature.property("invalid_blocks_threshold", value); return this; }
}
