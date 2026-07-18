package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.util.LootValue;

public class ModifyContentsFunction extends LootFunction {
	public ModifyContentsFunction(String component, LootFunction modifier) {
		super("minecraft:modify_contents");
		parameter("component", component);
		put("modifier", LootValue.encode(LootFunction.CODEC, modifier));
	}
}
