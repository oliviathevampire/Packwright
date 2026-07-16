package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.List;

public class StructuresSpawnCondition extends SpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "structure");

    public static final MapCodec<StructuresSpawnCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IdOrTag.LIST_CODEC.fieldOf("structures").forGetter(x -> x.structures)
    ).apply(i, (s) -> {
        StructuresSpawnCondition out = new StructuresSpawnCondition();
        out.structures = s;
        return out;
    }));

    static {
        SpawnCondition.register(TYPE.toString(), MAP_CODEC);
    }

    private List<IdOrTag> structures;

    public StructuresSpawnCondition() {
        super(TYPE.toString());
    }

    public static StructuresSpawnCondition structuresCondition() { return new StructuresSpawnCondition(); }

    public StructuresSpawnCondition structures(List<IdOrTag> structures) { this.structures = structures; return this; }
    public StructuresSpawnCondition structure(Identifier id) { this.structures = List.of(IdOrTag.id(id)); return this; }
    public StructuresSpawnCondition structures(Identifier... ids) { this.structures = Arrays.stream(ids).map(IdOrTag::id).toList(); return this; }
    public StructuresSpawnCondition structureTag(Identifier tag) { this.structures = List.of(IdOrTag.tag(tag)); return this; }

    public List<IdOrTag> getStructures() { return structures; }
}
