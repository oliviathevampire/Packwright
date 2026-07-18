package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;

public class SetCountFunction extends LootFunction {
	public SetCountFunction(NumberProvider count) {
		super("minecraft:set_count");
		parameter("count", count);
	}

	public SetCountFunction(NumberProvider count, boolean add) {
		this(count);
		parameter("add", add);
	}
}
