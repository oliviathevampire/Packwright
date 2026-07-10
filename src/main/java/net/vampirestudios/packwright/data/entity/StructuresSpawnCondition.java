package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class StructuresSpawnCondition extends SpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "structures");

    public static final MapCodec<StructuresSpawnCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IdOrTag.CODEC.fieldOf("structures").forGetter(x -> x.structures)
    ).apply(i, (s) -> {
        StructuresSpawnCondition out = new StructuresSpawnCondition();
        out.structures = s;
        return out;
    }));

    static {
        SpawnCondition.register(TYPE.toString(), MAP_CODEC);
    }

    private IdOrTag structures;

    public StructuresSpawnCondition() {
        super(TYPE.toString());
    }

    public static StructuresSpawnCondition structuresCondition() { return new StructuresSpawnCondition(); }

    public StructuresSpawnCondition structures(IdOrTag structures) { this.structures = structures; return this; }
    public StructuresSpawnCondition structure(Identifier id) { this.structures = IdOrTag.id(id); return this; }
    public StructuresSpawnCondition structureTag(Identifier tag) { this.structures = IdOrTag.tag(tag); return this; }

    public IdOrTag getStructures() { return structures; }
}
