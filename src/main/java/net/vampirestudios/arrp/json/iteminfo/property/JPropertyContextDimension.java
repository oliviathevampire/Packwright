package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:context_dimension" property.
 */
public class JPropertyContextDimension extends JProperty {
    public static final String TYPE = "minecraft:context_dimension";
    public JPropertyContextDimension() {
        super(TYPE);
    }

    public static final MapCodec<JPropertyContextDimension> CODEC = MapCodec.unit(JPropertyContextDimension::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
