package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** minecraft:grass — Colormap(temperature,downfall). Fields: temperature (0..1), downfall (0..1) */
public final class JTintGrass extends JTint {
	public static final String TYPE = "minecraft:grass";
	public static final Codec<JTintGrass> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("temperature").forGetter(JTintGrass::temperature),
			ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("downfall").forGetter(JTintGrass::downfall)
	).apply(i, JTintGrass::new));

	static {
		JTint.register(TYPE, CODEC);
	}

	private final float temperature;
	private final float downfall;

	public JTintGrass(float temperature, float downfall) {
		super(TYPE);
		this.temperature = temperature;
		this.downfall = downfall;
	}

	public float temperature() {
		return temperature;
	}

	public float downfall() {
		return downfall;
	}

	@Override
	public JTintGrass clone() {
		return new JTintGrass(temperature, downfall);
	}
}
