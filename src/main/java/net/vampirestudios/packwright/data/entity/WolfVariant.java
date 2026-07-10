package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public final class WolfVariant {
    public static final Codec<WolfVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Assets.CODEC.fieldOf("assets").forGetter(x -> x.assets),
            SpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions").forGetter(x -> Optional.ofNullable(x.spawnConditions))
    ).apply(i, (assets, spawns) -> {
        WolfVariant out = new WolfVariant();
        out.assets = assets;
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private Assets assets;
    private SpawnPrioritySelectors spawnConditions; // optional

    public WolfVariant() {}

    public static WolfVariant wolfVariant() { return new WolfVariant(); }

    public WolfVariant assets(Identifier wild, Identifier tame, Identifier angry) {
        this.assets = new Assets().wild(wild).tame(tame).angry(angry);
        return this;
    }

    public WolfVariant assets(Assets assets) { this.assets = assets; return this; }

    public WolfVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public Assets getAssets() { return assets; }
    public SpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }

    public static class Assets {
        public static final Codec<Assets> CODEC = RecordCodecBuilder.create(i -> i.group(
                Identifier.CODEC.fieldOf("wild").forGetter(x -> x.wild),
                Identifier.CODEC.fieldOf("tame").forGetter(x -> x.tame),
                Identifier.CODEC.fieldOf("angry").forGetter(x -> x.angry)
        ).apply(i, (w, t, a) -> {
            Assets out = new Assets();
            out.wild = w; out.tame = t; out.angry = a;
            return out;
        }));

        private Identifier wild, tame, angry;

        public Assets wild(Identifier id) { this.wild = id; return this; }
        public Assets tame(Identifier id) { this.tame = id; return this; }
        public Assets angry(Identifier id){ this.angry = id; return this; }

        public Identifier getWild() { return wild; }
        public Identifier getTame() { return tame; }
        public Identifier getAngry(){ return angry; }
    }
}
