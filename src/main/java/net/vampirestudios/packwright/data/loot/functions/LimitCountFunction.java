package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.predicate.Range;

public class LimitCountFunction extends LootFunction {
	public LimitCountFunction(Range limit) {
		super("minecraft:limit_count");
		parameter("limit", limit);
	}
}
