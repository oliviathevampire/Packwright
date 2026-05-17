package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:angle" numeric property.
 */
public class JPropertyAngle extends JProperty {
    public static final String TYPE = "minecraft:angle";

    private boolean wobble = true; // Default is true

    protected JPropertyAngle() {
        super("minecraft:angle");
    }

    public JPropertyAngle(boolean wobble) {
        super("minecraft:angle");
        this.wobble = wobble;
    }

    // Static factory method
    public static JPropertyAngle angle() {
        return new JPropertyAngle();
    }

    // Fluent method
    public JPropertyAngle wobble(boolean wobble) {
        this.wobble = wobble;
        return this;
    }

    // Getter
    public boolean isWobble() {
        return wobble;
    }

    public static final MapCodec<JPropertyAngle> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.fieldOf("wobble").forGetter(JPropertyAngle::isWobble)
    ).apply(i, JPropertyAngle::new));

    static {
        JProperty.register(TYPE, CODEC);
    }
}
