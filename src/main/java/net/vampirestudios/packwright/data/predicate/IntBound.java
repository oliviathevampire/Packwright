package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * A simple min/max integer bound, as used by advancement criteria fields like
 * {@code minecraft:enchanted_item}'s "levels" or {@code minecraft:inventory_changed}'s
 * "slots" sub-fields (vanilla's {@code MinMaxBounds.Ints}). Always serialized as a
 * {@code {"min": x, "max": y}} object — this project skips vanilla's bare-number
 * shorthand for exact values, consistent with its simplified-range convention. Both
 * bounds are optional; unset means unbounded on that side.
 */
public record IntBound(Optional<Integer> min, Optional<Integer> max) {
	public static final Codec<IntBound> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("min").forGetter(IntBound::min),
			Codec.INT.optionalFieldOf("max").forGetter(IntBound::max)
	).apply(i, IntBound::new));

	public static IntBound exactly(int value) {
		return new IntBound(Optional.of(value), Optional.of(value));
	}

	public static IntBound atLeast(int min) {
		return new IntBound(Optional.of(min), Optional.empty());
	}

	public static IntBound atMost(int max) {
		return new IntBound(Optional.empty(), Optional.of(max));
	}

	public static IntBound between(int min, int max) {
		return new IntBound(Optional.of(min), Optional.of(max));
	}

	public boolean isAny() {
		return min.isEmpty() && max.isEmpty();
	}
}
