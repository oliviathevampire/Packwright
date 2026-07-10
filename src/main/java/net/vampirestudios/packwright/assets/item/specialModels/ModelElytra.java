package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:elytra" special model type.
 */
public class ModelElytra extends ModelSpecial {
    public static final String TYPE = "minecraft:elytra";
    public static final MapCodec<ModelElytra> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelElytra::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelElytra::codecGetFallback),
            Codec.STRING.optionalFieldOf("texture").forGetter(m -> Optional.ofNullable(m.texture))
    ).apply(i, (tints, fallback, texture) -> {
        ModelElytra m = new ModelElytra();
        ItemModel.applyBase(m, tints, fallback);
        texture.ifPresent(t -> m.texture = t);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String texture;

    protected ModelElytra() {
        super("minecraft:elytra");
    }

    public static ModelElytra elytra() {
        return new ModelElytra();
    }

    public ModelElytra texture(String texture) {
        this.texture = texture;
        return this;
    }

    public String getTexture() {
        return texture;
    }
}
