package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:use_duration" numeric property.
 */
public class JPropertyUseDuration extends JProperty {
	public static final MapCodec<JPropertyUseDuration> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.BOOL.optionalFieldOf("remaining", false).forGetter(JPropertyUseDuration::isRemaining)
	).apply(i, rem -> new JPropertyUseDuration().remaining(rem)));

	static {
		JProperty.register("minecraft:use_duration", CODEC);
	}

	private boolean remaining = false;

	protected JPropertyUseDuration() {
		super("minecraft:use_duration");
	}

	public static JPropertyUseDuration useDuration() {
		return new JPropertyUseDuration();
	}

	public JPropertyUseDuration remaining(boolean remaining) {
		this.remaining = remaining;
		return this;
	}

	public boolean isRemaining() {
		return remaining;
	}
}
