package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:crossbow/charged" boolean property.
 */
public class JPropertyCrossbowCharged extends JProperty {
	public static final String TYPE = "minecraft:crossbow/charged";
	public static final MapCodec<JPropertyCrossbowCharged> CODEC = MapCodec.unit(JPropertyCrossbowCharged::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyCrossbowCharged() {
		super(TYPE);
	}

	// Static factory method
	public static JPropertyCrossbowCharged crossbowCharged() {
		return new JPropertyCrossbowCharged();
	}
}