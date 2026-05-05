package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JStructuresSpawnCondition extends JSpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "structures");

    public static final MapCodec<JStructuresSpawnCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            JIdOrTag.CODEC.fieldOf("structures").forGetter(x -> x.structures)
    ).apply(i, (s) -> {
        JStructuresSpawnCondition out = new JStructuresSpawnCondition();
        out.structures = s;
        return out;
    }));

    static {
        JSpawnCondition.register(TYPE.toString(), MAP_CODEC);
    }

    private JIdOrTag structures;

    public JStructuresSpawnCondition() {
        super(TYPE.toString());
    }

    public static JStructuresSpawnCondition structuresCondition() { return new JStructuresSpawnCondition(); }

    public JStructuresSpawnCondition structures(JIdOrTag structures) { this.structures = structures; return this; }
    public JStructuresSpawnCondition structure(Identifier id) { this.structures = JIdOrTag.id(id); return this; }
    public JStructuresSpawnCondition structureTag(Identifier tag) { this.structures = JIdOrTag.tag(tag); return this; }

    public JIdOrTag getStructures() { return structures; }
}
