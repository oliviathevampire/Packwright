package net.vampirestudios.packwright.assets.item.tints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** minecraft:map_color — Map color or default. Fields: default (RGB) */
public final class TintMapColor extends Tint {
	public static final String TYPE = "minecraft:map_color";
	public static final Codec<TintMapColor> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TintMapColor::defaultRgb)
	).apply(i, TintMapColor::new));

	static {
		Tint.register(TYPE, CODEC);
	}

	private final int def;

	public TintMapColor(int def) {
		super(TYPE);
		this.def = def;
	}

	public int defaultRgb() {
		return def;
	}
}
