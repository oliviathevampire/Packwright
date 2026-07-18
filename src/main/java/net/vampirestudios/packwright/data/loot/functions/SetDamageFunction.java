package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;

public class SetDamageFunction extends LootFunction {
	public SetDamageFunction(NumberProvider damage) {
		super("minecraft:set_damage");
		parameter("damage", damage);
	}

	public SetDamageFunction(NumberProvider damage, boolean add) {
		this(damage);
		parameter("add", add);
	}
}
