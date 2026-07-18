package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class FillPlayerHeadFunction extends LootFunction {
	public FillPlayerHeadFunction(EntityTarget entity) {
		super("minecraft:fill_player_head");
		parameter("entity", entity.id());
	}
}
