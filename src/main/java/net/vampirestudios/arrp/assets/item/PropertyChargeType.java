package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "charge_type" string property.
 */
public class PropertyChargeType extends Property {
    public static final String TYPE = "minecraft:charge_type";
    protected PropertyChargeType() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyChargeType chargeType() {
        return new PropertyChargeType();
    }

    public static final MapCodec<PropertyChargeType> CODEC = MapCodec.unit(PropertyChargeType::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
