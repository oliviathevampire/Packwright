package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

/** {@code minecraft:by_cost}: enchants like an enchanting table at the given cost */
public record ByCostEnchantmentProvider(Enchantments enchantments, IntProvider cost) implements EnchantmentProvider {
	public static final MapCodec<ByCostEnchantmentProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:by_cost"),
			Enchantments.CODEC.fieldOf("enchantments").forGetter(ByCostEnchantmentProvider::enchantments),
			IntProvider.CODEC.fieldOf("cost").forGetter(ByCostEnchantmentProvider::cost)
	).apply(i, (type, enchantments, cost) -> new ByCostEnchantmentProvider(enchantments, cost)));
}
