package net.vampirestudios.arrp.data.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface FloatProvider {
	Codec<FloatProvider> DISPATCH_CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<FloatProvider, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String type = string(map, ops, "type", "");
				return switch (normalizeType(type)) {
					case "uniform" -> UniformFloat.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "clamped_normal" -> ClampedNormalFloat.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "trapezoid" -> TrapezoidFloat.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					default -> DataResult.error(() -> "Unknown float provider type: " + type);
				};
			});
		}

		@Override
		public <T> DataResult<T> encode(FloatProvider input, DynamicOps<T> ops, T prefix) {
			RecordBuilder<T> builder = ops.mapBuilder();
			if (input instanceof UniformFloat uniform) {
				return UniformFloat.encode(uniform, ops, builder).build(prefix);
			}
			if (input instanceof ClampedNormalFloat clampedNormal) {
				return ClampedNormalFloat.encode(clampedNormal, ops, builder).build(prefix);
			}
			if (input instanceof TrapezoidFloat trapezoid) {
				return TrapezoidFloat.encode(trapezoid, ops, builder).build(prefix);
			}
			return DataResult.error(() -> "Constant float providers must be encoded as a raw float");
		}
	};
	Codec<Either<Float, FloatProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(Codec.FLOAT, DISPATCH_CODEC);
	Codec<FloatProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(
			either -> either.map(ConstantFloat::new, provider -> provider),
			provider -> provider instanceof ConstantFloat(float value) ? Either.left(value) : Either.right(provider)
	);

	static Codec<FloatProvider> codec(float minValue, float maxValue) {
		return CODEC.validate(value -> {
			if (value.min() < minValue) {
				return DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.min() + "-" + value.max() + "]");
			}
			return value.max() > maxValue
					? DataResult.error(() -> "Value provider too high: " + maxValue + " [" + value.min() + "-" + value.max() + "]")
					: DataResult.success(value);
		});
	}

	static FloatProvider constant(float value) {
		return new ConstantFloat(value);
	}

	static FloatProvider uniform(float minInclusive, float maxExclusive) {
		return new UniformFloat(minInclusive, maxExclusive);
	}

	static FloatProvider clampedNormal(float mean, float deviation, float min, float max) {
		return new ClampedNormalFloat(mean, deviation, min, max);
	}

	static FloatProvider trapezoid(float min, float max, float plateau) {
		return new TrapezoidFloat(min, max, plateau);
	}

	MapCodec<? extends FloatProvider> codec();
	float min();
	float max();

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}

	record ConstantFloat(float value) implements FloatProvider {
		public static final MapCodec<ConstantFloat> MAP_CODEC = Codec.FLOAT.fieldOf("value").xmap(ConstantFloat::new, ConstantFloat::value);

		@Override
		public MapCodec<ConstantFloat> codec() { return MAP_CODEC; }
		@Override
		public float min() { return value; }
		@Override
		public float max() { return value; }
	}

	record UniformFloat(float minInclusive, float maxExclusive) implements FloatProvider {
		public static final MapCodec<UniformFloat> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:uniform"),
				Codec.FLOAT.fieldOf("min_inclusive").forGetter(UniformFloat::minInclusive),
				Codec.FLOAT.fieldOf("max_exclusive").forGetter(UniformFloat::maxExclusive)
		).apply(i, (type, minInclusive, maxExclusive) -> new UniformFloat(minInclusive, maxExclusive)));

		@Override
		public MapCodec<UniformFloat> codec() { return MAP_CODEC; }
		@Override
		public float min() { return minInclusive; }
		@Override
		public float max() { return maxExclusive; }

		private static <T> RecordBuilder<T> encode(UniformFloat input, DynamicOps<T> ops, RecordBuilder<T> builder) {
			return builder
					.add("type", ops.createString("minecraft:uniform"))
					.add("min_inclusive", ops.createFloat(input.minInclusive))
					.add("max_exclusive", ops.createFloat(input.maxExclusive));
		}
	}

	record ClampedNormalFloat(float mean, float deviation, float min, float max) implements FloatProvider {
		public static final MapCodec<ClampedNormalFloat> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:clamped_normal"),
				Codec.FLOAT.fieldOf("mean").forGetter(ClampedNormalFloat::mean),
				Codec.FLOAT.fieldOf("deviation").forGetter(ClampedNormalFloat::deviation),
				Codec.FLOAT.fieldOf("min").forGetter(ClampedNormalFloat::min),
				Codec.FLOAT.fieldOf("max").forGetter(ClampedNormalFloat::max)
		).apply(i, (type, mean, deviation, min, max) -> new ClampedNormalFloat(mean, deviation, min, max)));

		@Override
		public MapCodec<ClampedNormalFloat> codec() { return MAP_CODEC; }

		private static <T> RecordBuilder<T> encode(ClampedNormalFloat input, DynamicOps<T> ops, RecordBuilder<T> builder) {
			return builder
					.add("type", ops.createString("minecraft:clamped_normal"))
					.add("mean", ops.createFloat(input.mean))
					.add("deviation", ops.createFloat(input.deviation))
					.add("min", ops.createFloat(input.min))
					.add("max", ops.createFloat(input.max));
		}
	}

	record TrapezoidFloat(float min, float max, float plateau) implements FloatProvider {
		public static final MapCodec<TrapezoidFloat> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:trapezoid"),
				Codec.FLOAT.fieldOf("min").forGetter(TrapezoidFloat::min),
				Codec.FLOAT.fieldOf("max").forGetter(TrapezoidFloat::max),
				Codec.FLOAT.fieldOf("plateau").forGetter(TrapezoidFloat::plateau)
		).apply(i, (type, min, max, plateau) -> new TrapezoidFloat(min, max, plateau)));

		@Override
		public MapCodec<TrapezoidFloat> codec() { return MAP_CODEC; }

		private static <T> RecordBuilder<T> encode(TrapezoidFloat input, DynamicOps<T> ops, RecordBuilder<T> builder) {
			return builder
					.add("type", ops.createString("minecraft:trapezoid"))
					.add("min", ops.createFloat(input.min))
					.add("max", ops.createFloat(input.max))
					.add("plateau", ops.createFloat(input.plateau));
		}
	}
}
