package net.vampirestudios.arrp.data.worldgen.feature.builders;

import net.vampirestudios.arrp.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;

public final class HugeMushroomFeatureBuilder extends FeatureBuilder {
	public HugeMushroomFeatureBuilder(String type) {
		super(type);
		foliageRadius(2);
	}

	public HugeMushroomFeatureBuilder capProvider(Identifier block) {
		feature.property("cap_provider", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public HugeMushroomFeatureBuilder stemProvider(Identifier block) {
		feature.property("stem_provider", BlockStateProvider.CODEC, BlockStateProvider.simple(block));
		return this;
	}

	public HugeMushroomFeatureBuilder foliageRadius(int radius) {
		feature.property("foliage_radius", radius);
		return this;
	}

	public HugeMushroomFeatureBuilder canPlaceOn(PlacedFeature.BlockPredicate predicate) {
		feature.property("can_place_on", PlacedFeature.BlockPredicate.CODEC, predicate);
		return this;
	}
}
