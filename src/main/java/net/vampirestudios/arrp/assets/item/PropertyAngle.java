package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:angle" numeric property.
 */
public class PropertyAngle extends Property {
    public static final String TYPE = "minecraft:angle";

    private boolean wobble = true; // Default is true

    protected PropertyAngle() {
        super("minecraft:angle");
    }

    public PropertyAngle(boolean wobble) {
        super("minecraft:angle");
        this.wobble = wobble;
    }

    // Static factory method
    public static PropertyAngle angle() {
        return new PropertyAngle();
    }

    // Fluent method
    public PropertyAngle wobble(boolean wobble) {
        this.wobble = wobble;
        return this;
    }

    // Getter
    public boolean isWobble() {
        return wobble;
    }

    public static final MapCodec<PropertyAngle> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.fieldOf("wobble").forGetter(PropertyAngle::isWobble)
    ).apply(i, PropertyAngle::new));

    static {
        Property.register(TYPE, CODEC);
    }
}
