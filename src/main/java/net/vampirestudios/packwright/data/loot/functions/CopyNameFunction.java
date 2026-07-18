package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

public class CopyNameFunction extends LootFunction {
	public CopyNameFunction() {
		super("minecraft:copy_name");
		parameter("source", "block_entity");
	}
}
