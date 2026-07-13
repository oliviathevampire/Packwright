package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

/** {@code minecraft:single}: always the given enchantment, at a fixed or random level */
public record SingleEnchantmentProvider(Identifier enchantment, IntProvider level) implements EnchantmentProvider {
	public static final MapCodec<SingleEnchantmentProvider> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:single"),
			Identifier.CODEC.fieldOf("enchantment").forGetter(SingleEnchantmentProvider::enchantment),
			IntProvider.CODEC.fieldOf("level").forGetter(SingleEnchantmentProvider::level)
	).apply(i, (type, enchantment, level) -> new SingleEnchantmentProvider(enchantment, level)));
}
