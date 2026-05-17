package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:pulling" boolean property.
 */
public class PropertyPulling extends Property {
	public static final String TYPE = "minecraft:pulling";
	public static final MapCodec<PropertyPulling> CODEC = MapCodec.unit(PropertyPulling::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyPulling() {
		super(TYPE);
	}

	// Static factory method
	public static PropertyPulling pulling() {
		return new PropertyPulling();
	}
}