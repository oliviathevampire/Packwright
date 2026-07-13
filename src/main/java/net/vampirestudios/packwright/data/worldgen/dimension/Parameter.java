package net.vampirestudios.packwright.data.worldgen.dimension;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.List;

/**
 * A climate point or inclusive climate range.
 *
 * <p>A point serializes as a number:</p>
 *
 * <pre>{@code
 * "temperature": 0.5
 * }</pre>
 *
 * <p>A range serializes as two values:</p>
 *
 * <pre>{@code
 * "temperature": [0.45, 1.0]
 * }</pre>
 */
public record Parameter(float min, float max) {
	private static final Codec<Either<Float, List<Float>>> VALUE_CODEC =
			Codec.either(Codec.FLOAT, Codec.FLOAT.listOf());

	public static final Codec<Parameter> CODEC = VALUE_CODEC.flatXmap(value -> value.map(
			point -> DataResult.success(Parameter.point(point)),
			range -> {
				if (range.size() != 2) {
					return DataResult.error(() -> "Expected a two-value climate range");
				}

				float min = range.get(0);
				float max = range.get(1);

				if (min > max) {
					return DataResult.error(() -> "Climate range minimum " + min + " exceeds maximum " + max);
				}

				return DataResult.success(Parameter.span(min, max));
			}
	), parameter -> DataResult.success(parameter.isPoint()
			? Either.left(parameter.min)
			: Either.right(List.of(parameter.min, parameter.max))
	));

	public Parameter {
		if (min > max) {
			throw new IllegalArgumentException("Climate range minimum " + min + " exceeds maximum " + max);
		}
	}

	public static Parameter point(float value) {
		return new Parameter(value, value);
	}

	public static Parameter span(float min, float max) {
		return new Parameter(min, max);
	}

	public static Parameter full() {
		return span(-1.0F, 1.0F);
	}

	public boolean isPoint() {
		return Float.compare(this.min, this.max) == 0;
	}
}