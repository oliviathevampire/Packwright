package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class SetRandomPotionFunction extends LootFunction {
	public SetRandomPotionFunction(String... options) {
		super("minecraft:set_random_potion");
		if (options.length > 0) {
			parameter("options", List.of(options));
		}
	}
}
