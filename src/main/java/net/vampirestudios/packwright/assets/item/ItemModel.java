package net.vampirestudios.packwright.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.assets.item.models.*;
import net.vampirestudios.packwright.assets.item.tints.Tint;
import net.vampirestudios.packwright.assets.models.Transformation;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for different model types.
 */
public abstract class ItemModel {
    protected String type;
    protected List<Tint> tints;
    protected ItemModel fallback;
    protected Transformation transformation;

    protected ItemModel() {}

    protected ItemModel(String type) {
        this.type = type;
    }

    public String getType() { return type; }

    public List<Tint> getTints() { return tints; }

    public ItemModel getFallback() { return fallback; }

    public ItemModel transformation(Transformation transformation) {
        this.transformation = transformation;
        return this;
    }

    public ItemModel tints(Tint... tints) {
        this.tints = Arrays.asList(tints);
        return this;
    }

    public ItemModel tints(List<Tint> tints) {
        this.tints = tints;
        return this;
    }

    public ItemModel tint(Tint tint) {
        if (tints == null) tints = new ArrayList<>();
        tints.add(tint);
        return this;
    }

    public ItemModel fallback(ItemModel fb) {
        this.fallback = fb;
        return this;
    }


    // Static factory methods for different model types
    public static ModelBasic model(Identifier model) {
        return new ModelBasic(model);
    }

    public static ModelRangeDispatch rangeDispatch() {
        return new ModelRangeDispatch();
    }

    public static ModelComposite composite() {
        return new ModelComposite();
    }

    public static ModelCondition condition() {
        return new ModelCondition();
    }

    public static ModelSelect select() {
        return new ModelSelect();
    }

    public static ModelEmpty empty() {
        return new ModelEmpty();
    }

    // registry of subtype codecs
    private static final Map<String, Codec<? extends ItemModel>> REGISTRY = new ConcurrentHashMap<>();

    public static void register(String type, MapCodec<? extends ItemModel> codec) {
        REGISTRY.put(type, codec.codec());
    }

    /**
     * Tagged codec: picks subtype codec by "type" and also handles base fields.
     * Each subtype should call ItemModel.register("minecraft:xyz", CODEC).
     */
    public static final Codec<ItemModel> CODEC = Codecs.tagged("type", ItemModel::getType, REGISTRY::get);

    // self-reference for fallback recursion
    protected static final Codec<ItemModel> LAZY_SELF = Codec.lazyInitialized(() -> CODEC);

    /** null-safe Optional getter for tints */
    protected Optional<List<Tint>> codecGetTints() {
        return Optional.ofNullable(tints).filter(l -> !l.isEmpty());
    }

    /** null-safe Optional getter for fallback */
    protected Optional<ItemModel> codecGetFallback() {
        return Optional.ofNullable(fallback);
    }

    /** null-safe Optional getter for transformation */
    protected Optional<Transformation> codecGetTransformation() {
        return Optional.ofNullable(transformation);
    }

    /** apply decoded tints+fallback to a subtype instance */
    protected static <M extends ItemModel> M applyBase(M m,
                                                        Optional<List<Tint>> tintsOpt,
                                                        Optional<ItemModel> fbOpt) {
        tintsOpt.ifPresent(m::tints);
        fbOpt.ifPresent(m::fallback);
        return m;
    }

    /** apply decoded tints, fallback, and transformation to a subtype instance */
    protected static <M extends ItemModel> M applyBase(M m,
                                                       Optional<List<Tint>> tintsOpt,
                                                       Optional<ItemModel> fbOpt,
                                                       Optional<Transformation> transformationOpt) {
        tintsOpt.ifPresent(m::tints);
        fbOpt.ifPresent(m::fallback);
        transformationOpt.ifPresent(m::transformation);
        return m;
    }
}
