package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** minecraft:map_color — Map color or default. Fields: default (RGB) */
public final class JTintMapColor extends JTint {
	public static final String TYPE = "minecraft:map_color";
	public static final Codec<JTintMapColor> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(JTintMapColor::defaultRgb)
	).apply(i, JTintMapColor::new));

	static {
		JTint.register(TYPE, CODEC);
	}

	private final int def;

	public JTintMapColor(int def) {
		super(TYPE);
		this.def = def;
	}

	public int defaultRgb() {
		return def;
	}

	@Override
	public JTintMapColor clone() {
		return new JTintMapColor(def);
	}
}
