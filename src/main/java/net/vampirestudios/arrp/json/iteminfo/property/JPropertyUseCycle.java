package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:use_cycle" numeric property.
 */
public class JPropertyUseCycle extends JProperty {
	public static final String TYPE = "minecraft:use_cycle";
	public static final MapCodec<JPropertyUseCycle> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.FLOAT.fieldOf("period").forGetter(JPropertyUseCycle::getPeriod)
	).apply(i, JPropertyUseCycle::new));

	static {
		JProperty.register(TYPE, CODEC.xmap(x -> x, x -> x));
	}

	private float period = 1.0f; // Default period is 1.0

	protected JPropertyUseCycle() {
		super(TYPE);
	}

	// Fluent method

	public JPropertyUseCycle(float period) {
		super(TYPE);
		this.period = period;
	}

	// Static factory method
	public static JPropertyUseCycle useCycle() {
		return new JPropertyUseCycle();
	}

	/**
	 * Sets the period for the use cycle.
	 *
	 * @param period The period in ticks for the cycle.
	 * @return The current instance for method chaining.
	 */
	public JPropertyUseCycle period(float period) {
		this.period = period;
		return this;
	}

	// Getter
	public float getPeriod() {
		return period;
	}
}
