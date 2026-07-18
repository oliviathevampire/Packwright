package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;

public class SetOminousBottleAmplifierFunction extends LootFunction {
	public SetOminousBottleAmplifierFunction(NumberProvider amplifier) {
		super("minecraft:set_ominous_bottle_amplifier");
		parameter("amplifier", amplifier);
	}
}
