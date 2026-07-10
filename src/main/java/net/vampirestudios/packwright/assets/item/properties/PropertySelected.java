package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "selected" boolean property.
 */
public class PropertySelected extends Property {
	// empty object -> only "property" is emitted
	public static final MapCodec<PropertySelected> CODEC = MapCodec.unit(PropertySelected::new);

	static {
		Property.register("minecraft:selected", CODEC);
	}

	protected PropertySelected() {
		super("minecraft:selected");
	}

	// Static factory method
	public static PropertySelected selected() {
		return new PropertySelected();
	}
}
