package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

public class JChickenVariant {
    public static final Codec<JChickenVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            ModelType.CODEC.optionalFieldOf("model", ModelType.NORMAL).forGetter(x -> x.model),
            Identifier.CODEC.fieldOf("asset_id").forGetter(x -> x.assetId),
            JSpawnPrioritySelectors.CODEC.optionalFieldOf("spawn_conditions").forGetter(x -> Optional.ofNullable(x.spawnConditions))
    ).apply(i, (model, assetId, spawns) -> {
        JChickenVariant out = new JChickenVariant();
        out.model = model;
        out.assetId = assetId;
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private ModelType model = ModelType.NORMAL;
    private Identifier assetId;
    private JSpawnPrioritySelectors spawnConditions;

    public JChickenVariant() {}

    public static JChickenVariant chickenVariant() { return new JChickenVariant(); }

    public JChickenVariant model(ModelType model) { this.model = model; return this; }
    public JChickenVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public JChickenVariant spawnConditions(JSpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public ModelType getModel() { return model; }
    public Identifier getAssetId() { return assetId; }
    public JSpawnPrioritySelectors getSpawnConditions() { return spawnConditions; }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;
        ModelType(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
    }
}
