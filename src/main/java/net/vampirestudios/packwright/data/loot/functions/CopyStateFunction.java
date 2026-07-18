package net.vampirestudios.packwright.data.loot.functions;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class CopyStateFunction extends LootFunction {
	public CopyStateFunction(Identifier block, String... properties) {
		super("minecraft:copy_state");
		parameter("block", block);
		parameter("properties", List.of(properties));
	}
}
