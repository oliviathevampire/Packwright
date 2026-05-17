package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:cooldown" numeric property.
 */
public class PropertyCooldown extends Property {
    public static final String TYPE = "minecraft:cooldown";
    protected PropertyCooldown() {
        super("minecraft:cooldown");
    }

    // Static factory method
    public static PropertyCooldown cooldown() {
        return new PropertyCooldown();
    }

    public static final MapCodec<PropertyCooldown> CODEC = MapCodec.unit(PropertyCooldown::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
