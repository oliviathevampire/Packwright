package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.ItemModel;

/**
 * Abstract base class for special model types "minecraft:special".
 */
public class ModelSpecial extends ItemModel {
    public static final String TYPE = "minecraft:special";
    public static final MapCodec<ModelSpecial> CODEC = RecordCodecBuilder.mapCodec(function -> function.group(
            Codec.STRING.fieldOf("base").forGetter(ModelSpecial::getBase),
            ItemModel.CODEC.fieldOf("model").forGetter(ModelSpecial::getModel),
            Codec.STRING.fieldOf("special_type").forGetter(ModelSpecial::getSpecialType)
    ).apply(function, ModelSpecial::new));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> {
            m.type = TYPE;
            return m;
        }, m -> m));
    }

    private String base;  // Optional base model
    private ItemModel model; // Nested model
    private String specialType;

    public ModelSpecial() {
        super(TYPE);
    }

    protected ModelSpecial(String specialType) {
        super(TYPE);
        this.specialType = specialType;
    }

    protected ModelSpecial(String base, ItemModel model, String specialType) {
        super(TYPE);
        this.base = base;
        this.model = model;
        this.specialType = specialType;
    }

    public static ModelSpecial specialModel(String base, ItemModel special) {
        return new ModelSpecial().base(base).model(special);
    }

    public static ModelShield shield() {
        return new ModelShield();
    }

    public String getSpecialType() {
        return specialType;
    }

    public static ModelHead head() {
        return new ModelHead();
    }

    // Fluent methods
    public ModelSpecial base(String base) {
        this.base = base;
        return this;
    }

    public ModelSpecial model(ItemModel model) {
        this.model = model;
        return this;
    }

    // Getters
    public String getBase() {
        return base;
    }

    public ItemModel getModel() {
        return model;
    }
}
