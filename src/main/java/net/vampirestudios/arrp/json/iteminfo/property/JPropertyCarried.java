package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "carried" string property.
 */
public class JPropertyCarried extends JProperty {
    public static final String TYPE = "minecraft:carried";
    protected JPropertyCarried() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyCarried carried() {
        return new JPropertyCarried();
    }

    public static final MapCodec<JPropertyCarried> CODEC = MapCodec.unit(JPropertyCarried::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
