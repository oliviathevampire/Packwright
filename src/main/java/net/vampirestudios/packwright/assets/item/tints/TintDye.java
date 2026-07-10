package net.vampirestudios.packwright.assets.item.tints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** Uses dye color when present; otherwise `default` RGB. */
public final class TintDye extends Tint {
    public static final String TYPE = "minecraft:dye";

    private final int defaultRgb; // required fallback RGB (0xRRGGBB)

    public TintDye(int defaultRgb) {
        super(TYPE);
        this.defaultRgb = defaultRgb;
    }

    public int defaultRgb() { return defaultRgb; }

    public static TintDye of(int value) {
        return new TintDye(value);
    }

    public static final Codec<TintDye> CODEC = RecordCodecBuilder.create(i -> i.group(
        ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TintDye::defaultRgb)
    ).apply(i, TintDye::new));

    static { Tint.register(TYPE, CODEC); }


}
