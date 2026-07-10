package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.MapCodec;

public record ForkingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) implements TreeTrunkPlacer {
	public static final MapCodec<ForkingTrunkPlacer> CODEC = TreeTrunkPlacerCodecs.baseHeight(
			"minecraft:forking_trunk_placer",
			ForkingTrunkPlacer::new,
			ForkingTrunkPlacer::baseHeight,
			ForkingTrunkPlacer::heightRandA,
			ForkingTrunkPlacer::heightRandB
	);
}
