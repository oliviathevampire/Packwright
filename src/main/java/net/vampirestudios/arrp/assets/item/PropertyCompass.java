package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:compass" numeric property.
 */
public class PropertyCompass extends Property {
    public static final String TYPE = "minecraft:compass";
    private String target; // Default value
    private boolean wobble;   // Default: true

    public PropertyCompass() {
        super(TYPE);
    }

    public static PropertyCompass of(String target, boolean wobble) {
        return new PropertyCompass().wobble(wobble).target(target);
    }

    // Getters and Setters
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target for the compass.
     * Possible values: "spawn", "lodestone", "recovery", "none"
     */
    public PropertyCompass target(String target) {
        this.target = target;
        return this;
    }

    public boolean shouldWobble() {
        return wobble;
    }

    public PropertyCompass wobble(boolean wobble) {
        this.wobble = wobble;
        return this;
    }

    public static final MapCodec<PropertyCompass> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.fieldOf("target").forGetter(PropertyCompass::getTarget),
            Codec.BOOL.optionalFieldOf("wobble", true).forGetter(PropertyCompass::shouldWobble)
    ).apply(i, PropertyCompass::of));

    static {
        Property.register(TYPE, CODEC);
    }
}
