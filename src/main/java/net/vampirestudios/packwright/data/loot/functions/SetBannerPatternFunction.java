package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

public class SetBannerPatternFunction extends LootFunction {
	public SetBannerPatternFunction(boolean append) {
		super("minecraft:set_banner_pattern");
		parameter("append", append);
	}
}
