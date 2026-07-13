package net.vampirestudios.packwright.data.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

/**
 * An enchantment provider in the {@code enchantment_provider} registry: rolls
 * enchantments for naturally spawned equipment and similar contexts.
 *
 * <pre>{@code
 * EnchantmentProvider.single(vanillaId("fire_aspect"), 2)
 * EnchantmentProvider.byCost(Enchantments.tag(vanillaId("on_random_loot")), IntProvider.uniform(5, 20))
 * }</pre>
 */
public sealed interface EnchantmentProvider permits SingleEnchantmentProvider, ByCostEnchantmentProvider, ByCostWithDifficultyEnchantmentProvider {
	Codec<EnchantmentProvider> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<EnchantmentProvider, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "single" -> SingleEnchantmentProvider.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "by_cost" -> ByCostEnchantmentProvider.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "by_cost_with_difficulty" -> ByCostWithDifficultyEnchantmentProvider.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported enchantment provider type");
			});
		}

		@Override
		public <T> DataResult<T> encode(EnchantmentProvider input, DynamicOps<T> ops, T prefix) {
			if (input instanceof SingleEnchantmentProvider p) return SingleEnchantmentProvider.CODEC.codec().encode(p, ops, prefix);
			if (input instanceof ByCostEnchantmentProvider p) return ByCostEnchantmentProvider.CODEC.codec().encode(p, ops, prefix);
			if (input instanceof ByCostWithDifficultyEnchantmentProvider p) return ByCostWithDifficultyEnchantmentProvider.CODEC.codec().encode(p, ops, prefix);
			return DataResult.error(() -> "Unsupported enchantment provider: " + input.getClass().getSimpleName());
		}
	};

	/** {@code minecraft:single}: always the given enchantment at a fixed level */
	static SingleEnchantmentProvider single(Identifier enchantment, int level) {
		return new SingleEnchantmentProvider(enchantment, IntProvider.constant(level));
	}

	/** {@code minecraft:single} with a random level */
	static SingleEnchantmentProvider single(Identifier enchantment, IntProvider level) {
		return new SingleEnchantmentProvider(enchantment, level);
	}

	/** {@code minecraft:by_cost}: enchants like an enchanting table at the given cost */
	static ByCostEnchantmentProvider byCost(Enchantments enchantments, IntProvider cost) {
		return new ByCostEnchantmentProvider(enchantments, cost);
	}

	/** {@code minecraft:by_cost_with_difficulty}: cost scales with local difficulty */
	static ByCostWithDifficultyEnchantmentProvider byCostWithDifficulty(Enchantments enchantments, int minCost, int maxCostSpan) {
		return new ByCostWithDifficultyEnchantmentProvider(enchantments, minCost, maxCostSpan);
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
