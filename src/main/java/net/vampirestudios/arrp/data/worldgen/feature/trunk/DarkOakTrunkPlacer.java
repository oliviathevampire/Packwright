package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.MapCodec;

public record DarkOakTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) implements TreeTrunkPlacer {
	public static final MapCodec<DarkOakTrunkPlacer> CODEC = TreeTrunkPlacerCodecs.baseHeight(
			"minecraft:dark_oak_trunk_placer",
			DarkOakTrunkPlacer::new,
			DarkOakTrunkPlacer::baseHeight,
			DarkOakTrunkPlacer::heightRandA,
			DarkOakTrunkPlacer::heightRandB
	);
}
