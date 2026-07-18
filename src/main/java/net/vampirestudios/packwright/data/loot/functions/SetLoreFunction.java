package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class SetLoreFunction extends LootFunction {
	public SetLoreFunction(ListOperation mode, String... lines) {
		super("minecraft:set_lore");
		mode(mode);
		parameter("lore", List.of(lines));
	}

	public SetLoreFunction(ListOperation mode, EntityTarget entity, String... lines) {
		this(mode, lines);
		parameter("entity", entity.id());
	}
}
