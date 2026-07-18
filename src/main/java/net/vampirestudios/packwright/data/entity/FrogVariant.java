package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * data/<ns>/frog_variant/<id>.json
 *
 * {
 *   "asset_id": "minecraft:...",
 *   "spawn_conditions": [ ... ]
 * }
 *
 * Note: unlike most other animal variants, vanilla's FrogVariant has no baby_asset_id.
 */
public class FrogVariant {
    public static final Codec<FrogVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(x -> x.spawnConditions)
    ).apply(i, (assetId, spawns) -> {
        FrogVariant out = new FrogVariant();
        out.assetId = assetId;
        out.spawnConditions = spawns;
        return out;
    }));

    private Identifier assetId;
    private SpawnPrioritySelectors spawnConditions;

    public FrogVariant() {}

    public static FrogVariant frogVariant() { return new FrogVariant(); }

    public FrogVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public FrogVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public Identifier getAssetId() { return assetId; }
    public SpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
