package net.vampirestudios.arrp.data.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface IntProvider {
	Codec<IntProvider> DISPATCH_CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<IntProvider, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String type = string(map, ops, "type", "");
				return switch (normalizeType(type)) {
					case "uniform" -> UniformInt.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "biased_to_bottom" -> BiasedToBottomInt.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "clamped" -> ClampedInt.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					default -> DataResult.error(() -> "Unknown int provider type: " + type);
				};
			});
		}

		@Override
		public <T> DataResult<T> encode(IntProvider input, DynamicOps<T> ops, T prefix) {
			if (input instanceof UniformInt uniform) return UniformInt.CODEC.codec().encode(uniform, ops, prefix);
			if (input instanceof BiasedToBottomInt biased) return BiasedToBottomInt.CODEC.codec().encode(biased, ops, prefix);
			if (input instanceof ClampedInt clamped) return ClampedInt.CODEC.codec().encode(clamped, ops, prefix);
			return DataResult.error(() -> "Constant int providers must be encoded as a raw int");
		}
	};
	Codec<Either<Integer, IntProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(Codec.INT, DISPATCH_CODEC);
	Codec<IntProvider> CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(
			either -> either.map(ConstantInt::new, provider -> provider),
			provider -> provider instanceof ConstantInt(int value) ? Either.left(value) : Either.right(provider)
	);

	static IntProvider constant(int value) {
		return new ConstantInt(value);
	}

	static IntProvider uniform(int minInclusive, int maxInclusive) {
		return new UniformInt(minInclusive, maxInclusive);
	}

	static IntProvider biasedToBottom(int minInclusive, int maxInclusive) {
		return new BiasedToBottomInt(minInclusive, maxInclusive);
	}

	static IntProvider clamped(IntProvider source, int minInclusive, int maxInclusive) {
		return new ClampedInt(source, minInclusive, maxInclusive);
	}

	int min();
	int max();

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}

	record ConstantInt(int value) implements IntProvider {
		@Override
		public int min() { return value; }
		@Override
		public int max() { return value; }
	}

	record UniformInt(int minInclusive, int maxInclusive) implements IntProvider {
		public static final MapCodec<UniformInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:uniform"),
				Codec.INT.fieldOf("min_inclusive").forGetter(UniformInt::minInclusive),
				Codec.INT.fieldOf("max_inclusive").forGetter(UniformInt::maxInclusive)
		).apply(i, (type, minInclusive, maxInclusive) -> new UniformInt(minInclusive, maxInclusive)));

		@Override
		public int min() { return minInclusive; }
		@Override
		public int max() { return maxInclusive; }
	}

	record BiasedToBottomInt(int minInclusive, int maxInclusive) implements IntProvider {
		public static final MapCodec<BiasedToBottomInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:biased_to_bottom"),
				Codec.INT.fieldOf("min_inclusive").forGetter(BiasedToBottomInt::minInclusive),
				Codec.INT.fieldOf("max_inclusive").forGetter(BiasedToBottomInt::maxInclusive)
		).apply(i, (type, minInclusive, maxInclusive) -> new BiasedToBottomInt(minInclusive, maxInclusive)));

		@Override
		public int min() { return minInclusive; }
		@Override
		public int max() { return maxInclusive; }
	}

	record ClampedInt(IntProvider source, int minInclusive, int maxInclusive) implements IntProvider {
		public static final MapCodec<ClampedInt> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:clamped"),
				IntProvider.CODEC.fieldOf("source").forGetter(ClampedInt::source),
				Codec.INT.fieldOf("min_inclusive").forGetter(ClampedInt::minInclusive),
				Codec.INT.fieldOf("max_inclusive").forGetter(ClampedInt::maxInclusive)
		).apply(i, (type, source, minInclusive, maxInclusive) -> new ClampedInt(source, minInclusive, maxInclusive)));

		@Override
		public int min() { return minInclusive; }
		@Override
		public int max() { return maxInclusive; }
	}
}
