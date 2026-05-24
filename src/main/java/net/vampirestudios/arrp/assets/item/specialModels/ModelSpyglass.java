package net.vampirestudios.arrp.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.ItemModel;
import net.vampirestudios.arrp.assets.item.models.ModelSpecial;
import net.vampirestudios.arrp.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:spyglass" special model type.
 */
public class ModelSpyglass extends ModelSpecial {
    public static final String TYPE = "minecraft:spyglass";
    public static final MapCodec<ModelSpyglass> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelSpyglass::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelSpyglass::codecGetFallback),
            Codec.STRING.optionalFieldOf("scope_texture").forGetter(m -> Optional.ofNullable(m.scopeTexture))
    ).apply(i, (tints, fallback, scopeTexture) -> {
        ModelSpyglass m = new ModelSpyglass();
        ItemModel.applyBase(m, tints, fallback);
        scopeTexture.ifPresent(t -> m.scopeTexture = t);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String scopeTexture;

    protected ModelSpyglass() {
        super("minecraft:spyglass");
    }

    public static ModelSpyglass spyglass() {
        return new ModelSpyglass();
    }

    public ModelSpyglass scopeTexture(String scopeTexture) {
        this.scopeTexture = scopeTexture;
        return this;
    }

    public String getScopeTexture() {
        return scopeTexture;
    }
}
