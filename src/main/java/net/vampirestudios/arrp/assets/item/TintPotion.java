package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** Uses the item's potion color; falls back to 'default' RGB if absent. */
public final class TintPotion extends Tint {
	public static final String TYPE = "minecraft:potion";
	public static final Codec<TintPotion> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TintPotion::defaultRgb)
	).apply(i, TintPotion::new));

	static {
		Tint.register(TYPE, CODEC);
	}

	private final int defaultRgb;

	public TintPotion(int defaultRgb) {
		super(TYPE);
		this.defaultRgb = defaultRgb;
	}

	public int defaultRgb() {
		return defaultRgb;
	}

	@Override
	public TintPotion clone() {
		return new TintPotion(defaultRgb);
	}
}
