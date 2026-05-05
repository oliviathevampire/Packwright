package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:charge" numeric property.
 */
public class JPropertyCharge extends JProperty {
    public static final String TYPE = "minecraft:charge";
    protected JPropertyCharge() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyCharge charge() {
        return new JPropertyCharge();
    }

    public static final MapCodec<JPropertyCharge> CODEC = MapCodec.unit(JPropertyCharge::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
