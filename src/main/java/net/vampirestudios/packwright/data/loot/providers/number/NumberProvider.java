package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.registry.enchantment.LevelBasedValue;

import java.util.Arrays;

/**
 * A loot number provider: a constant, a random distribution, a scoreboard lookup,
 * storage lookup, enchantment/environment value, or sum.
 */
public interface NumberProvider {
	Codec<NumberProvider> DISPATCH_CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<NumberProvider, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "constant" -> ConstantNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "uniform" -> UniformNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "binomial" -> BinomialNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "score" -> ScoreNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "storage" -> StorageNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "sum" -> SumNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "enchantment_level" -> EnchantmentLevelNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "environment_attribute" -> EnvironmentAttributeNumberProvider.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unknown number provider type");
			});
		}

		@Override
		public <T> DataResult<T> encode(NumberProvider input, DynamicOps<T> ops, T prefix) {
			if (input instanceof ConstantNumberProvider provider) return ConstantNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof UniformNumberProvider provider) return UniformNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof BinomialNumberProvider provider) return BinomialNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof ScoreNumberProvider provider) return ScoreNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof StorageNumberProvider provider) return StorageNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof SumNumberProvider provider) return SumNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof EnchantmentLevelNumberProvider provider) return EnchantmentLevelNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			if (input instanceof EnvironmentAttributeNumberProvider provider) return EnvironmentAttributeNumberProvider.MAP_CODEC.codec().encode(provider, ops, prefix);
			return DataResult.error(() -> "Unsupported number provider: " + input.getClass().getSimpleName());
		}
	};
	Codec<NumberProvider> DIRECT_CODEC = Codec.either(Codec.FLOAT, DISPATCH_CODEC).xmap(
			either -> either.map(ConstantNumberProvider::new, provider -> provider),
			provider -> provider instanceof ConstantNumberProvider(float amount) ? Either.left(amount) : Either.right(provider)
	);
	Codec<NumberProvider> CODEC = DIRECT_CODEC;

	static NumberProvider constant(Number value) {
		return new ConstantNumberProvider(value.floatValue());
	}

	static NumberProvider uniform(Number min, Number max) {
		return uniform(constant(min), constant(max));
	}

	static NumberProvider uniform(NumberProvider min, NumberProvider max) {
		return new UniformNumberProvider(min, max);
	}

	static NumberProvider binomial(int n, float p) {
		return binomial(constant(n), constant(p));
	}

	static NumberProvider binomial(NumberProvider n, NumberProvider p) {
		return new BinomialNumberProvider(n, p);
	}

	static NumberProvider score(EntityTarget target, String objective) {
		return new ScoreNumberProvider(target, objective, 1.0F);
	}

	static NumberProvider score(EntityTarget target, String objective, float scale) {
		return new ScoreNumberProvider(target, objective, scale);
	}

	static NumberProvider storage(Identifier storage, String path) {
		return new StorageNumberProvider(storage, path);
	}

	static NumberProvider enchantmentLevel(LevelBasedValue amount) {
		return new EnchantmentLevelNumberProvider(amount);
	}

	static NumberProvider environmentAttribute(Identifier attribute) {
		return new EnvironmentAttributeNumberProvider(attribute);
	}

	static NumberProvider sum(NumberProvider... summands) {
		return new SumNumberProvider(Arrays.asList(summands));
	}

	default Object value() {
		return CODEC.encodeStart(JavaOps.INSTANCE, this).getOrThrow();
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
