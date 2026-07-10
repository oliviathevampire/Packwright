package net.vampirestudios.packwright.data.worldgen.feature.trunk;

import com.mojang.serialization.MapCodec;

public record FancyTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) implements TreeTrunkPlacer {
	public static final MapCodec<FancyTrunkPlacer> CODEC = TreeTrunkPlacerCodecs.baseHeight(
			"minecraft:fancy_trunk_placer",
			FancyTrunkPlacer::new,
			FancyTrunkPlacer::baseHeight,
			FancyTrunkPlacer::heightRandA,
			FancyTrunkPlacer::heightRandB
	);
}
