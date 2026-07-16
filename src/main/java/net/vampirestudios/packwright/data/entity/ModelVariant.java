package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * Shared base for simple mob variants that carry an asset_id, a required baby_asset_id,
 * and required spawn_conditions. Subclasses add their own mob-specific ModelType enum
 * and model field.
 */
public abstract class ModelVariant {
    protected Identifier assetId;
    protected Identifier babyAssetId;
    protected SpawnPrioritySelectors spawnConditions;

    protected static <T extends ModelVariant> RecordCodecBuilder<T, Identifier> assetIdCodec() {
        return Identifier.CODEC.fieldOf("asset_id").forGetter(v -> v.assetId);
    }

    protected static <T extends ModelVariant> RecordCodecBuilder<T, Identifier> babyAssetIdCodec() {
        return Identifier.CODEC.fieldOf("baby_asset_id").forGetter(v -> v.babyAssetId);
    }

    protected static <T extends ModelVariant> RecordCodecBuilder<T, SpawnPrioritySelectors> spawnCodec() {
        return SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(v -> v.spawnConditions);
    }

    public Identifier getAssetId() { return assetId; }
    public Identifier getBabyAssetId() { return babyAssetId; }
    public SpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
