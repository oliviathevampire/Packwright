package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public final class JWolfVariant {
    public static final Codec<JWolfVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Assets.CODEC.fieldOf("assets").forGetter(x -> x.assets),
            JSpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions").forGetter(x -> Optional.ofNullable(x.spawnConditions))
    ).apply(i, (assets, spawns) -> {
        JWolfVariant out = new JWolfVariant();
        out.assets = assets;
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private Assets assets;
    private JSpawnPrioritySelectors spawnConditions; // optional

    public JWolfVariant() {}

    public static JWolfVariant wolfVariant() { return new JWolfVariant(); }

    public JWolfVariant assets(Identifier wild, Identifier tame, Identifier angry) {
        this.assets = new Assets().wild(wild).tame(tame).angry(angry);
        return this;
    }

    public JWolfVariant assets(Assets assets) { this.assets = assets; return this; }

    public JWolfVariant spawnConditions(JSpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public Assets getAssets() { return assets; }
    public JSpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }

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
