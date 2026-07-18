package net.vampirestudios.packwright.data.loot.functions;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class SetPotionFunction extends LootFunction {
	public SetPotionFunction(Identifier potion) {
		super("minecraft:set_potion");
		parameter("id", potion);
	}
}
