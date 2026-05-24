package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:count" numeric property.
 */
public class PropertyCount extends Property {
    public static final String TYPE = "minecraft:count";
    private boolean normalize = true; // Default is true

    protected PropertyCount() {
        super(TYPE);
    }

    public PropertyCount(boolean normalize) {
        super(TYPE);
        this.normalize = normalize;
    }

    // Static factory method
    public static PropertyCount count() {
        return new PropertyCount().normalize(false);
    }

    public static PropertyCount count(boolean normalize) {
        return new PropertyCount().normalize(normalize);
    }

    // Fluent method
    public PropertyCount normalize(boolean normalize) {
        this.normalize = normalize;
        return this;
    }

    // Getter
    public boolean isNormalize() {
        return normalize;
    }

    public static final MapCodec<PropertyCount> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.fieldOf("normalize").forGetter(PropertyCount::isNormalize)
    ).apply(i, PropertyCount::new));

    static {
        Property.register(TYPE, CODEC);
    }
}
