package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** Uses dye color when present; otherwise `default` RGB. */
public final class JTintDye extends JTint {
    public static final String TYPE = "minecraft:dye";

    private final int defaultRgb; // required fallback RGB (0xRRGGBB)

    public JTintDye(int defaultRgb) {
        super(TYPE);
        this.defaultRgb = defaultRgb;
    }

    public int defaultRgb() { return defaultRgb; }

    public static JTintDye of(int value) {
        return new JTintDye(value);
    }

    public static final Codec<JTintDye> CODEC = RecordCodecBuilder.create(i -> i.group(
        ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(JTintDye::defaultRgb)
    ).apply(i, JTintDye::new));

    static { JTint.register(TYPE, CODEC); }

    @Override public JTintDye clone() { return new JTintDye(defaultRgb); }
}
