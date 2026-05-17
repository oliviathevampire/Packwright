package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public final class JZombieNautilusVariant extends JModelVariant {
    public static final Codec<JZombieNautilusVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            ModelType.CODEC.optionalFieldOf("model", ModelType.NORMAL).forGetter(x -> x.model),
            assetIdCodec(),
            spawnCodec()
    ).apply(i, (model, assetId, spawns) -> {
        JZombieNautilusVariant out = new JZombieNautilusVariant();
        out.model = model;
        out.assetId = assetId;
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private ModelType model = ModelType.NORMAL;

    public JZombieNautilusVariant() {}

    public static JZombieNautilusVariant zombieNautilusVariant() { return new JZombieNautilusVariant(); }

    public JZombieNautilusVariant model(ModelType model) { this.model = model; return this; }
    public JZombieNautilusVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public JZombieNautilusVariant spawnConditions(JSpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public ModelType getModel() { return model; }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        WARM("warm");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;
        ModelType(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
    }
}
