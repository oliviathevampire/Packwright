package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:damaged" boolean property.
 */
public class PropertyDamaged extends Property {
    public static final String TYPE = "minecraft:damaged";
    protected PropertyDamaged() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyDamaged damaged() {
        return new PropertyDamaged();
    }

    public static final MapCodec<PropertyDamaged> CODEC = MapCodec.unit(PropertyDamaged::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
