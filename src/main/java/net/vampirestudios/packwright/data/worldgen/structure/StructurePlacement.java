package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.vampirestudios.packwright.data.entity.IdOrTag;

/** a {@code worldgen/structure_set} placement rule */
public sealed interface StructurePlacement permits RandomSpreadPlacement, ConcentricRingsPlacement, DimensionOriginPlacement {
	Codec<StructurePlacement> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<StructurePlacement, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "random_spread" -> RandomSpreadPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "concentric_rings" -> ConcentricRingsPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "dimension_origin" -> DimensionOriginPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported structure placement type");
			});
		}

		@Override
		public <T> DataResult<T> encode(StructurePlacement input, DynamicOps<T> ops, T prefix) {
			if (input instanceof RandomSpreadPlacement p) return RandomSpreadPlacement.CODEC.codec().encode(p, ops, prefix);
			if (input instanceof ConcentricRingsPlacement p) return ConcentricRingsPlacement.CODEC.codec().encode(p, ops, prefix);
			if (input instanceof DimensionOriginPlacement p) return DimensionOriginPlacement.CODEC.codec().encode(p, ops, prefix);
			return DataResult.error(() -> "Unsupported structure placement: " + input.getClass().getSimpleName());
		}
	};

	/** {@code minecraft:random_spread}: a spaced grid of chunks, offset by a random amount per cell */
	static RandomSpreadPlacement randomSpread(int salt, int spacing, int separation) {
		return new RandomSpreadPlacement(salt, spacing, separation);
	}

	/** {@code minecraft:concentric_rings}: rings of evenly-spaced structures around the origin */
	static ConcentricRingsPlacement concentricRings(int distance, int spread, int count) {
		return new ConcentricRingsPlacement(distance, spread, count, java.util.Optional.empty());
	}

	static ConcentricRingsPlacement concentricRings(int distance, int spread, int count, IdOrTag preferredBiomes) {
		return new ConcentricRingsPlacement(distance, spread, count, java.util.Optional.ofNullable(preferredBiomes));
	}

	/** {@code minecraft:dimension_origin}: a single structure at the dimension origin */
	static DimensionOriginPlacement dimensionOrigin() {
		return new DimensionOriginPlacement();
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
