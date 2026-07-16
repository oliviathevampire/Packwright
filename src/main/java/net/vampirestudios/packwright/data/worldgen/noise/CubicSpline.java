package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * A piecewise cubic spline used by the {@code spline} density function: either a
 * flat constant, or a series of {@link Point}s plotted against a density-function
 * {@code coordinate}.
 */
public sealed interface CubicSpline permits CubicSpline.Constant, CubicSpline.Multipoint {
	static CubicSpline constant(float value) {
		return new Constant(value);
	}

	static CubicSpline multipoint(DensityFunction coordinate, List<Point> points) {
		return new Multipoint(coordinate, points);
	}

	static Point point(float location, float value, float derivative) {
		return new Point(location, constant(value), derivative);
	}

	static Point point(float location, float value) {
		return point(location, value, 0.0F);
	}

	static Point point(float location, CubicSpline value, float derivative) {
		return new Point(location, value, derivative);
	}

	static Point point(float location, CubicSpline value) {
		return point(location, value, 0.0F);
	}

	static Codec<CubicSpline> codec(Codec<DensityFunction> densityFunctionCodec) {
		return Codec.recursive("PackwrightCubicSpline", self -> {
			Codec<Multipoint> multipointCodec = Multipoint.codec(densityFunctionCodec, self);

			return Codec.either(Codec.FLOAT, multipointCodec).xmap(
					(Either<Float, Multipoint> either) -> (CubicSpline) either.map(
							value -> new Constant(value),
							multipoint -> multipoint
					),
					(CubicSpline spline) -> spline instanceof Constant constant
							? Either.<Float, Multipoint>left(constant.value())
							: Either.<Float, Multipoint>right((Multipoint) spline)
			);
		});
	}

	record Constant(float value) implements CubicSpline {
	}

	record Multipoint(DensityFunction coordinate, List<Point> points) implements CubicSpline {
		static Codec<Multipoint> codec(Codec<DensityFunction> densityFunctionCodec, Codec<CubicSpline> splineCodec) {
			return RecordCodecBuilder.create(instance -> instance.group(
					densityFunctionCodec
							.fieldOf("coordinate")
							.forGetter(Multipoint::coordinate),
					Point.codec(splineCodec)
							.listOf()
							.fieldOf("points")
							.forGetter(Multipoint::points)
			).apply(instance, Multipoint::new));
		}
	}

	record Point(float location, CubicSpline value, float derivative) {
		static Codec<Point> codec(Codec<CubicSpline> splineCodec) {
			return RecordCodecBuilder.create(instance -> instance.group(
					Codec.FLOAT.fieldOf("location").forGetter(Point::location),
					splineCodec.fieldOf("value").forGetter(Point::value),
					Codec.FLOAT.fieldOf("derivative").forGetter(Point::derivative)
			).apply(instance, Point::new));
		}
	}
}
