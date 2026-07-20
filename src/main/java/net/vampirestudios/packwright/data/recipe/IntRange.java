package net.vampirestudios.packwright.data.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * A simple inclusive integer range, mirroring the JSON shape of vanilla's
 * {@code MinMaxBounds.Ints} (used e.g. by {@code TransmuteRecipe}'s {@code material_count}
 * and {@code BookCloningRecipe}'s {@code allowed_generations}): either a bare integer
 * (an exact value), or an object with optional {@code min}/{@code max} fields.
 * <p>
 * This project doesn't model the full {@code MinMaxBounds} family (float/double variants,
 * command-input parsing, etc.) elsewhere, so only the minimal integer shape is provided.
 */
public record IntRange(Optional<Integer> min, Optional<Integer> max) {
	private static final Codec<IntRange> OBJECT_CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("min").forGetter(IntRange::min),
			Codec.INT.optionalFieldOf("max").forGetter(IntRange::max)
	).apply(i, IntRange::new));

	public static final Codec<IntRange> CODEC = Codec.either(OBJECT_CODEC, Codec.INT).xmap(
			either -> either.map(range -> range, IntRange::exactly),
			range -> range.min.equals(range.max) && range.min.isPresent()
					? Either.right(range.min.get())
					: Either.left(range)
	);

	public static IntRange exactly(int value) {
		return new IntRange(Optional.of(value), Optional.of(value));
	}

	public static IntRange between(int min, int max) {
		return new IntRange(Optional.of(min), Optional.of(max));
	}

	public static IntRange atLeast(int min) {
		return new IntRange(Optional.of(min), Optional.empty());
	}

	public static IntRange atMost(int max) {
		return new IntRange(Optional.empty(), Optional.of(max));
	}
}
