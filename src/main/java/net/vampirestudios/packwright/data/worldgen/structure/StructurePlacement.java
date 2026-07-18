package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.entity.IdOrTag;

import java.util.List;

/** a {@code worldgen/structure_set} placement rule */
public sealed interface StructurePlacement permits RandomSpreadPlacement, ConcentricRingsPlacement {
	Codec<StructurePlacement> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<StructurePlacement, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "random_spread" -> RandomSpreadPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "concentric_rings" -> ConcentricRingsPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported structure placement type");
			});
		}

		@Override
		public <T> DataResult<T> encode(StructurePlacement input, DynamicOps<T> ops, T prefix) {
			if (input instanceof RandomSpreadPlacement p) return RandomSpreadPlacement.CODEC.codec().encode(p, ops, prefix);
			if (input instanceof ConcentricRingsPlacement p) return ConcentricRingsPlacement.CODEC.codec().encode(p, ops, prefix);
			return DataResult.error(() -> "Unsupported structure placement: " + input.getClass().getSimpleName());
		}
	};

	/** {@code minecraft:random_spread}: a spaced grid of chunks, offset by a random amount per cell */
	static RandomSpreadPlacement randomSpread(int salt, int spacing, int separation) {
		return new RandomSpreadPlacement(
				LocateOffset.ZERO, "default", 1.0F, salt, java.util.Optional.empty(),
				spacing, separation, "linear"
		);
	}

	/** {@code minecraft:concentric_rings}: rings of evenly-spaced structures around the origin */
	static ConcentricRingsPlacement concentricRings(int distance, int spread, int count, int salt, IdOrTag preferredBiomes) {
		return new ConcentricRingsPlacement(
				LocateOffset.ZERO, "default", 1.0F, salt, java.util.Optional.empty(),
				distance, spread, count, preferredBiomes
		);
	}

	/**
	 * A {@code Vec3i}-like block offset applied to the located position, shared by the
	 * spreading placement types ({@code locate_offset}, defaults to {@code [0, 0, 0]}).
	 */
	record LocateOffset(int x, int y, int z) {
		public static final LocateOffset ZERO = new LocateOffset(0, 0, 0);

		public static final Codec<LocateOffset> CODEC = Codec.INT.listOf().xmap(
				list -> list.size() == 3 ? new LocateOffset(list.get(0), list.get(1), list.get(2)) : ZERO,
				offset -> List.of(offset.x, offset.y, offset.z)
		);
	}

	/**
	 * Suppresses generation near an existing {@code worldgen/structure_set}, shared by the
	 * spreading placement types ({@code exclusion_zone}, absent by default).
	 */
	record ExclusionZone(Identifier otherSet, int chunkCount) {
		public static final Codec<ExclusionZone> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("other_set").forGetter(ExclusionZone::otherSet),
				Codec.INT.fieldOf("chunk_count").forGetter(ExclusionZone::chunkCount)
		).apply(i, ExclusionZone::new));
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
