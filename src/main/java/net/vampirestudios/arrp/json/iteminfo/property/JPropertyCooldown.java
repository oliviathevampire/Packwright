package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:cooldown" numeric property.
 */
public class JPropertyCooldown extends JProperty {
    public static final String TYPE = "minecraft:cooldown";
    protected JPropertyCooldown() {
        super("minecraft:cooldown");
    }

    // Static factory method
    public static JPropertyCooldown cooldown() {
        return new JPropertyCooldown();
    }

    public static final MapCodec<JPropertyCooldown> CODEC = MapCodec.unit(JPropertyCooldown::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
