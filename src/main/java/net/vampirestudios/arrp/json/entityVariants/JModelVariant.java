package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Shared base for simple mob variants that carry an asset_id and optional spawn_conditions.
 * Subclasses add their own mob-specific ModelType enum and model field.
 */
public abstract class JModelVariant {
    protected Identifier assetId;
    protected JSpawnPrioritySelectors spawnConditions;

    protected static <T extends JModelVariant> RecordCodecBuilder<T, Identifier> assetIdCodec() {
        return Identifier.CODEC.fieldOf("asset_id").forGetter(v -> v.assetId);
    }

    protected static <T extends JModelVariant> RecordCodecBuilder<T, Optional<JSpawnPrioritySelectors>> spawnCodec() {
        return JSpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions")
                .forGetter(v -> Optional.ofNullable(v.spawnConditions));
    }

    public Identifier getAssetId() { return assetId; }
    public JSpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
