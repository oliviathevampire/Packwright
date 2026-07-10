package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Shared base for simple mob variants that carry an asset_id and optional spawn_conditions.
 * Subclasses add their own mob-specific ModelType enum and model field.
 */
public abstract class ModelVariant {
    protected Identifier assetId;
    protected SpawnPrioritySelectors spawnConditions;

    protected static <T extends ModelVariant> RecordCodecBuilder<T, Identifier> assetIdCodec() {
        return Identifier.CODEC.fieldOf("asset_id").forGetter(v -> v.assetId);
    }

    protected static <T extends ModelVariant> RecordCodecBuilder<T, Optional<SpawnPrioritySelectors>> spawnCodec() {
        return SpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions")
                .forGetter(v -> Optional.ofNullable(v.spawnConditions));
    }

    public Identifier getAssetId() { return assetId; }
    public SpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
