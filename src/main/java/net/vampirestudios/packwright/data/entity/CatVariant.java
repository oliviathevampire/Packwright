package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * data/<ns>/cat_variant/<id>.json
 *
 * {
 *   "asset_id": "minecraft:...",
 *   "baby_asset_id": "minecraft:...",
 *   "spawn_conditions": [ ... ]
 * }
 */
public class CatVariant {
    public static final Codec<CatVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
            Identifier.CODEC.fieldOf("baby_asset_id").forGetter(x -> x.babyAssetId),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(x -> x.spawnConditions)
    ).apply(i, (assetId, babyAssetId, spawns) -> {
        CatVariant out = new CatVariant();
        out.assetId = assetId;
        out.babyAssetId = babyAssetId;
        out.spawnConditions = spawns;
        return out;
    }));

    private Identifier assetId;
    private Identifier babyAssetId;
    private SpawnPrioritySelectors spawnConditions;

    public CatVariant() {}

    public static CatVariant catVariant() { return new CatVariant(); }

    public CatVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public CatVariant babyAssetId(Identifier babyAssetId) { this.babyAssetId = babyAssetId; return this; }
    public CatVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public Identifier getAssetId() { return assetId; }
    public Identifier getBabyAssetId() { return babyAssetId; }
    public SpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
