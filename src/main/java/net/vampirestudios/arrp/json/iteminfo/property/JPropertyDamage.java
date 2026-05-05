package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:damage" numeric property.
 */
public class JPropertyDamage extends JProperty {
    public static final String TYPE = "minecraft:damage";
    private boolean normalize = true; // Default: true

    public JPropertyDamage() {
        super(TYPE);
    }

    public static JPropertyDamage of(boolean normalize) {
        JPropertyDamage count = new JPropertyDamage();
        count.normalize = normalize;
        return count;
    }

    // Getter and Setter
    public boolean isNormalize() {
        return normalize;
    }

    public static final MapCodec<JPropertyDamage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.fieldOf("normalize").forGetter(JPropertyDamage::isNormalize)
    ).apply(i, JPropertyDamage::of));

    static {
        JProperty.register(TYPE, CODEC);
    }
}
