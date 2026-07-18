package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.registry.enchantment.LevelBasedValue;

public record EnchantmentLevelNumberProvider(LevelBasedValue amount) implements NumberProvider {
	public static final MapCodec<EnchantmentLevelNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:enchantment_level"),
			LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentLevelNumberProvider::amount)
	).apply(i, (type, amount) -> new EnchantmentLevelNumberProvider(amount)));
}
