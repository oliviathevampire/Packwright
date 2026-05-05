package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:time" numeric property.
 */
public class JPropertyTime extends JProperty {
	public static final MapCodec<JPropertyTime> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.optionalFieldOf("source", "daytime").forGetter(JPropertyTime::getSource),
			Codec.BOOL.optionalFieldOf("wobble", true).forGetter(JPropertyTime::isWobble)
	).apply(i, (src, wob) -> {
		var p = new JPropertyTime();
		p.source(src);
		p.wobble(wob);
		return p;
	}));

	static {
		JProperty.register("minecraft:time", CODEC);
	}

	private String source = "daytime"; // "daytime", "moon_phase", "random"
	private boolean wobble = true;

	public JPropertyTime() {
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
