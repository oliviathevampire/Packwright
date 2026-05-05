package net.vampirestudios.arrp.json.iteminfo.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.codec.Codecs;
import net.vampirestudios.arrp.json.iteminfo.tint.JTint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for different model types.
 */
public abstract class JItemModel {
    protected String type;
    protected List<JTint> tints;
    protected JItemModel fallback;

    protected JItemModel() {}

    protected JItemModel(String type) {
        this.type = type;
    }

    public String getType() { return type; }

    public List<JTint> getTints() { return tints; }

    public JItemModel getFallback() { return fallback; }

    public JItemModel tints(JTint... tints) {
        this.tints = Arrays.asList(tints);
        return this;
    }

    public JItemModel tints(List<JTint> tints) {
        this.tints = tints;
        return this;
    }

    public JItemModel tint(JTint tint) {
        if (tints == null) tints = new ArrayList<>();
        tints.add(tint);
        return this;
    }

    public JItemModel fallback(JItemModel fb) {
        this.fallback = fb;
        return this;
    }


    // Static factory methods for different model types
    public static JModelBasic model(String model) {
        return new JModelBasic(model);
    }

    public static JModelRangeDispatch rangeDispatch() {
        return new JModelRangeDispatch();
    }

    public static JModelComposite composite() {
        return new JModelComposite();
    }

    public static JModelCondition condition() {
        return new JModelCondition();
    }

    public static JModelSelect select() {
        return new JModelSelect();
    }

    public static JModelEmpty empty() {
        return new JModelEmpty();
    }

    // registry of subtype codecs
    private static final Map<String, Codec<? extends JItemModel>> REGISTRY = new ConcurrentHashMap<>();

    public static void register(String type, MapCodec<? extends JItemModel> codec) {
        REGISTRY.put(type, codec.codec());
    }

    /**
     * Tagged codec: picks subtype codec by "type" and also handles base fields.
     * Each subtype should call JItemModel.register("minecraft:xyz", CODEC).
     */
    public static final Codec<JItemModel> CODEC = Codecs.tagged("type", JItemModel::getType, REGISTRY::get);

    // self-reference for fallback recursion
    static final Codec<JItemModel> LAZY_SELF = Codec.lazyInitialized(() -> CODEC);

    /** null-safe Optional getter for tints */
    protected Optional<List<JTint>> codecGetTints() {
        return Optional.ofNullable(tints).filter(l -> !l.isEmpty());
    }

    /** null-safe Optional getter for fallback */
    protected Optional<JItemModel> codecGetFallback() {
        return Optional.ofNullable(fallback);
    }

    /** apply decoded tints+fallback to a subtype instance */
    protected static <M extends JItemModel> M applyBase(M m,
                                                        Optional<List<JTint>> tintsOpt,
                                                        Optional<JItemModel> fbOpt) {
        tintsOpt.ifPresent(m::tints);
        fbOpt.ifPresent(m::fallback);
        return m;
    }

    // ----- helper for subtypes -----

    /**
     * Convenience group for including base fields in subtype RecordCodecBuilder.
     */
    protected static <M extends JItemModel> List<RecordCodecBuilder<M, ?>> baseFields() {
        return List.of(
            JTint.CODEC.listOf().optionalFieldOf("tints").forGetter(M::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(M::codecGetFallback)
        );
    }
}
