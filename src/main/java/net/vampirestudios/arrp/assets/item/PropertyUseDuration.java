package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:use_duration" numeric property.
 */
public class PropertyUseDuration extends Property {
	public static final MapCodec<PropertyUseDuration> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.BOOL.optionalFieldOf("remaining", false).forGetter(PropertyUseDuration::isRemaining)
	).apply(i, rem -> new PropertyUseDuration().remaining(rem)));

	static {
		Property.register("minecraft:use_duration", CODEC);
	}

	private boolean remaining = false;

	protected PropertyUseDuration() {
		super("minecraft:use_duration");
	}

	public static PropertyUseDuration useDuration() {
		return new PropertyUseDuration();
	}

	public PropertyUseDuration remaining(boolean remaining) {
		this.remaining = remaining;
		return this;
	}

	public boolean isRemaining() {
		return remaining;
	}
}
