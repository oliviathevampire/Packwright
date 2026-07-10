package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class BiomeSpawnCondition extends SpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "biome");

    public static final MapCodec<BiomeSpawnCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IdOrTag.CODEC.fieldOf("biomes").forGetter(x -> x.biomes)
    ).apply(i, (bio) -> {
        BiomeSpawnCondition out = new BiomeSpawnCondition();
        out.biomes = bio;
        return out;
    }));

    static {
        SpawnCondition.register(TYPE.toString(), CODEC);
    }

    private IdOrTag biomes;

    private BiomeSpawnCondition() {
        super(TYPE.toString());
    }


    public static BiomeSpawnCondition biomeCondition() { return new BiomeSpawnCondition(); }

    public BiomeSpawnCondition biomes(IdOrTag biomes) { this.biomes = biomes; return this; }
    public BiomeSpawnCondition biome(Identifier biome) { this.biomes = IdOrTag.id(biome); return this; }
    public BiomeSpawnCondition biomeTag(Identifier tag) { this.biomes = IdOrTag.tag(tag); return this; }

    public IdOrTag getBiomes() { return biomes; }
}