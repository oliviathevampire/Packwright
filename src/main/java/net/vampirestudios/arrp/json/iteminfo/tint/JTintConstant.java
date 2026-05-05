package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class JTintConstant extends JTint {
	public static final String TYPE = "minecraft:constant";
	public static final Codec<JTintConstant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("value").forGetter(JTintConstant::color)
	).apply(i, JTintConstant::new));

	static {
		JTint.register(TYPE, CODEC);
	}

	private final int value; // RGB or ARGB int (engine can add alpha if needed)

	public JTintConstant(int value) {
		super(TYPE);
		this.value = value;
	}

	public static JTintConstant of(int value) {
		return new JTintConstant(value);
	}

	public int color() {
		return value;
	}

	@Override
	public JTintConstant clone() {
		return new JTintConstant(value);
	}
}
