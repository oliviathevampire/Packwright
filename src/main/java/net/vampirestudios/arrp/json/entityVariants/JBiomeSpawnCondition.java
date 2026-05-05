package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class JBiomeSpawnCondition extends JSpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "biome");

    public static final MapCodec<JBiomeSpawnCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            JIdOrTag.CODEC.fieldOf("biomes").forGetter(x -> x.biomes)
    ).apply(i, (bio) -> {
        JBiomeSpawnCondition out = new JBiomeSpawnCondition();
        out.biomes = bio;
        return out;
    }));

    static {
        JSpawnCondition.register(TYPE.toString(), CODEC);
    }

    private JIdOrTag biomes;

    private JBiomeSpawnCondition() {
        super(TYPE.toString());
    }


    public static JBiomeSpawnCondition biomeCondition() { return new JBiomeSpawnCondition(); }

    public JBiomeSpawnCondition biomes(JIdOrTag biomes) { this.biomes = biomes; return this; }
    public JBiomeSpawnCondition biome(Identifier biome) { this.biomes = JIdOrTag.id(biome); return this; }
    public JBiomeSpawnCondition biomeTag(Identifier tag) { this.biomes = JIdOrTag.tag(tag); return this; }

    public JIdOrTag getBiomes() { return biomes; }
}