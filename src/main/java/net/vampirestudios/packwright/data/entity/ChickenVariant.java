package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public class ChickenVariant extends ModelVariant {
    public static final Codec<ChickenVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
            ModelType.CODEC.optionalFieldOf("model", ModelType.NORMAL).forGetter(x -> x.model),
            assetIdCodec(),
            babyAssetIdCodec(),
            spawnCodec()
    ).apply(i, (model, assetId, babyAssetId, spawns) -> {
        ChickenVariant out = new ChickenVariant();
        out.model = model;
        out.assetId = assetId;
        out.babyAssetId = babyAssetId;
        out.spawnConditions = spawns;
        return out;
    }));

    private ModelType model = ModelType.NORMAL;

    public ChickenVariant() {}

    public static ChickenVariant chickenVariant() { return new ChickenVariant(); }

    public ChickenVariant model(ModelType model) { this.model = model; return this; }
    public ChickenVariant assetId(Identifier assetId) { this.assetId = assetId; return this; }
    public ChickenVariant babyAssetId(Identifier babyAssetId) { this.babyAssetId = babyAssetId; return this; }
    public ChickenVariant spawnConditions(SpawnPrioritySelectors spawns) { this.spawnConditions = spawns; return this; }

    public ModelType getModel() { return model; }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;
        ModelType(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
    }
}
