package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:using_item" boolean property.
 */
public class PropertyUsingItem extends Property {
	public static final String TYPE = "minecraft:using_item";
	public static final MapCodec<PropertyUsingItem> CODEC = MapCodec.unit(PropertyUsingItem::new);

	static {
		Property.register(TYPE, CODEC);
	}

	public PropertyUsingItem() {
		super(TYPE);
	}
}
