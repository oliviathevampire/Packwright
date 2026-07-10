package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.MapCodec;

public record MegaJungleTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) implements TreeTrunkPlacer {
	public static final MapCodec<MegaJungleTrunkPlacer> CODEC = TreeTrunkPlacerCodecs.baseHeight(
			"minecraft:mega_jungle_trunk_placer",
			MegaJungleTrunkPlacer::new,
			MegaJungleTrunkPlacer::baseHeight,
			MegaJungleTrunkPlacer::heightRandA,
			MegaJungleTrunkPlacer::heightRandB
	);
}
