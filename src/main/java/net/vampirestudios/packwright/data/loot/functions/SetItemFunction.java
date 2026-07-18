package net.vampirestudios.packwright.data.loot.functions;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class SetItemFunction extends LootFunction {
	public SetItemFunction(Identifier item) {
		super("minecraft:set_item");
		parameter("item", item);
	}
}
