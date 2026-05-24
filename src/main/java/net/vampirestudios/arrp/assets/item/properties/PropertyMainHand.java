package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:main_hand" property.
 */
public class PropertyMainHand extends Property {
	// empty object -> only "property" is emitted
	public static final MapCodec<PropertyMainHand> CODEC = MapCodec.unit(PropertyMainHand::new);

	static {
		Property.register("minecraft:main_hand", CODEC);
	}

	public PropertyMainHand() {
		super("minecraft:main_hand");
	}

	public static PropertyMainHand mainHand() {
		return new PropertyMainHand();
	}
}
