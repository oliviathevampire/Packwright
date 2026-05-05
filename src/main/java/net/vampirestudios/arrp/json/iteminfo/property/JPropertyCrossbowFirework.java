package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:crossbow/firework" boolean property.
 */
public class JPropertyCrossbowFirework extends JProperty {
	public static final String TYPE = "minecraft:crossbow/firework";
	public static final MapCodec<JPropertyCrossbowFirework> CODEC = MapCodec.unit(JPropertyCrossbowFirework::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyCrossbowFirework() {
		super(TYPE);
	}

	// Static factory method
	public static JPropertyCrossbowFirework crossbowFirework() {
		return new JPropertyCrossbowFirework();
	}
}