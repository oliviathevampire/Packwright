package net.vampirestudios.packwright.data.worldgen.feature.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record PoplarTrunkPlacer(
		int baseHeight,
		int heightRandA,
		int heightRandB,
		IntProvider trunkHeightAboveBranches,
		IntProvider branchAmount
) implements TreeTrunkPlacer {
	public static final MapCodec<PoplarTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:poplar_trunk_placer"),
			Codec.INT.fieldOf("base_height").forGetter(PoplarTrunkPlacer::baseHeight),
			Codec.INT.fieldOf("height_rand_a").forGetter(PoplarTrunkPlacer::heightRandA),
			Codec.INT.fieldOf("height_rand_b").forGetter(PoplarTrunkPlacer::heightRandB),
			IntProvider.CODEC.fieldOf("trunk_height_above_branches").forGetter(PoplarTrunkPlacer::trunkHeightAboveBranches),
			IntProvider.CODEC.fieldOf("branch_amount").forGetter(PoplarTrunkPlacer::branchAmount)
	).apply(i, (type, baseHeight, heightRandA, heightRandB, trunkHeightAboveBranches, branchAmount) ->
			new PoplarTrunkPlacer(baseHeight, heightRandA, heightRandB, trunkHeightAboveBranches, branchAmount)));
}
