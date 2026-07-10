package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.MapCodec;

public record StraightTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) implements TreeTrunkPlacer {
	public static final MapCodec<StraightTrunkPlacer> CODEC = TreeTrunkPlacerCodecs.baseHeight(
			"minecraft:straight_trunk_placer",
			StraightTrunkPlacer::new,
			StraightTrunkPlacer::baseHeight,
			StraightTrunkPlacer::heightRandA,
			StraightTrunkPlacer::heightRandB
	);
}
