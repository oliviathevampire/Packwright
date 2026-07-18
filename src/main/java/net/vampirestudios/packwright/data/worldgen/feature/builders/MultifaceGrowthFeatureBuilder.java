package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;

import java.util.List;

/** generic multiface spreader (glow lichen, sculk vein, etc.); {@code minecraft:multiface_growth} in vanilla */
public final class MultifaceGrowthFeatureBuilder extends FeatureBuilder {
	public MultifaceGrowthFeatureBuilder(Identifier block) {
		super("minecraft:multiface_growth");
		feature.property("block", block);
		searchRange(10);
		chanceOfSpreading(0.5F);
	}

	public MultifaceGrowthFeatureBuilder searchRange(int value) {
		feature.property("search_range", value);
		return this;
	}

	public MultifaceGrowthFeatureBuilder canPlaceOnFloor(boolean value) {
		feature.property("can_place_on_floor", value);
		return this;
	}

	public MultifaceGrowthFeatureBuilder canPlaceOnCeiling(boolean value) {
		feature.property("can_place_on_ceiling", value);
		return this;
	}

	public MultifaceGrowthFeatureBuilder canPlaceOnWall(boolean value) {
		feature.property("can_place_on_wall", value);
		return this;
	}

	public MultifaceGrowthFeatureBuilder chanceOfSpreading(float value) {
		feature.property("chance_of_spreading", value);
		return this;
	}

	public MultifaceGrowthFeatureBuilder canBePlacedOn(List<Identifier> blocks) {
		feature.property("can_be_placed_on", blocks, Identifier.CODEC);
		return this;
	}

	public MultifaceGrowthFeatureBuilder canBePlacedOn(Identifier... blocks) {
		return canBePlacedOn(List.of(blocks));
	}
}
