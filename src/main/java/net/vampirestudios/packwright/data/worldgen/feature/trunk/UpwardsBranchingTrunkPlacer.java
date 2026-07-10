package net.vampirestudios.packwright.data.worldgen.feature.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

import java.util.List;

public record UpwardsBranchingTrunkPlacer(
		int baseHeight,
		int heightRandA,
		int heightRandB,
		IntProvider extraBranchSteps,
		float placeBranchPerLogProbability,
		IntProvider extraBranchLength,
		List<Identifier> canGrowThrough
) implements TreeTrunkPlacer {
	public static final MapCodec<UpwardsBranchingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:upwards_branching_trunk_placer"),
			Codec.INT.fieldOf("base_height").forGetter(UpwardsBranchingTrunkPlacer::baseHeight),
			Codec.INT.fieldOf("height_rand_a").forGetter(UpwardsBranchingTrunkPlacer::heightRandA),
			Codec.INT.fieldOf("height_rand_b").forGetter(UpwardsBranchingTrunkPlacer::heightRandB),
			IntProvider.CODEC.fieldOf("extra_branch_steps").forGetter(UpwardsBranchingTrunkPlacer::extraBranchSteps),
			Codec.FLOAT.fieldOf("place_branch_per_log_probability").forGetter(UpwardsBranchingTrunkPlacer::placeBranchPerLogProbability),
			IntProvider.CODEC.fieldOf("extra_branch_length").forGetter(UpwardsBranchingTrunkPlacer::extraBranchLength),
			Identifier.CODEC.listOf().fieldOf("can_grow_through").forGetter(UpwardsBranchingTrunkPlacer::canGrowThrough)
	).apply(i, (type, baseHeight, heightRandA, heightRandB, extraBranchSteps, placeBranchPerLogProbability, extraBranchLength, canGrowThrough) ->
			new UpwardsBranchingTrunkPlacer(baseHeight, heightRandA, heightRandB, extraBranchSteps, placeBranchPerLogProbability, extraBranchLength, canGrowThrough)));
}
