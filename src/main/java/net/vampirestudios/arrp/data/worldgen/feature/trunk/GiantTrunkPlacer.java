package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.MapCodec;

public record GiantTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) implements TreeTrunkPlacer {
	public static final MapCodec<GiantTrunkPlacer> CODEC = TreeTrunkPlacerCodecs.baseHeight(
			"minecraft:giant_trunk_placer",
			GiantTrunkPlacer::new,
			GiantTrunkPlacer::baseHeight,
			GiantTrunkPlacer::heightRandA,
			GiantTrunkPlacer::heightRandB
	);
}
