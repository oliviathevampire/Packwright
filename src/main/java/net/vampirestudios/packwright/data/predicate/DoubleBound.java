package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * A simple min/max double bound, as used by advancement criteria fields like
 * {@code minecraft:used_ender_eye}'s "distance" (vanilla's {@code MinMaxBounds.Doubles}).
 * Always serialized as a {@code {"min": x, "max": y}} object; see {@link IntBound} for
 * why the bare-number shorthand is skipped.
 */
public record DoubleBound(Optional<Double> min, Optional<Double> max) {
	public static final Codec<DoubleBound> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.DOUBLE.optionalFieldOf("min").forGetter(DoubleBound::min),
			Codec.DOUBLE.optionalFieldOf("max").forGetter(DoubleBound::max)
	).apply(i, DoubleBound::new));

	public static DoubleBound exactly(double value) {
		return new DoubleBound(Optional.of(value), Optional.of(value));
	}

	public static DoubleBound atLeast(double min) {
		return new DoubleBound(Optional.of(min), Optional.empty());
	}

	public static DoubleBound atMost(double max) {
		return new DoubleBound(Optional.empty(), Optional.of(max));
	}

	public static DoubleBound between(double min, double max) {
		return new DoubleBound(Optional.of(min), Optional.of(max));
	}

	public boolean isAny() {
		return min.isEmpty() && max.isEmpty();
	}
}
