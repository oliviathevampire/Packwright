package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "charge_type" string property.
 */
public class JPropertyChargeType extends JProperty {
    public static final String TYPE = "minecraft:charge_type";
    protected JPropertyChargeType() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyChargeType chargeType() {
        return new JPropertyChargeType();
    }

    public static final MapCodec<JPropertyChargeType> CODEC = MapCodec.unit(JPropertyChargeType::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
