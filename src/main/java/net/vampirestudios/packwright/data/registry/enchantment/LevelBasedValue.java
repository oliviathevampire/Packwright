package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * A value that scales with an enchantment's level, e.g. {@code minecraft:linear}. A bare
 * number decodes/encodes as a {@link #constant(float)}; anything else is an object with a
 * {@code type}.
 */
public sealed interface LevelBasedValue permits
		LevelBasedValue.Constant,
		LevelBasedValue.Linear,
		LevelBasedValue.Clamped,
		LevelBasedValue.Fraction,
		LevelBasedValue.LevelsSquared,
		LevelBasedValue.Exponent,
		LevelBasedValue.Lookup {

	Codec<LevelBasedValue> DISPATCH_CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<LevelBasedValue, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "clamped" -> Clamped.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "fraction" -> Fraction.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "levels_squared" -> LevelsSquared.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "linear" -> Linear.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "exponent" -> Exponent.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "lookup" -> Lookup.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported level-based value type");
			});
		}

		@Override
		public <T> DataResult<T> encode(LevelBasedValue input, DynamicOps<T> ops, T prefix) {
			if (input instanceof Clamped v) return Clamped.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof Fraction v) return Fraction.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof LevelsSquared v) return LevelsSquared.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof Linear v) return Linear.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof Exponent v) return Exponent.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof Lookup v) return Lookup.CODEC.codec().encode(v, ops, prefix);
			return DataResult.error(() -> "Unsupported level-based value: " + input.getClass().getSimpleName());
		}
	};

	Codec<LevelBasedValue> CODEC = Codec.either(Codec.FLOAT, DISPATCH_CODEC).xmap(
			either -> either.map(v -> (LevelBasedValue) new Constant(v), v -> v),
			value -> value instanceof Constant c ? Either.left(c.value()) : Either.right(value)
	);

	static Constant constant(float value) { return new Constant(value); }
	static Linear linear(float base, float perLevelAboveFirst) { return new Linear(base, perLevelAboveFirst); }
	static Linear perLevel(float perLevel) { return new Linear(perLevel, perLevel); }
	static Clamped clamped(LevelBasedValue value, float min, float max) { return new Clamped(value, min, max); }
	static Fraction fraction(LevelBasedValue numerator, LevelBasedValue denominator) { return new Fraction(numerator, denominator); }
	static LevelsSquared levelsSquared(float added) { return new LevelsSquared(added); }
	static Exponent exponent(LevelBasedValue base, LevelBasedValue power) { return new Exponent(base, power); }
	static Lookup lookup(List<Float> values, LevelBasedValue fallback) { return new Lookup(values, fallback); }

	record Constant(float value) implements LevelBasedValue {
	}

	record Linear(float base, float perLevelAboveFirst) implements LevelBasedValue {
		static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:linear"),
				Codec.FLOAT.fieldOf("base").forGetter(Linear::base),
				Codec.FLOAT.fieldOf("per_level_above_first").forGetter(Linear::perLevelAboveFirst)
		).apply(i, (type, base, perLevel) -> new Linear(base, perLevel)));
	}

	record Clamped(LevelBasedValue value, float min, float max) implements LevelBasedValue {
		static final MapCodec<Clamped> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:clamped"),
				LevelBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value),
				Codec.FLOAT.fieldOf("min").forGetter(Clamped::min),
				Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)
		).apply(i, (type, value, min, max) -> new Clamped(value, min, max)));
	}

	record Fraction(LevelBasedValue numerator, LevelBasedValue denominator) implements LevelBasedValue {
		static final MapCodec<Fraction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:fraction"),
				LevelBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator),
				LevelBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)
		).apply(i, (type, num, denom) -> new Fraction(num, denom)));
	}

	record LevelsSquared(float added) implements LevelBasedValue {
		static final MapCodec<LevelsSquared> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:levels_squared"),
				Codec.FLOAT.fieldOf("added").forGetter(LevelsSquared::added)
		).apply(i, (type, added) -> new LevelsSquared(added)));
	}

	record Exponent(LevelBasedValue base, LevelBasedValue power) implements LevelBasedValue {
		static final MapCodec<Exponent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:exponent"),
				LevelBasedValue.CODEC.fieldOf("base").forGetter(Exponent::base),
				LevelBasedValue.CODEC.fieldOf("power").forGetter(Exponent::power)
		).apply(i, (type, base, power) -> new Exponent(base, power)));
	}

	record Lookup(List<Float> values, LevelBasedValue fallback) implements LevelBasedValue {
		static final MapCodec<Lookup> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:lookup"),
				Codec.FLOAT.listOf().fieldOf("values").forGetter(Lookup::values),
				LevelBasedValue.CODEC.fieldOf("fallback").forGetter(Lookup::fallback)
		).apply(i, (type, values, fallback) -> new Lookup(values, fallback)));
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
