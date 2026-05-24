package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:crossbow/firework" boolean property.
 */
public class PropertyCrossbowFirework extends Property {
	public static final String TYPE = "minecraft:crossbow/firework";
	public static final MapCodec<PropertyCrossbowFirework> CODEC = MapCodec.unit(PropertyCrossbowFirework::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyCrossbowFirework() {
		super(TYPE);
	}

	// Static factory method
	public static PropertyCrossbowFirework crossbowFirework() {
		return new PropertyCrossbowFirework();
	}
}