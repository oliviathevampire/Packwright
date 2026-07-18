package net.vampirestudios.packwright.data.loot.functions;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.LootFunction;

public class ApplyBonusCountFunction extends LootFunction {
	public ApplyBonusCountFunction(Identifier enchantment, ApplyBonusFormula formula) {
		super("minecraft:apply_bonus");
		parameter("enchantment", enchantment);
		parameter("formula", formula.id());
		Object parameters = formula.parameters();
		if (parameters != null) {
			put("parameters", parameters);
		}
	}
}
