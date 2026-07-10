package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Optional;

/**
 * Represents the "minecraft:shulker_box" special model type.
 */
public class ModelShulkerBox extends ModelSpecial {
    public static final String TYPE = "minecraft:shulker_box";
    public static final MapCodec<ModelShulkerBox> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelShulkerBox::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelShulkerBox::codecGetFallback),
            Codec.STRING.optionalFieldOf("color").forGetter(m -> Optional.ofNullable(m.color)),
            Codec.BOOL.optionalFieldOf("open").forGetter(m -> m.open ? Optional.of(true) : Optional.empty())
    ).apply(i, (tints, fallback, color, open) -> {
        ModelShulkerBox m = new ModelShulkerBox();
        ItemModel.applyBase(m, tints, fallback);
        color.ifPresent(c -> m.color = c);
        open.ifPresent(o -> m.open = o);
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String color;
    private boolean open;

    protected ModelShulkerBox() {
        super("minecraft:shulker_box");
    }

    public static ModelShulkerBox shulkerBox() {
        return new ModelShulkerBox();
    }

    public ModelShulkerBox color(String color) {
        this.color = color;
        return this;
    }

    public ModelShulkerBox open(boolean open) {
        this.open = open;
        return this;
    }

    public String getColor() { return color; }
    public boolean isOpen()  { return open; }
}
