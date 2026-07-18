package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public sealed interface TreeFoliagePlacer permits
		BlobFoliagePlacer,
		FancyFoliagePlacer,
		SpruceFoliagePlacer,
		PineFoliagePlacer,
		AcaciaFoliagePlacer,
		BushFoliagePlacer,
		MegaJungleFoliagePlacer,
		MegaPineFoliagePlacer,
		DarkOakFoliagePlacer,
		RandomSpreadFoliagePlacer,
		CherryFoliagePlacer,
		PoplarFoliagePlacer {
	Codec<TreeFoliagePlacer> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<TreeFoliagePlacer, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "blob_foliage_placer" -> BlobFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "fancy_foliage_placer" -> FancyFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "spruce_foliage_placer" -> SpruceFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "pine_foliage_placer" -> PineFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "acacia_foliage_placer" -> AcaciaFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "bush_foliage_placer" -> BushFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "jungle_foliage_placer" -> MegaJungleFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "mega_pine_foliage_placer" -> MegaPineFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "dark_oak_foliage_placer" -> DarkOakFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "random_spread_foliage_placer" -> RandomSpreadFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "cherry_foliage_placer" -> CherryFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "poplar_foliage_placer" -> PoplarFoliagePlacer.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported tree foliage placer type");
			});
		}

		@Override
		public <T> DataResult<T> encode(TreeFoliagePlacer input, DynamicOps<T> ops, T prefix) {
			if (input instanceof BlobFoliagePlacer placer) return BlobFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof FancyFoliagePlacer placer) return FancyFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof SpruceFoliagePlacer placer) return SpruceFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof PineFoliagePlacer placer) return PineFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof AcaciaFoliagePlacer placer) return AcaciaFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof BushFoliagePlacer placer) return BushFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof MegaJungleFoliagePlacer placer) return MegaJungleFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof MegaPineFoliagePlacer placer) return MegaPineFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof DarkOakFoliagePlacer placer) return DarkOakFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof RandomSpreadFoliagePlacer placer) return RandomSpreadFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof CherryFoliagePlacer placer) return CherryFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			if (input instanceof PoplarFoliagePlacer placer) return PoplarFoliagePlacer.CODEC.codec().encode(placer, ops, prefix);
			return DataResult.error(() -> "Unsupported tree foliage placer: " + input.getClass().getSimpleName());
		}
	};

	static BlobFoliagePlacer blob(IntProvider radius, IntProvider offset, int height) {
		return new BlobFoliagePlacer(radius, offset, height);
	}

	static FancyFoliagePlacer fancy(IntProvider radius, IntProvider offset, int height) {
		return new FancyFoliagePlacer(radius, offset, height);
	}

	static SpruceFoliagePlacer spruce(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
		return new SpruceFoliagePlacer(radius, offset, trunkHeight);
	}

	static PineFoliagePlacer pine(IntProvider radius, IntProvider offset, IntProvider height) {
		return new PineFoliagePlacer(radius, offset, height);
	}

	static AcaciaFoliagePlacer acacia(IntProvider radius, IntProvider offset) {
		return new AcaciaFoliagePlacer(radius, offset);
	}

	static BushFoliagePlacer bush(IntProvider radius, IntProvider offset, int height) {
		return new BushFoliagePlacer(radius, offset, height);
	}

	static MegaJungleFoliagePlacer megaJungle(IntProvider radius, IntProvider offset, int height) {
		return new MegaJungleFoliagePlacer(radius, offset, height);
	}

	static MegaPineFoliagePlacer megaPine(IntProvider radius, IntProvider offset, IntProvider crownHeight) {
		return new MegaPineFoliagePlacer(radius, offset, crownHeight);
	}

	static DarkOakFoliagePlacer darkOak(IntProvider radius, IntProvider offset) {
		return new DarkOakFoliagePlacer(radius, offset);
	}

	static RandomSpreadFoliagePlacer randomSpread(IntProvider radius, IntProvider offset, int foliageHeight, int leafPlacementAttempts) {
		return new RandomSpreadFoliagePlacer(radius, offset, foliageHeight, leafPlacementAttempts);
	}

	static CherryFoliagePlacer cherry(IntProvider radius, IntProvider offset, int height, float wideBottomLayerHoleChance, float cornerHoleChance, float hangingLeavesChance, float hangingLeavesExtensionChance) {
		return cherry(radius, offset, IntProvider.constant(height), wideBottomLayerHoleChance, cornerHoleChance, hangingLeavesChance, hangingLeavesExtensionChance);
	}

	static CherryFoliagePlacer cherry(IntProvider radius, IntProvider offset, IntProvider height, float wideBottomLayerHoleChance, float cornerHoleChance, float hangingLeavesChance, float hangingLeavesExtensionChance) {
		return new CherryFoliagePlacer(radius, offset, height, wideBottomLayerHoleChance, cornerHoleChance, hangingLeavesChance, hangingLeavesExtensionChance);
	}

	static PoplarFoliagePlacer poplar(IntProvider height, float sideHoleChance) {
		return poplar(IntProvider.constant(0), IntProvider.constant(0), height, sideHoleChance);
	}

	static PoplarFoliagePlacer poplar(IntProvider radius, IntProvider offset, IntProvider height, float sideHoleChance) {
		return new PoplarFoliagePlacer(radius, offset, height, sideHoleChance);
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
