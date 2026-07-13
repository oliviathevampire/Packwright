package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * Factory methods and implementations for density functions.
 */
public final class DensityFunctions {
	private DensityFunctions() {
	}

	// ---------- factories ----------

	public static DensityFunction constant(double value) {
		return new Constant(value);
	}

	public static DensityFunction zero() {
		return constant(0.0);
	}

	public static DensityFunction reference(Identifier id) {
		return new DensityFunction.Reference(id);
	}

	public static DensityFunction noise(
			Identifier noise,
			double xzScale,
			double yScale
	) {
		return new Noise(noise, xzScale, yScale);
	}

	public static DensityFunction yClampedGradient(
			int fromY,
			int toY,
			double fromValue,
			double toValue
	) {
		return new YClampedGradient(
				fromY,
				toY,
				fromValue,
				toValue
		);
	}

	public static DensityFunction add(
			DensityFunction argument1,
			DensityFunction argument2
	) {
		return new TwoArgumentSimpleFunction(
				TwoArgumentSimpleFunction.Type.ADD,
				argument1,
				argument2
		);
	}

	public static DensityFunction mul(
			DensityFunction argument1,
			DensityFunction argument2
	) {
		return new TwoArgumentSimpleFunction(
				TwoArgumentSimpleFunction.Type.MUL,
				argument1,
				argument2
		);
	}

	public static DensityFunction min(
			DensityFunction argument1,
			DensityFunction argument2
	) {
		return new TwoArgumentSimpleFunction(
				TwoArgumentSimpleFunction.Type.MIN,
				argument1,
				argument2
		);
	}

	public static DensityFunction max(
			DensityFunction argument1,
			DensityFunction argument2
	) {
		return new TwoArgumentSimpleFunction(
				TwoArgumentSimpleFunction.Type.MAX,
				argument1,
				argument2
		);
	}

	public static DensityFunction abs(DensityFunction argument) {
		return new Mapped(Mapped.Type.ABS, argument);
	}

	public static DensityFunction square(DensityFunction argument) {
		return new Mapped(Mapped.Type.SQUARE, argument);
	}

	public static DensityFunction cube(DensityFunction argument) {
		return new Mapped(Mapped.Type.CUBE, argument);
	}

	public static DensityFunction halfNegative(DensityFunction argument) {
		return new Mapped(Mapped.Type.HALF_NEGATIVE, argument);
	}

	public static DensityFunction quarterNegative(DensityFunction argument) {
		return new Mapped(Mapped.Type.QUARTER_NEGATIVE, argument);
	}

	public static DensityFunction squeeze(DensityFunction argument) {
		return new Mapped(Mapped.Type.SQUEEZE, argument);
	}

	public static DensityFunction interpolated(DensityFunction argument) {
		return new Marker(Marker.Type.INTERPOLATED, argument);
	}

	public static DensityFunction flatCache(DensityFunction argument) {
		return new Marker(Marker.Type.FLAT_CACHE, argument);
	}

	public static DensityFunction cache2d(DensityFunction argument) {
		return new Marker(Marker.Type.CACHE_2D, argument);
	}

	public static DensityFunction cacheOnce(DensityFunction argument) {
		return new Marker(Marker.Type.CACHE_ONCE, argument);
	}

	public static DensityFunction blendDensity(DensityFunction argument) {
		return new BlendDensity(argument);
	}

	public static DensityFunction clamp(
			DensityFunction input,
			double min,
			double max
	) {
		return new Clamp(input, min, max);
	}

	public static DensityFunction rangeChoice(
			DensityFunction input,
			double minInclusive,
			double maxExclusive,
			DensityFunction whenInRange,
			DensityFunction whenOutOfRange
	) {
		return new RangeChoice(
				input,
				minInclusive,
				maxExclusive,
				whenInRange,
				whenOutOfRange
		);
	}

	// ---------- implementations ----------

