package net.vampirestudios.arrp.json.entityVariants;

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
public class JSimpleMobVariant {
    public static final Codec<JSimpleMobVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
            Codec.STRING.optionalFieldOf("model").forGetter(x -> Optional.ofNullable(x.model)),
            JSpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions").forGetter(x -> Optional.ofNullable(x.spawnConditions))
    ).apply(i, (assetId, model, spawns) -> {
        JSimpleMobVariant out = new JSimpleMobVariant();
        out.assetId = assetId;
        model.ifPresent(out::model);
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private Identifier assetId;
    private String model; // optional (keep string; model enums differ per mob registry)
    private JSpawnPrioritySelectors spawnConditions;

    public JSimpleMobVariant() {}

    public static JSimpleMobVariant mobVariant() { return new JSimpleMobVariant(); }

    public JSimpleMobVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public JSimpleMobVariant model(String model) { this.model = model; return this; }
    public JSimpleMobVariant spawnConditions(JSpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public Identifier getAssetId() { return assetId; }
    public String getModel() { return model; }
    public JSpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }
}
