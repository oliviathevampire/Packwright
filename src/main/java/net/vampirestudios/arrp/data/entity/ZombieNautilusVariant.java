package net.vampirestudios.arrp.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public final class ZombieNautilusVariant extends ModelVariant {
    public static final Codec<ZombieNautilusVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            ModelType.CODEC.optionalFieldOf("model", ModelType.NORMAL).forGetter(x -> x.model),
            assetIdCodec(),
            spawnCodec()
    ).apply(i, (model, assetId, spawns) -> {
        ZombieNautilusVariant out = new ZombieNautilusVariant();
        out.model = model;
        out.assetId = assetId;
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private ModelType model = ModelType.NORMAL;

    public ZombieNautilusVariant() {}

    public static ZombieNautilusVariant zombieNautilusVariant() { return new ZombieNautilusVariant(); }

    public ZombieNautilusVariant model(ModelType model) { this.model = model; return this; }
    public ZombieNautilusVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public ZombieNautilusVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

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
