package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record CherryTrunkPlacer(
		int baseHeight,
		int heightRandA,
		int heightRandB,
		IntProvider branchCount,
		IntProvider branchHorizontalLength,
		IntProvider branchStartOffsetFromTop,
		IntProvider branchEndOffsetFromTop
) implements TreeTrunkPlacer {
	public static final MapCodec<CherryTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:cherry_trunk_placer"),
			Codec.INT.fieldOf("base_height").forGetter(CherryTrunkPlacer::baseHeight),
			Codec.INT.fieldOf("height_rand_a").forGetter(CherryTrunkPlacer::heightRandA),
			Codec.INT.fieldOf("height_rand_b").forGetter(CherryTrunkPlacer::heightRandB),
			IntProvider.CODEC.fieldOf("branch_count").forGetter(CherryTrunkPlacer::branchCount),
			IntProvider.CODEC.fieldOf("branch_horizontal_length").forGetter(CherryTrunkPlacer::branchHorizontalLength),
			IntProvider.CODEC.fieldOf("branch_start_offset_from_top").forGetter(CherryTrunkPlacer::branchStartOffsetFromTop),
			IntProvider.CODEC.fieldOf("branch_end_offset_from_top").forGetter(CherryTrunkPlacer::branchEndOffsetFromTop)
	).apply(i, (type, baseHeight, heightRandA, heightRandB, branchCount, branchHorizontalLength, branchStartOffsetFromTop, branchEndOffsetFromTop) ->
			new CherryTrunkPlacer(baseHeight, heightRandA, heightRandB, branchCount, branchHorizontalLength, branchStartOffsetFromTop, branchEndOffsetFromTop)));
}
