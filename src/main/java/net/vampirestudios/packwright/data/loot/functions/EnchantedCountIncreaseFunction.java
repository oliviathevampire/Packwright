package net.vampirestudios.packwright.data.loot.functions;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;

public class EnchantedCountIncreaseFunction extends LootFunction {
	public EnchantedCountIncreaseFunction(Identifier enchantment, NumberProvider count) {
		super("minecraft:enchanted_count_increase");
		parameter("enchantment", enchantment);
		parameter("count", count);
	}

	public EnchantedCountIncreaseFunction(Identifier enchantment, NumberProvider count, int limit) {
		this(enchantment, count);
		parameter("limit", limit);
	}
}
