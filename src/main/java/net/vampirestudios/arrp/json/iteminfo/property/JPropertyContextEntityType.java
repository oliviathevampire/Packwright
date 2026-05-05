package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:context_entity_type" property.
 */
public class JPropertyContextEntityType extends JProperty {
    public static final String TYPE = "minecraft:context_entity_type";
    public JPropertyContextEntityType() {
        super("minecraft:context_entity_type");
    }

    public static final MapCodec<JPropertyContextEntityType> CODEC = MapCodec.unit(JPropertyContextEntityType::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
