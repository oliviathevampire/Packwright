package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:use_cycle" numeric property.
 */
public class PropertyUseCycle extends Property {
	public static final String TYPE = "minecraft:use_cycle";
	public static final MapCodec<PropertyUseCycle> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.FLOAT.fieldOf("period").forGetter(PropertyUseCycle::getPeriod)
	).apply(i, PropertyUseCycle::new));

	static {
		Property.register(TYPE, CODEC);
	}

	private float period = 1.0f; // Default period is 1.0

	protected PropertyUseCycle() {
		super(TYPE);
	}

	// Fluent method

	public PropertyUseCycle(float period) {
		super(TYPE);
		this.period = period;
	}

	// Static factory method
	public static PropertyUseCycle useCycle() {
		return new PropertyUseCycle();
	}

	/**
	 * Sets the period for the use cycle.
	 *
	 * @param period The period in ticks for the cycle.
	 * @return The current instance for method chaining.
	 */
	public PropertyUseCycle period(float period) {
		this.period = period;
		return this;
	}

	// Getter
	public float getPeriod() {
		return period;
	}
}
