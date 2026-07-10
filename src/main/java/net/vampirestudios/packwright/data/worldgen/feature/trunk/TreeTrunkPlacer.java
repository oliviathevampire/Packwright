package net.vampirestudios.packwright.data.worldgen.feature.trunk;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

import java.util.List;

public sealed interface TreeTrunkPlacer permits
		StraightTrunkPlacer,
		FancyTrunkPlacer,
		ForkingTrunkPlacer,
		GiantTrunkPlacer,
		MegaJungleTrunkPlacer,
		DarkOakTrunkPlacer,
		BendingTrunkPlacer,
		CherryTrunkPlacer,
		UpwardsBranchingTrunkPlacer,
		PoplarTrunkPlacer {
	Codec<TreeTrunkPlacer> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<TreeTrunkPlacer, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "straight_trunk_placer" -> StraightTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "fancy_trunk_placer" -> FancyTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "forking_trunk_placer" -> ForkingTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "giant_trunk_placer" -> GiantTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "mega_jungle_trunk_placer" -> MegaJungleTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "dark_oak_trunk_placer" -> DarkOakTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "bending_trunk_placer" -> BendingTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "cherry_trunk_placer" -> CherryTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "upwards_branching_trunk_placer" -> UpwardsBranchingTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "poplar_trunk_placer" -> PoplarTrunkPlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported tree trunk placer type");
			});
		}

		@Override
		public <T> DataResult<T> encode(TreeTrunkPlacer input, DynamicOps<T> ops, T prefix) {
			if (input instanceof StraightTrunkPlacer placer) return StraightTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof FancyTrunkPlacer placer) return FancyTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof ForkingTrunkPlacer placer) return ForkingTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof GiantTrunkPlacer placer) return GiantTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof MegaJungleTrunkPlacer placer) return MegaJungleTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof DarkOakTrunkPlacer placer) return DarkOakTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof BendingTrunkPlacer placer) return BendingTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof CherryTrunkPlacer placer) return CherryTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof UpwardsBranchingTrunkPlacer placer) return UpwardsBranchingTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof PoplarTrunkPlacer placer) return PoplarTrunkPlacer.CODEC.codec().encode(placer, ops, prefix);
			return DataResult.error(() -> "Unsupported tree trunk placer: " + input.getClass().getSimpleName());
		}
	};

	static StraightTrunkPlacer straight(int baseHeight, int heightRandA, int heightRandB) {
		return new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB);
	}

	static FancyTrunkPlacer fancy(int baseHeight, int heightRandA, int heightRandB) {
		return new FancyTrunkPlacer(baseHeight, heightRandA, heightRandB);
	}

	static ForkingTrunkPlacer forking(int baseHeight, int heightRandA, int heightRandB) {
		return new ForkingTrunkPlacer(baseHeight, heightRandA, heightRandB);
	}

	static GiantTrunkPlacer giant(int baseHeight, int heightRandA, int heightRandB) {
		return new GiantTrunkPlacer(baseHeight, heightRandA, heightRandB);
	}

	static MegaJungleTrunkPlacer megaJungle(int baseHeight, int heightRandA, int heightRandB) {
		return new MegaJungleTrunkPlacer(baseHeight, heightRandA, heightRandB);
	}

	static DarkOakTrunkPlacer darkOak(int baseHeight, int heightRandA, int heightRandB) {
		return new DarkOakTrunkPlacer(baseHeight, heightRandA, heightRandB);
	}

	static BendingTrunkPlacer bending(int baseHeight, int heightRandA, int heightRandB, int minHeightForLeaves) {
		return bending(baseHeight, heightRandA, heightRandB, minHeightForLeaves, IntProvider.constant(1));
	}

	static BendingTrunkPlacer bending(int baseHeight, int heightRandA, int heightRandB, int minHeightForLeaves, IntProvider bendLength) {
		return new BendingTrunkPlacer(baseHeight, heightRandA, heightRandB, minHeightForLeaves, bendLength);
	}

	static CherryTrunkPlacer cherry() {
		return cherry(7, 1, 0);
	}

	static CherryTrunkPlacer cherry(int baseHeight, int heightRandA, int heightRandB) {
		return cherry(
				baseHeight,
				heightRandA,
				heightRandB,
				IntProvider.constant(1),
				IntProvider.uniform(2, 4),
				IntProvider.uniform(-4, -3),
				IntProvider.uniform(-1, 0)
		);
	}

	static CherryTrunkPlacer cherry(
			int baseHeight,
			int heightRandA,
			int heightRandB,
			IntProvider branchCount,
			IntProvider branchHorizontalLength,
			IntProvider branchStartOffsetFromTop,
			IntProvider branchEndOffsetFromTop
	) {
		return new CherryTrunkPlacer(baseHeight, heightRandA, heightRandB, branchCount, branchHorizontalLength, branchStartOffsetFromTop, branchEndOffsetFromTop);
	}

	static UpwardsBranchingTrunkPlacer upwardsBranching(int baseHeight, int heightRandA, int heightRandB) {
		return upwardsBranching(
				baseHeight,
				heightRandA,
				heightRandB,
				IntProvider.uniform(1, 4),
				0.5F,
				IntProvider.uniform(0, 1),
				List.of()
		);
	}

	static UpwardsBranchingTrunkPlacer upwardsBranching(
			int baseHeight,
			int heightRandA,
			int heightRandB,
			IntProvider extraBranchSteps,
			float placeBranchPerLogProbability,
			IntProvider extraBranchLength,
			List<Identifier> canGrowThrough
	) {
		return new UpwardsBranchingTrunkPlacer(baseHeight, heightRandA, heightRandB, extraBranchSteps, placeBranchPerLogProbability, extraBranchLength, canGrowThrough);
	}

	static UpwardsBranchingTrunkPlacer up(int baseHeight, int heightRandA, int heightRandB) {
		return upwardsBranching(baseHeight, heightRandA, heightRandB);
	}

	static PoplarTrunkPlacer poplar(IntProvider trunkHeightAboveBranches, IntProvider branchAmount) {
		return poplar(7, 4, 0, trunkHeightAboveBranches, branchAmount);
	}

	static PoplarTrunkPlacer poplar(int baseHeight, int heightRandA, int heightRandB, IntProvider trunkHeightAboveBranches, IntProvider branchAmount) {
		return new PoplarTrunkPlacer(baseHeight, heightRandA, heightRandB, trunkHeightAboveBranches, branchAmount);
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
