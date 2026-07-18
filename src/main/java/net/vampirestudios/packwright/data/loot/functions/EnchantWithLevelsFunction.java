package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;

import java.util.List;

public class EnchantWithLevelsFunction extends LootFunction {
	public EnchantWithLevelsFunction(NumberProvider levels) {
		super("minecraft:enchant_with_levels");
		parameter("levels", levels);
	}

	public EnchantWithLevelsFunction(NumberProvider levels, String... options) {
		this(levels);
		parameter("options", List.of(options));
	}
}
