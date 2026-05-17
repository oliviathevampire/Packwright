package net.vampirestudios.arrp.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Generic mob-variant entry:
 * - asset_id: Identifier
 * - model: optional string (some registries use it)
 * - spawn_conditions: optional
 *
 * Use folder-based add method to place under the right data/<ns>/<folder>/<id>.json
 */
public class SimpleMobVariant {
    public static final Codec<SimpleMobVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
            Codec.STRING.optionalFieldOf("model").forGetter(x -> Optional.ofNullable(x.model)),
            SpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions").forGetter(x -> Optional.ofNullable(x.spawnConditions))
    ).apply(i, (assetId, model, spawns) -> {
        SimpleMobVariant out = new SimpleMobVariant();
        out.assetId = assetId;
        model.ifPresent(out::model);
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private Identifier assetId;
    private String model; // optional (keep string; model enums differ per mob registry)
    private SpawnPrioritySelectors spawnConditions;

    public SimpleMobVariant() {}

    public static SimpleMobVariant mobVariant() { return new SimpleMobVariant(); }

    public SimpleMobVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public SimpleMobVariant model(String model) { this.model = model; return this; }
    public SimpleMobVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public Identifier getAssetId() { return assetId; }
    public String getModel() { return model; }
    public SpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
