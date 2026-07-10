package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:context_dimension" property.
 */
public class PropertyContextDimension extends Property {
    public static final String TYPE = "minecraft:context_dimension";
    public PropertyContextDimension() {
        super(TYPE);
    }

    public static final MapCodec<PropertyContextDimension> CODEC = MapCodec.unit(PropertyContextDimension::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
