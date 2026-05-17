package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/** minecraft:firework — Average firework colors or default. Fields: default (RGB) */
public final class JTintFirework extends JTint {
    public static final String TYPE = "minecraft:firework";

    private final int def;

    public JTintFirework(int def) {
        super(TYPE);
        this.def = def;
    }

    public int defaultRgb() { return def; }

    public static final Codec<JTintFirework> CODEC = RecordCodecBuilder.create(i -> i.group(
        ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(JTintFirework::defaultRgb)
    ).apply(i, JTintFirework::new));

    static { JTint.register(TYPE, CODEC); }

    @Override public JTintFirework clone() { return new JTintFirework(def); }
}
