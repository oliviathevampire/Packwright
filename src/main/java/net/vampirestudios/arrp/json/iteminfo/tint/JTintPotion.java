package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** Uses the item's potion color; falls back to 'default' RGB if absent. */
public final class JTintPotion extends JTint {
	public static final String TYPE = "minecraft:potion";
	public static final Codec<JTintPotion> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(JTintPotion::defaultRgb)
	).apply(i, JTintPotion::new));

	static {
		JTint.register(TYPE, CODEC);
	}

	private final int defaultRgb;

	public JTintPotion(int defaultRgb) {
		super(TYPE);
		this.defaultRgb = defaultRgb;
	}

	public int defaultRgb() {
		return defaultRgb;
	}

	@Override
	public JTintPotion clone() {
		return new JTintPotion(defaultRgb);
	}
}
