package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:time" numeric property.
 */
public class PropertyTime extends Property {
	public static final MapCodec<PropertyTime> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.optionalFieldOf("source", "daytime").forGetter(PropertyTime::getSource),
			Codec.BOOL.optionalFieldOf("wobble", true).forGetter(PropertyTime::isWobble)
	).apply(i, (src, wob) -> {
		var p = new PropertyTime();
		p.source(src);
		p.wobble(wob);
		return p;
	}));

	static {
		Property.register("minecraft:time", CODEC);
	}

	private String source = "daytime"; // "daytime", "moon_phase", "random"
	private boolean wobble = true;

	public PropertyTime() {
		super("minecraft:time");
	}

	public String getSource() {
		return source;
	}

	public void source(String source) {
		this.source = source;
	}

	public boolean isWobble() {
		return wobble;
	}

	public void wobble(boolean wobble) {
		this.wobble = wobble;
	}
}
