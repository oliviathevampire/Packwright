package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class EnchantRandomlyFunction extends LootFunction {
	public EnchantRandomlyFunction(String... options) {
		super("minecraft:enchant_randomly");
		if (options.length > 0) {
			parameter("options", List.of(options));
		}
	}
}
