package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:custom_model_data" property.
 */
public class PropertyCustomModelData extends Property {
    public static final String TYPE = "minecraft:custom_model_data";
    private int index = 0; // Default value

    public PropertyCustomModelData() {
        super(TYPE);
    }

    public static PropertyCustomModelData of(int index) {
        PropertyCustomModelData customModelData = new PropertyCustomModelData();
        customModelData.index = index;
        return customModelData;
    }

    // Getter and Setter
    public int getIndex() {
        return index;
    }

    public static final MapCodec<PropertyCustomModelData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.fieldOf("index").forGetter(PropertyCustomModelData::getIndex)
    ).apply(i, PropertyCustomModelData::of));

    static {
        Property.register(TYPE, CODEC);
    }
}
