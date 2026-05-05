package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:count" numeric property.
 */
public class JPropertyCount extends JProperty {
    public static final String TYPE = "minecraft:count";
    private boolean normalize = true; // Default is true

    protected JPropertyCount() {
        super(TYPE);
    }

    public JPropertyCount(boolean normalize) {
        super(TYPE);
        this.normalize = normalize;
    }

    // Static factory method
    public static JPropertyCount count() {
        return new JPropertyCount().normalize(false);
    }

    public static JPropertyCount count(boolean normalize) {
        return new JPropertyCount().normalize(normalize);
    }

    // Fluent method
    public JPropertyCount normalize(boolean normalize) {
        this.normalize = normalize;
        return this;
    }

    // Getter
    public boolean isNormalize() {
        return normalize;
    }

    public static final MapCodec<JPropertyCount> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.BOOL.fieldOf("normalize").forGetter(JPropertyCount::isNormalize)
    ).apply(i, JPropertyCount::new));

    static {
        JProperty.register(TYPE, CODEC.xmap(x -> x, x -> x));
    }
}
