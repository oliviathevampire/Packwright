package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:crossbow/charged" boolean property.
 */
public class PropertyCrossbowCharged extends Property {
	public static final String TYPE = "minecraft:crossbow/charged";
	public static final MapCodec<PropertyCrossbowCharged> CODEC = MapCodec.unit(PropertyCrossbowCharged::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyCrossbowCharged() {
		super(TYPE);
	}

	// Static factory method
	public static PropertyCrossbowCharged crossbowCharged() {
		return new PropertyCrossbowCharged();
	}
}