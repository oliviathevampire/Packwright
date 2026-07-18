package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;

public class SetRandomDyesFunction extends LootFunction {
	public SetRandomDyesFunction(NumberProvider numberOfDyes) {
		super("minecraft:set_random_dyes");
		parameter("number_of_dyes", numberOfDyes);
	}
}
