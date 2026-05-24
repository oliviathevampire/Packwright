package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:view_entity" boolean property.
 */
public class PropertyViewEntity extends Property {
	public static final MapCodec<PropertyViewEntity> CODEC = MapCodec.unit(PropertyViewEntity::new);

	static {
		Property.register("minecraft:view_entity", CODEC);
	}

	public PropertyViewEntity() {
		super("minecraft:view_entity");
	}
}
