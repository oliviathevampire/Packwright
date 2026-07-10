package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:chest" special model type.
 */
public class ModelChest extends ModelSpecial {
    public static final String TYPE = "minecraft:chest";
    public static final MapCodec<ModelChest> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelChest::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelChest::codecGetFallback),
            Codec.STRING.optionalFieldOf("texture").forGetter(m -> Optional.ofNullable(m.texture)),
            Codec.FLOAT.optionalFieldOf("openness").forGetter(m -> m.openness != 0f ? Optional.of(m.openness) : Optional.empty())
    ).apply(i, (tints, fallback, texture, openness) -> {
        ModelChest m = new ModelChest();
        ItemModel.applyBase(m, tints, fallback);
        texture.ifPresent(t -> m.texture = t);
        openness.ifPresent(o -> m.openness = o);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String texture;
    private float openness;

    public ModelChest() {
        super("minecraft:chest");
    }

    public String getTexture() { return texture; }
    public float getOpenness() { return openness; }

    public ModelChest texture(String texture) {
        this.texture = texture;
        return this;
    }

    public ModelChest openness(float openness) {
        this.openness = openness;
        return this;
    }
}
