package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class CopyComponentsFunction extends LootFunction {
	public CopyComponentsFunction() {
		this("block_entity");
	}

	public CopyComponentsFunction(EntityTarget source) {
		this(source.id());
	}

	public CopyComponentsFunction(String source) {
		super("minecraft:copy_components");
		parameter("source", source);
	}
}
