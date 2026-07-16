package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

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

	public static DensityFunction cacheAllInCell(DensityFunction argument) {
		return new Marker(Marker.Type.CACHE_ALL_IN_CELL, argument);
	}

	public static DensityFunction invert(DensityFunction argument) {
		return new Mapped(Mapped.Type.INVERT, argument);
	}

	public static DensityFunction blendDensity(DensityFunction argument) {
		return new BlendDensity(argument);
	}

	public static DensityFunction blendAlpha() {
		return new BlendAlpha();
	}

	public static DensityFunction blendOffset() {
		return new BlendOffset();
	}

	public static DensityFunction beardifier() {
		return new Beardifier();
	}

	public static DensityFunction oldBlendedNoise(
			double xzScale,
			double yScale,
			double xzFactor,
			double yFactor,
			double smearScaleMultiplier
	) {
		return new OldBlendedNoise(xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier);
	}

	public static DensityFunction endIslands() {
		return new EndIslands();
	}

	public static DensityFunction shiftedNoise(
			DensityFunction shiftX,
			DensityFunction shiftY,
			DensityFunction shiftZ,
			double xzScale,
			double yScale,
			Identifier noise
	) {
		return new ShiftedNoise(shiftX, shiftY, shiftZ, xzScale, yScale, noise);
	}

	public static DensityFunction intervalSelect(
			DensityFunction input,
			List<Double> thresholds,
			List<DensityFunction> functions
	) {
		return new IntervalSelect(input, thresholds, functions);
	}

	public static DensityFunction shiftA(Identifier noise) {
		return new ShiftA(noise);
	}

	public static DensityFunction shiftB(Identifier noise) {
		return new ShiftB(noise);
	}

	public static DensityFunction shift(Identifier noise) {
		return new Shift(noise);
	}

	public static DensityFunction spline(CubicSpline spline) {
		return new Spline(spline);
	}

	public static DensityFunction findTopSurface(
			DensityFunction density,
			DensityFunction upperBound,
			int lowerBound,
			int cellHeight
	) {
		return new FindTopSurface(density, upperBound, lowerBound, cellHeight);
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
				case INVERT -> DensityFunctionTypes.INVERT;
			};
		}

		public enum Type {
			ABS,
			SQUARE,
			CUBE,
			HALF_NEGATIVE,
			QUARTER_NEGATIVE,
			SQUEEZE,
			INVERT
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
				case CACHE_ALL_IN_CELL -> DensityFunctionTypes.CACHE_ALL_IN_CELL;
			};
		}

		public enum Type {
			INTERPOLATED,
			FLAT_CACHE,
			CACHE_2D,
			CACHE_ONCE,
			CACHE_ALL_IN_CELL
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

	/** {@code minecraft:blend_alpha}: the blending alpha at this position, no arguments */
	public record BlendAlpha() implements DensityFunction.Direct {
		private static final MapCodec<BlendAlpha> DATA_CODEC = MapCodec.unit(new BlendAlpha());

		static MapCodec<BlendAlpha> codec(Codec<DensityFunction> ignored) {
			return DATA_CODEC;
		}

		@Override
		public DensityFunctionType<BlendAlpha> type() {
			return DensityFunctionTypes.BLEND_ALPHA;
		}
	}

	/** {@code minecraft:blend_offset}: the blending offset at this position, no arguments */
	public record BlendOffset() implements DensityFunction.Direct {
		private static final MapCodec<BlendOffset> DATA_CODEC = MapCodec.unit(new BlendOffset());

		static MapCodec<BlendOffset> codec(Codec<DensityFunction> ignored) {
			return DATA_CODEC;
		}

		@Override
		public DensityFunctionType<BlendOffset> type() {
			return DensityFunctionTypes.BLEND_OFFSET;
		}
	}

	/** {@code minecraft:beardifier}: mob/structure carve-out density, no arguments */
	public record Beardifier() implements DensityFunction.Direct {
		private static final MapCodec<Beardifier> DATA_CODEC = MapCodec.unit(new Beardifier());

		static MapCodec<Beardifier> codec(Codec<DensityFunction> ignored) {
			return DATA_CODEC;
		}

		@Override
		public DensityFunctionType<Beardifier> type() {
			return DensityFunctionTypes.BEARDIFIER;
		}
	}

	/** {@code minecraft:old_blended_noise}: the legacy (pre-1.18) terrain noise */
	public record OldBlendedNoise(
			double xzScale,
			double yScale,
			double xzFactor,
			double yFactor,
			double smearScaleMultiplier
	) implements DensityFunction.Direct {
		static MapCodec<OldBlendedNoise> codec(Codec<DensityFunction> ignored) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Codec.DOUBLE
							.fieldOf("xz_scale")
							.forGetter(OldBlendedNoise::xzScale),
					Codec.DOUBLE
							.fieldOf("y_scale")
							.forGetter(OldBlendedNoise::yScale),
					Codec.DOUBLE
							.fieldOf("xz_factor")
							.forGetter(OldBlendedNoise::xzFactor),
					Codec.DOUBLE
							.fieldOf("y_factor")
							.forGetter(OldBlendedNoise::yFactor),
					Codec.DOUBLE
							.fieldOf("smear_scale_multiplier")
							.forGetter(OldBlendedNoise::smearScaleMultiplier)
			).apply(instance, OldBlendedNoise::new));
		}

		@Override
		public DensityFunctionType<OldBlendedNoise> type() {
			return DensityFunctionTypes.OLD_BLENDED_NOISE;
		}
	}

	/** {@code minecraft:end_islands}: the End's island density, no arguments */
	public record EndIslands() implements DensityFunction.Direct {
		private static final MapCodec<EndIslands> DATA_CODEC = MapCodec.unit(new EndIslands());

		static MapCodec<EndIslands> codec(Codec<DensityFunction> ignored) {
			return DATA_CODEC;
		}

		@Override
		public DensityFunctionType<EndIslands> type() {
			return DensityFunctionTypes.END_ISLANDS;
		}
	}

	/** {@code minecraft:shifted_noise}: samples {@code noise} at a position offset by three density functions */
	public record ShiftedNoise(
			DensityFunction shiftX,
			DensityFunction shiftY,
			DensityFunction shiftZ,
			double xzScale,
			double yScale,
			Identifier noise
	) implements DensityFunction.Direct {
		static MapCodec<ShiftedNoise> codec(Codec<DensityFunction> densityFunctionCodec) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("shift_x")
							.forGetter(ShiftedNoise::shiftX),
					densityFunctionCodec
							.fieldOf("shift_y")
							.forGetter(ShiftedNoise::shiftY),
					densityFunctionCodec
							.fieldOf("shift_z")
							.forGetter(ShiftedNoise::shiftZ),
					Codec.DOUBLE
							.fieldOf("xz_scale")
							.forGetter(ShiftedNoise::xzScale),
					Codec.DOUBLE
							.fieldOf("y_scale")
							.forGetter(ShiftedNoise::yScale),
					Identifier.CODEC
							.fieldOf("noise")
							.forGetter(ShiftedNoise::noise)
			).apply(instance, ShiftedNoise::new));
		}

		@Override
		public DensityFunctionType<ShiftedNoise> type() {
			return DensityFunctionTypes.SHIFTED_NOISE;
		}
	}

	/** {@code minecraft:interval_select}: picks one of {@code functions} based on which {@code thresholds} bucket {@code input} falls into */
	public record IntervalSelect(
			DensityFunction input,
			List<Double> thresholds,
			List<DensityFunction> functions
	) implements DensityFunction.Direct {
		static MapCodec<IntervalSelect> codec(Codec<DensityFunction> densityFunctionCodec) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("input")
							.forGetter(IntervalSelect::input),
					Codec.DOUBLE.listOf()
							.fieldOf("thresholds")
							.forGetter(IntervalSelect::thresholds),
					densityFunctionCodec.listOf()
							.fieldOf("functions")
							.forGetter(IntervalSelect::functions)
			).apply(instance, IntervalSelect::new));
		}

		@Override
		public DensityFunctionType<IntervalSelect> type() {
			return DensityFunctionTypes.INTERVAL_SELECT;
		}
	}

	/** {@code minecraft:shift_a}: samples {@code noise} at (blockX, 0, blockZ), scaled by 0.25 */
	public record ShiftA(Identifier noise) implements DensityFunction.Direct {
		static MapCodec<ShiftA> codec(Codec<DensityFunction> ignored) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Identifier.CODEC
							.fieldOf("argument")
							.forGetter(ShiftA::noise)
			).apply(instance, ShiftA::new));
		}

		@Override
		public DensityFunctionType<ShiftA> type() {
			return DensityFunctionTypes.SHIFT_A;
		}
	}

	/** {@code minecraft:shift_b}: samples {@code noise} at (blockZ, blockX, 0), scaled by 0.25 */
	public record ShiftB(Identifier noise) implements DensityFunction.Direct {
		static MapCodec<ShiftB> codec(Codec<DensityFunction> ignored) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Identifier.CODEC
							.fieldOf("argument")
							.forGetter(ShiftB::noise)
			).apply(instance, ShiftB::new));
		}

		@Override
		public DensityFunctionType<ShiftB> type() {
			return DensityFunctionTypes.SHIFT_B;
		}
	}

	/** {@code minecraft:shift}: samples {@code noise} at (blockX, blockY, blockZ), scaled by 0.25 */
	public record Shift(Identifier noise) implements DensityFunction.Direct {
		static MapCodec<Shift> codec(Codec<DensityFunction> ignored) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					Identifier.CODEC
							.fieldOf("argument")
							.forGetter(Shift::noise)
			).apply(instance, Shift::new));
		}

		@Override
		public DensityFunctionType<Shift> type() {
			return DensityFunctionTypes.SHIFT;
		}
	}

	/** {@code minecraft:spline}: samples a {@link CubicSpline} */
	public record Spline(CubicSpline spline) implements DensityFunction.Direct {
		static MapCodec<Spline> codec(Codec<DensityFunction> densityFunctionCodec) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					CubicSpline.codec(densityFunctionCodec)
							.fieldOf("spline")
							.forGetter(Spline::spline)
			).apply(instance, Spline::new));
		}

		@Override
		public DensityFunctionType<Spline> type() {
			return DensityFunctionTypes.SPLINE;
		}
	}

	/** {@code minecraft:find_top_surface}: the highest {@code density}-positive cell at or below {@code upper_bound}, stepping down by {@code cell_height} until {@code lower_bound} */
	public record FindTopSurface(
			DensityFunction density,
			DensityFunction upperBound,
			int lowerBound,
			int cellHeight
	) implements DensityFunction.Direct {
		static MapCodec<FindTopSurface> codec(Codec<DensityFunction> densityFunctionCodec) {
			return RecordCodecBuilder.mapCodec(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("density")
							.forGetter(FindTopSurface::density),
					densityFunctionCodec
							.fieldOf("upper_bound")
							.forGetter(FindTopSurface::upperBound),
					Codec.INT
							.fieldOf("lower_bound")
							.forGetter(FindTopSurface::lowerBound),
					Codec.INT
							.fieldOf("cell_height")
							.forGetter(FindTopSurface::cellHeight)
			).apply(instance, FindTopSurface::new));
		}

		@Override
		public DensityFunctionType<FindTopSurface> type() {
			return DensityFunctionTypes.FIND_TOP_SURFACE;
		}
	}
}