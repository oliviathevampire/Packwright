package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:pull" numeric property.
 */
public class PropertyPull extends Property {
	public static final String TYPE = "minecraft:pull";
	public static final MapCodec<PropertyPull> CODEC = MapCodec.unit(PropertyPull::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyPull() {
		super(TYPE);
	}

	// Static factory method
	public static PropertyPull pull() {
		return new PropertyPull();
	}
}