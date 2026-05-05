package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "fishing_rod/cast" boolean property.
 */
public class JPropertyFishingRodCast extends JProperty {
    public static final String TYPE = "minecraft:fishing_rod/cast";
    protected JPropertyFishingRodCast() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyFishingRodCast fishingRodCast() {
        return new JPropertyFishingRodCast();
    }

    public static final MapCodec<JPropertyFishingRodCast> CODEC = MapCodec.unit(JPropertyFishingRodCast::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
