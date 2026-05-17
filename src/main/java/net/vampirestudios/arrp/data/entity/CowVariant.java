package net.vampirestudios.arrp.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public class CowVariant extends ModelVariant {
    public static final Codec<CowVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            ModelType.CODEC.optionalFieldOf("model", ModelType.NORMAL).forGetter(x -> x.model),
            assetIdCodec(),
            spawnCodec()
    ).apply(i, (model, assetId, spawns) -> {
        CowVariant out = new CowVariant();
        out.model = model;
        out.assetId = assetId;
        spawns.ifPresent(out::spawnConditions);
        return out;
    }));

    private ModelType model = ModelType.NORMAL;

    public CowVariant() {}

    public static CowVariant cowVariant() { return new CowVariant(); }

    public CowVariant model(ModelType model) { this.model = model; return this; }
    public CowVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public CowVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public ModelType getModel() { return model; }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold"),
        WARM("warm");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;
        ModelType(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
    }
}
