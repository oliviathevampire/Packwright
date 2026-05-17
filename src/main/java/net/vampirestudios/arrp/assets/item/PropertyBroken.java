package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:broken" boolean property.
 */
public class PropertyBroken extends Property {
    public static final String TYPE = "minecraft:broken";
    protected PropertyBroken() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyBroken broken() {
        return new PropertyBroken();
    }

    public static final MapCodec<PropertyBroken> CODEC = MapCodec.unit(PropertyBroken::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
