package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:conduit" special model type.
 */
public class ModelConduit extends ModelSpecial {
    public static final String TYPE = "minecraft:conduit";
    public static final MapCodec<ModelConduit> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelConduit::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelConduit::codecGetFallback),
            Codec.STRING.optionalFieldOf("texture").forGetter(m -> Optional.ofNullable(m.texture))
    ).apply(i, (tints, fallback, texture) -> {
        ModelConduit m = new ModelConduit();
        ItemModel.applyBase(m, tints, fallback);
        texture.ifPresent(t -> m.texture = t);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String texture;

    protected ModelConduit() {
        super("minecraft:conduit");
    }

    public static ModelConduit conduit() {
        return new ModelConduit();
    }

    public ModelConduit texture(String texture) {
        this.texture = texture;
        return this;
    }

    public String getTexture() {
        return texture;
    }
}
