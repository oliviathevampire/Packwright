package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** minecraft:firework — Average firework colors or default. Fields: default (RGB) */
public final class TintFirework extends Tint {
    public static final String TYPE = "minecraft:firework";

    private final int def;

    public TintFirework(int def) {
        super(TYPE);
        this.def = def;
    }

    public int defaultRgb() { return def; }

    public static final Codec<TintFirework> CODEC = RecordCodecBuilder.create(i -> i.group(
        ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TintFirework::defaultRgb)
    ).apply(i, TintFirework::new));

    static { Tint.register(TYPE, CODEC); }

    @Override public TintFirework clone() { return new TintFirework(def); }
}
