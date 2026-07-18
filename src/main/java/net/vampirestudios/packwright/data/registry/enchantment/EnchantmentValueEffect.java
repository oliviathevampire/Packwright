package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * modifies a numeric value based on the enchantment level, e.g. {@code minecraft:add}. Used
 * by most {@code EnchantmentEffects} components ({@code damage}, {@code knockback}, ...).
 */
public sealed interface EnchantmentValueEffect permits
		EnchantmentValueEffect.Add,
		EnchantmentValueEffect.Multiply,
		EnchantmentValueEffect.Set,
		EnchantmentValueEffect.RemoveBinomial,
		EnchantmentValueEffect.ScaleExponentially,
		EnchantmentValueEffect.AllOf {

	Codec<EnchantmentValueEffect> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<EnchantmentValueEffect, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "add" -> Add.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "multiply" -> Multiply.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "set" -> Set.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "remove_binomial" -> RemoveBinomial.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "exponential" -> ScaleExponentially.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "all_of" -> AllOf.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported enchantment value effect type");
			});
		}

		@Override
		public <T> DataResult<T> encode(EnchantmentValueEffect input, DynamicOps<T> ops, T prefix) {
			if (input instanceof Add v) return Add.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof Multiply v) return Multiply.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof Set v) return Set.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof RemoveBinomial v) return RemoveBinomial.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof ScaleExponentially v) return ScaleExponentially.CODEC.codec().encode(v, ops, prefix);
			if (input instanceof AllOf v) return AllOf.CODEC.codec().encode(v, ops, prefix);
			return DataResult.error(() -> "Unsupported enchantment value effect: " + input.getClass().getSimpleName());
		}
	};

	static Add add(LevelBasedValue value) { return new Add(value); }
	static Multiply multiply(LevelBasedValue factor) { return new Multiply(factor); }
	static Set set(LevelBasedValue value) { return new Set(value); }
	static RemoveBinomial removeBinomial(LevelBasedValue chance) { return new RemoveBinomial(chance); }
	static ScaleExponentially exponential(LevelBasedValue base, LevelBasedValue exponent) { return new ScaleExponentially(base, exponent); }
	static AllOf allOf(EnchantmentValueEffect... effects) { return new AllOf(List.of(effects)); }

	record Add(LevelBasedValue value) implements EnchantmentValueEffect {
		static final MapCodec<Add> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:add"),
				LevelBasedValue.CODEC.fieldOf("value").forGetter(Add::value)
		).apply(i, (type, value) -> new Add(value)));
	}

	record Multiply(LevelBasedValue factor) implements EnchantmentValueEffect {
		static final MapCodec<Multiply> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:multiply"),
				LevelBasedValue.CODEC.fieldOf("factor").forGetter(Multiply::factor)
		).apply(i, (type, factor) -> new Multiply(factor)));
	}

	record Set(LevelBasedValue value) implements EnchantmentValueEffect {
		static final MapCodec<Set> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:set"),
				LevelBasedValue.CODEC.fieldOf("value").forGetter(Set::value)
		).apply(i, (type, value) -> new Set(value)));
	}

	record RemoveBinomial(LevelBasedValue chance) implements EnchantmentValueEffect {
		static final MapCodec<RemoveBinomial> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:remove_binomial"),
				LevelBasedValue.CODEC.fieldOf("chance").forGetter(RemoveBinomial::chance)
		).apply(i, (type, chance) -> new RemoveBinomial(chance)));
	}

	/** vanilla's JSON type id for this is {@code "exponential"} (the class is {@code ScaleExponentially}) */
	record ScaleExponentially(LevelBasedValue base, LevelBasedValue exponent) implements EnchantmentValueEffect {
		static final MapCodec<ScaleExponentially> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:exponential"),
				LevelBasedValue.CODEC.fieldOf("base").forGetter(ScaleExponentially::base),
				LevelBasedValue.CODEC.fieldOf("exponent").forGetter(ScaleExponentially::exponent)
		).apply(i, (type, base, exponent) -> new ScaleExponentially(base, exponent)));
	}

	record AllOf(List<EnchantmentValueEffect> effects) implements EnchantmentValueEffect {
		static final MapCodec<AllOf> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:all_of"),
				EnchantmentValueEffect.CODEC.listOf().fieldOf("effects").forGetter(AllOf::effects)
		).apply(i, (type, effects) -> new AllOf(effects)));
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
