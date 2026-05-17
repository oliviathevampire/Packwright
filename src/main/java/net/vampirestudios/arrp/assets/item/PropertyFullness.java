package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:fullness" numeric property.
 */
public class PropertyFullness extends Property {
	public static final String TYPE = "minecraft:bundle/fullness";
	public static final MapCodec<PropertyFullness> CODEC = MapCodec.unit(PropertyFullness::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyFullness() {
		super(TYPE);
	}

	// Static factory method
	public static PropertyFullness fullness() {
		return new PropertyFullness();
	}
}
