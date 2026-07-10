package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record BendingTrunkPlacer(
		int baseHeight,
		int heightRandA,
		int heightRandB,
		int minHeightForLeaves,
		IntProvider bendLength
) implements TreeTrunkPlacer {
	public static final MapCodec<BendingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:bending_trunk_placer"),
			Codec.INT.fieldOf("base_height").forGetter(BendingTrunkPlacer::baseHeight),
			Codec.INT.fieldOf("height_rand_a").forGetter(BendingTrunkPlacer::heightRandA),
			Codec.INT.fieldOf("height_rand_b").forGetter(BendingTrunkPlacer::heightRandB),
			Codec.INT.fieldOf("min_height_for_leaves").forGetter(BendingTrunkPlacer::minHeightForLeaves),
			IntProvider.CODEC.fieldOf("bend_length").forGetter(BendingTrunkPlacer::bendLength)
	).apply(i, (type, baseHeight, heightRandA, heightRandB, minHeightForLeaves, bendLength) ->
			new BendingTrunkPlacer(baseHeight, heightRandA, heightRandB, minHeightForLeaves, bendLength)));
}
