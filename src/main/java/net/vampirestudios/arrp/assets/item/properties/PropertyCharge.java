package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:charge" numeric property.
 */
public class PropertyCharge extends Property {
    public static final String TYPE = "minecraft:charge";
    protected PropertyCharge() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyCharge charge() {
        return new PropertyCharge();
    }

    public static final MapCodec<PropertyCharge> CODEC = MapCodec.unit(PropertyCharge::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
