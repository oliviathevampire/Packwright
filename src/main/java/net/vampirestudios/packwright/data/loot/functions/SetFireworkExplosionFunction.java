package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.List;

public class SetFireworkExplosionFunction extends LootFunction {
	public SetFireworkExplosionFunction(String shape, List<Integer> colors, List<Integer> fadeColors, Boolean trail, Boolean twinkle) {
		super("minecraft:set_firework_explosion");
		if (shape != null) {
			parameter("shape", shape);
		}
		if (colors != null) {
			parameter("colors", colors);
		}
		if (fadeColors != null) {
			parameter("fade_colors", fadeColors);
		}
		if (trail != null) {
			parameter("trail", trail);
		}
		if (twinkle != null) {
			parameter("twinkle", twinkle);
		}
	}
}
