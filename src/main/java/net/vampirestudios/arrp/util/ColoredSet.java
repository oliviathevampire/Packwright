package net.vampirestudios.arrp.util;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.Map;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class ColoredSet {
	private final Map<DyeColor, Identifier> values = new EnumMap<>(DyeColor.class);

	public ColoredSet(String suffix) {
		for (DyeColor color : DyeColor.values()) {
			values.put(color, vanillaId(color.getName() + suffix));
		}
	}

	public Identifier get(DyeColor color) {
		return values.get(color);
	}

	public Identifier white() {
		return get(DyeColor.WHITE);
	}

	public Identifier lightGray() {
		return get(DyeColor.LIGHT_GRAY);
	}

	public Identifier gray() {
		return get(DyeColor.GRAY);
	}

	public Identifier black() {
		return get(DyeColor.BLACK);
	}

	public Identifier brown() {
		return get(DyeColor.BROWN);
	}

	public Identifier red() {
		return get(DyeColor.RED);
	}

	public Identifier orange() {
		return get(DyeColor.ORANGE);
	}

	public Identifier yellow() {
		return get(DyeColor.YELLOW);
	}

	public Identifier lime() {
		return get(DyeColor.LIME);
	}

	public Identifier green() {
		return get(DyeColor.GREEN);
	}

	public Identifier cyan() {
		return get(DyeColor.CYAN);
	}

	public Identifier lightBlue() {
		return get(DyeColor.LIGHT_BLUE);
	}

	public Identifier blue() {
		return get(DyeColor.BLUE);
	}

	public Identifier purple() {
		return get(DyeColor.PURPLE);
	}

	public Identifier magenta() {
		return get(DyeColor.MAGENTA);
	}

	public Identifier pink() {
		return get(DyeColor.PINK);
	}
}