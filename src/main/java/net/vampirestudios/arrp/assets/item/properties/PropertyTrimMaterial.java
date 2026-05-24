package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "trim_material" string property.
 */
public class PropertyTrimMaterial extends Property {
	public static final MapCodec<PropertyTrimMaterial> CODEC = MapCodec.unit(PropertyTrimMaterial::new);

	static {
		Property.register("minecraft:trim_material", CODEC);
	}

	protected PropertyTrimMaterial() {
		super("minecraft:trim_material");
	}

	public static PropertyTrimMaterial trimMaterial() {
		return new PropertyTrimMaterial();
	}
}
