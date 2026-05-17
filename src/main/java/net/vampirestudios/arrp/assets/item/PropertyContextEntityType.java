package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:context_entity_type" property.
 */
public class PropertyContextEntityType extends Property {
    public static final String TYPE = "minecraft:context_entity_type";
    public PropertyContextEntityType() {
        super("minecraft:context_entity_type");
    }

    public static final MapCodec<PropertyContextEntityType> CODEC = MapCodec.unit(PropertyContextEntityType::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
