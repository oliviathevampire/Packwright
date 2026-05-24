package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:throwing" boolean property.
 */
public class PropertyThrowing extends Property {
	// empty object -> only "property" is emitted
	public static final MapCodec<PropertyThrowing> CODEC = MapCodec.unit(PropertyThrowing::new);

	static {
		Property.register("minecraft:throwing", CODEC);
	}

	protected PropertyThrowing() {
		super("minecraft:throwing");
	}

	// Static factory method
	public static PropertyThrowing throwing() {
		return new PropertyThrowing();
	}
}
