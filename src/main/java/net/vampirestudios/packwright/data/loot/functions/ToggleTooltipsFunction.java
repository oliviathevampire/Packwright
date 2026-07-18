package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

public class ToggleTooltipsFunction extends LootFunction {
	public ToggleTooltipsFunction(TooltipToggle... toggles) {
		super("minecraft:toggle_tooltips");
		for (TooltipToggle toggle : toggles) {
			subMap("toggles").put(toggle.component(), toggle.show());
		}
	}
}
