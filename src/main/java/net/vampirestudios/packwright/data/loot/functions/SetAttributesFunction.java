package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.Arrays;

public class SetAttributesFunction extends LootFunction {
	public SetAttributesFunction(LootAttributeModifier... modifiers) {
		super("minecraft:set_attributes");
		parameter("modifiers", Arrays.stream(modifiers).map(LootAttributeModifier::value).toList());
	}

	public SetAttributesFunction(boolean replace, LootAttributeModifier... modifiers) {
		this(modifiers);
		parameter("replace", replace);
	}
}
