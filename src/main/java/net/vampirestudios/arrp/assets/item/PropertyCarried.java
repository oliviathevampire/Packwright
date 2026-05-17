package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "carried" string property.
 */
public class PropertyCarried extends Property {
    public static final String TYPE = "minecraft:carried";
    protected PropertyCarried() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyCarried carried() {
        return new PropertyCarried();
    }

    public static final MapCodec<PropertyCarried> CODEC = MapCodec.unit(PropertyCarried::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
