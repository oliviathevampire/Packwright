package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class SetNameFunction extends LootFunction {
	public SetNameFunction(String name) {
		super("minecraft:set_name");
		parameter("name", name);
	}

	public SetNameFunction(String name, LootNameTarget target) {
		this(name);
		parameter("target", target.id());
	}

	public SetNameFunction(String name, LootNameTarget target, EntityTarget entity) {
		this(name, target);
		parameter("entity", entity.id());
	}
}
