package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:damage" numeric property.
 */
public class PropertyDamage extends Property {
    public static final String TYPE = "minecraft:damage";
    private boolean normalize = true; // Default: true

    public PropertyDamage() {
        super(TYPE);
    }

    public static PropertyDamage of(boolean normalize) {
        PropertyDamage count = new PropertyDamage();
        count.normalize = normalize;
        return count;
    }

    // Getter and Setter
    public boolean isNormalize() {
        return normalize;
    }

    public static final MapCodec<PropertyDamage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.fieldOf("normalize").forGetter(PropertyDamage::isNormalize)
    ).apply(i, PropertyDamage::of));

    static {
        Property.register(TYPE, CODEC);
    }
}
