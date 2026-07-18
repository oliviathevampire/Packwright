package net.vampirestudios.packwright.data.loot.functions;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class SetLootTableFunction extends LootFunction {
	public SetLootTableFunction(Identifier lootTable, Identifier blockEntityType) {
		super("minecraft:set_loot_table");
		parameter("name", lootTable);
		parameter("type", blockEntityType);
	}

	public SetLootTableFunction(Identifier lootTable, Identifier blockEntityType, long seed) {
		this(lootTable, blockEntityType);
		parameter("seed", seed);
	}
}
