package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.nbt.LootNbtSource;

public class CopyCustomDataFunction extends LootFunction {
	public CopyCustomDataFunction(LootNbtSource source) {
		super("minecraft:copy_custom_data");
		put("source", source.value());
	}
}
