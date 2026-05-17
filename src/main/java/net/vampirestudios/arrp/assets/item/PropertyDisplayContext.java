package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "display_context" string property.
 */
public class PropertyDisplayContext extends Property {
	public static final String TYPE = "minecraft:display_context";
	public static final MapCodec<PropertyDisplayContext> CODEC = MapCodec.unit(PropertyDisplayContext::new);

	static {
		Property.register(TYPE, CODEC);
	}

	protected PropertyDisplayContext() {
		super(TYPE);
	}

	// Static factory method
	public static PropertyDisplayContext displayContext() {
		return new PropertyDisplayContext();
	}
}
