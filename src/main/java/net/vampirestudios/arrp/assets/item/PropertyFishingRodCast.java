package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "fishing_rod/cast" boolean property.
 */
public class PropertyFishingRodCast extends Property {
    public static final String TYPE = "minecraft:fishing_rod/cast";
    protected PropertyFishingRodCast() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyFishingRodCast fishingRodCast() {
        return new PropertyFishingRodCast();
    }

    public static final MapCodec<PropertyFishingRodCast> CODEC = MapCodec.unit(PropertyFishingRodCast::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
