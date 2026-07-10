package net.vampirestudios.packwright.assets.item.tints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class TintConstant extends Tint {
	public static final String TYPE = "minecraft:constant";
	public static final Codec<TintConstant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("value").forGetter(TintConstant::color)
	).apply(i, TintConstant::new));

	static {
		Tint.register(TYPE, CODEC);
	}

	private final int value; // RGB or ARGB int (engine can add alpha if needed)

	public TintConstant(int value) {
		super(TYPE);
		this.value = value;
	}

	public static TintConstant of(int value) {
		return new TintConstant(value);
	}

	public int color() {
		return value;
	}
}
