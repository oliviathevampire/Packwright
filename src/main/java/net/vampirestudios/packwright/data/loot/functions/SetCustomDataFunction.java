package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

public class SetCustomDataFunction extends LootFunction {
	public SetCustomDataFunction(String snbt) {
		super("minecraft:set_custom_data");
		parameter("tag", snbt);
	}
}
