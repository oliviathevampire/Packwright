package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** {@code minecraft:by_cost_with_difficulty}: cost scales with local difficulty */
public record ByCostWithDifficultyEnchantmentProvider(
		Enchantments enchantments,
		int minCost,
		int maxCostSpan
) implements EnchantmentProvider {
	public static final MapCodec<ByCostWithDifficultyEnchantmentProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:by_cost_with_difficulty"),
			Enchantments.CODEC.fieldOf("enchantments").forGetter(ByCostWithDifficultyEnchantmentProvider::enchantments),
			Codec.INT.fieldOf("min_cost").forGetter(ByCostWithDifficultyEnchantmentProvider::minCost),
			Codec.INT.fieldOf("max_cost_span").forGetter(ByCostWithDifficultyEnchantmentProvider::maxCostSpan)
	).apply(i, (type, enchantments, minCost, maxCostSpan) -> new ByCostWithDifficultyEnchantmentProvider(enchantments, minCost, maxCostSpan)));
}
