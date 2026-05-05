package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:custom_model_data" property.
 */
public class JPropertyCustomModelData extends JProperty {
    public static final String TYPE = "minecraft:custom_model_data";
    private int index = 0; // Default value

    public JPropertyCustomModelData() {
        super(TYPE);
    }

    public static JPropertyCustomModelData of(int index) {
        JPropertyCustomModelData customModelData = new JPropertyCustomModelData();
        customModelData.index = index;
        return customModelData;
    }

    // Getter and Setter
    public int getIndex() {
        return index;
    }

    public static final MapCodec<JPropertyCustomModelData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.fieldOf("index").forGetter(JPropertyCustomModelData::getIndex)
    ).apply(i, JPropertyCustomModelData::of));

    static {
        JProperty.register(TYPE, CODEC);
    }
}
