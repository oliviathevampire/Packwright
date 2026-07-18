package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.List;

public final class BiomeSpawnCondition extends SpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "biome");

    public static final MapCodec<BiomeSpawnCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IdOrTag.LIST_CODEC.fieldOf("biomes").forGetter(x -> x.biomes)
    ).apply(i, (bio) -> {
        BiomeSpawnCondition out = new BiomeSpawnCondition();
        out.biomes = bio;
        return out;
    }));

    static {
        SpawnCondition.register(TYPE.toString(), CODEC);
    }

    private List<IdOrTag> biomes;

    private BiomeSpawnCondition() {
        super(TYPE.toString());
    }


    public static BiomeSpawnCondition biomeCondition() { return new BiomeSpawnCondition(); }

    public BiomeSpawnCondition biomes(List<IdOrTag> biomes) { this.biomes = biomes; return this; }
    public BiomeSpawnCondition biome(Identifier biome) { this.biomes = List.of(IdOrTag.id(biome)); return this; }
    public BiomeSpawnCondition biomes(Identifier... biomes) { this.biomes = Arrays.stream(biomes).map(IdOrTag::id).toList(); return this; }
    public BiomeSpawnCondition biomeTag(Identifier tag) { this.biomes = List.of(IdOrTag.tag(tag)); return this; }

    public List<IdOrTag> getBiomes() { return biomes; }
}
