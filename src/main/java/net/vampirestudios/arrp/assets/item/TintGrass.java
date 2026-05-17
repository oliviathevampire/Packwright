package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** minecraft:grass — Colormap(temperature,downfall). Fields: temperature (0..1), downfall (0..1) */
public final class TintGrass extends Tint {
	public static final String TYPE = "minecraft:grass";
	public static final Codec<TintGrass> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("temperature").forGetter(TintGrass::temperature),
			ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("downfall").forGetter(TintGrass::downfall)
	).apply(i, TintGrass::new));

	static {
		Tint.register(TYPE, CODEC);
	}

	private final float temperature;
	private final float downfall;

	public TintGrass(float temperature, float downfall) {
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
	public TintGrass clone() {
		return new TintGrass(temperature, downfall);
	}
}
