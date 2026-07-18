package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class SetInstrumentFunction extends LootFunction {
	public SetInstrumentFunction(String... options) {
		super("minecraft:set_instrument");
		parameter("options", List.of(options));
	}
}
