package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:trident" special model type.
 */
public class ModelTrident extends ModelSpecial {
    public static final String TYPE = "minecraft:trident";
    public static final MapCodec<ModelTrident> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelTrident::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelTrident::codecGetFallback),
            Codec.STRING.optionalFieldOf("texture").forGetter(m -> Optional.ofNullable(m.texture))
    ).apply(i, (tints, fallback, texture) -> {
        ModelTrident m = new ModelTrident();
        ItemModel.applyBase(m, tints, fallback);
        texture.ifPresent(t -> m.texture = t);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String texture;

    protected ModelTrident() {
        super("minecraft:trident");
    }

    public static ModelTrident trident() {
        return new ModelTrident();
    }

    public ModelTrident texture(String texture) {
        this.texture = texture;
        return this;
    }

    public String getTexture() {
        return texture;
    }
}
