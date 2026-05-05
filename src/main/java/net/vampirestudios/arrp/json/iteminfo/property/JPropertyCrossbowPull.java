package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:crossbow/pull" numeric property.
 */
public class JPropertyCrossbowPull extends JProperty {
	public static final String TYPE = "minecraft:crossbow/pull";
	public static final MapCodec<JPropertyCrossbowPull> CODEC = MapCodec.unit(JPropertyCrossbowPull::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyCrossbowPull() {
		super("minecraft:crossbow/pull");
	}

	// Static factory method
	public static JPropertyCrossbowPull crossbowPull() {
		return new JPropertyCrossbowPull();
	}
}
