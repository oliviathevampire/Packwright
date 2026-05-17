package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:crossbow/pull" numeric property.
 */
public class PropertyCrossbowPull extends Property {
	public static final String TYPE = "minecraft:crossbow/pull";
	public static final MapCodec<PropertyCrossbowPull> CODEC = MapCodec.unit(PropertyCrossbowPull::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyCrossbowPull() {
		super("minecraft:crossbow/pull");
	}

	// Static factory method
	public static PropertyCrossbowPull crossbowPull() {
		return new PropertyCrossbowPull();
	}
}