	public record Constant(double value) implements DensityFunction.Direct {
		static MapCodec<Constant> codec(Codec<DensityFunction> ignored) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Codec.DOUBLE
							.fieldOf("argument")
							.forGetter(Constant::value)
			).apply(instance, Constant::new));
		}

		@Override
		public DensityFunctionType<Constant> type() {
			return DensityFunctionTypes.CONSTANT;
		}
	}

	public record Noise(
			Identifier noise,
			double xzScale,
			double yScale
	) implements DensityFunction.Direct {
		static MapCodec<Noise> codec(Codec<DensityFunction> ignored) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Identifier.CODEC
							.fieldOf("noise")
							.forGetter(Noise::noise),
					Codec.DOUBLE
							.fieldOf("xz_scale")
							.forGetter(Noise::xzScale),
					Codec.DOUBLE
							.fieldOf("y_scale")
							.forGetter(Noise::yScale)
			).apply(instance, Noise::new));
		}

		@Override
		public DensityFunctionType<Noise> type() {
			return DensityFunctionTypes.NOISE;
		}
	}

	public record YClampedGradient(
			int fromY,
			int toY,
			double fromValue,
			double toValue
	) implements DensityFunction.Direct {
		static MapCodec<YClampedGradient> codec(
				Codec<DensityFunction> ignored
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Codec.INT
							.fieldOf("from_y")
							.forGetter(YClampedGradient::fromY),
					Codec.INT
							.fieldOf("to_y")
							.forGetter(YClampedGradient::toY),
					Codec.DOUBLE
							.fieldOf("from_value")
							.forGetter(YClampedGradient::fromValue),
					Codec.DOUBLE
							.fieldOf("to_value")
							.forGetter(YClampedGradient::toValue)
			).apply(instance, YClampedGradient::new));
		}

		@Override
		public DensityFunctionType<YClampedGradient> type() {
			return DensityFunctionTypes.Y_CLAMPED_GRADIENT;
		}
	}

	public record Mapped(
			Type operation,
			DensityFunction argument
	) implements DensityFunction.Direct {
		static MapCodec<Mapped> codec(
				Codec<DensityFunction> densityFunctionCodec,
				Type operation
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("argument")
							.forGetter(Mapped::argument)
			).apply(instance, argument -> new Mapped(operation, argument)));
		}

		@Override
		public DensityFunctionType<Mapped> type() {
			return switch (this.operation) {
				case ABS -> DensityFunctionTypes.ABS;
				case SQUARE -> DensityFunctionTypes.SQUARE;
				case CUBE -> DensityFunctionTypes.CUBE;
				case HALF_NEGATIVE -> DensityFunctionTypes.HALF_NEGATIVE;
				case QUARTER_NEGATIVE -> DensityFunctionTypes.QUARTER_NEGATIVE;
				case SQUEEZE -> DensityFunctionTypes.SQUEEZE;
			};
		}

		public enum Type {
			ABS,
			SQUARE,
			CUBE,
			HALF_NEGATIVE,
			QUARTER_NEGATIVE,
			SQUEEZE
		}
	}

	public record Marker(
			Type marker,
			DensityFunction argument
	) implements DensityFunction.Direct {
		static MapCodec<Marker> codec(
				Codec<DensityFunction> densityFunctionCodec,
				Type marker
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("argument")
							.forGetter(Marker::argument)
			).apply(instance, argument -> new Marker(marker, argument)));
		}

		@Override
		public DensityFunctionType<Marker> type() {
			return switch (this.marker) {
				case INTERPOLATED -> DensityFunctionTypes.INTERPOLATED;
				case FLAT_CACHE -> DensityFunctionTypes.FLAT_CACHE;
				case CACHE_2D -> DensityFunctionTypes.CACHE_2D;
				case CACHE_ONCE -> DensityFunctionTypes.CACHE_ONCE;
			};
		}

		public enum Type {
			INTERPOLATED,
			FLAT_CACHE,
			CACHE_2D,
			CACHE_ONCE
		}
	}

	public record TwoArgumentSimpleFunction(
			Type operation,
			DensityFunction argument1,
			DensityFunction argument2
	) implements DensityFunction.Direct {
		static MapCodec<TwoArgumentSimpleFunction> codec(
				Codec<DensityFunction> densityFunctionCodec,
				Type operation
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("argument1")
							.forGetter(TwoArgumentSimpleFunction::argument1),
					densityFunctionCodec
							.fieldOf("argument2")
							.forGetter(TwoArgumentSimpleFunction::argument2)
			).apply(
					instance,
					(argument1, argument2) ->
							new TwoArgumentSimpleFunction(
									operation,
									argument1,
									argument2
							)
			));
		}

		@Override
		public DensityFunctionType<TwoArgumentSimpleFunction> type() {
			return switch (this.operation) {
				case ADD -> DensityFunctionTypes.ADD;
				case MUL -> DensityFunctionTypes.MUL;
				case MIN -> DensityFunctionTypes.MIN;
				case MAX -> DensityFunctionTypes.MAX;
			};
		}

		public enum Type {
			ADD,
			MUL,
			MIN,
			MAX
		}
	}

	public record BlendDensity(
			DensityFunction argument
	) implements DensityFunction.Direct {
		static MapCodec<BlendDensity> codec(
				Codec<DensityFunction> densityFunctionCodec
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("argument")
							.forGetter(BlendDensity::argument)
			).apply(instance, BlendDensity::new));
		}

		@Override
		public DensityFunctionType<BlendDensity> type() {
			return DensityFunctionTypes.BLEND_DENSITY;
		}
	}

	public record Clamp(
			DensityFunction input,
			double min,
			double max
	) implements DensityFunction.Direct {
		static MapCodec<Clamp> codec(
				Codec<DensityFunction> densityFunctionCodec
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("input")
							.forGetter(Clamp::input),
					Codec.DOUBLE
							.fieldOf("min")
							.forGetter(Clamp::min),
					Codec.DOUBLE
							.fieldOf("max")
							.forGetter(Clamp::max)
			).apply(instance, Clamp::new));
		}

		@Override
		public DensityFunctionType<Clamp> type() {
			return DensityFunctionTypes.CLAMP;
		}
	}

	public record RangeChoice(
			DensityFunction input,
			double minInclusive,
			double maxExclusive,
			DensityFunction whenInRange,
			DensityFunction whenOutOfRange
	) implements DensityFunction.Direct {
		static MapCodec<RangeChoice> codec(
				Codec<DensityFunction> densityFunctionCodec
		) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("input")
							.forGetter(RangeChoice::input),
					Codec.DOUBLE
							.fieldOf("min_inclusive")
							.forGetter(RangeChoice::minInclusive),
					Codec.DOUBLE
							.fieldOf("max_exclusive")
							.forGetter(RangeChoice::maxExclusive),
					densityFunctionCodec
							.fieldOf("when_in_range")
							.forGetter(RangeChoice::whenInRange),
					densityFunctionCodec
							.fieldOf("when_out_of_range")
							.forGetter(RangeChoice::whenOutOfRange)
			).apply(instance, RangeChoice::new));
		}

		@Override
		public DensityFunctionType<RangeChoice> type() {
			return DensityFunctionTypes.RANGE_CHOICE;
		}
	}
}