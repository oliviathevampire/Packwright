package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:banner" special model type.
 */
public class ModelBanner extends ModelSpecial {
    public static final String TYPE = "minecraft:banner";
    public static final MapCodec<ModelBanner> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelBanner::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelBanner::codecGetFallback),
            Codec.STRING.optionalFieldOf("color").forGetter(m -> Optional.ofNullable(m.color))
    ).apply(i, (tints, fallback, color) -> {
        ModelBanner m = new ModelBanner();
        ItemModel.applyBase(m, tints, fallback);
        color.ifPresent(c -> m.color = c);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> {
            m.type = TYPE;
            return m;
        }, m -> m));
    }

    private String color;

    public ModelBanner() {
        super("minecraft:banner");
    }

    public String getColor() {
        return color;
    }

    public ModelBanner color(String color) {
        this.color = color;
        return this;
    }
}
