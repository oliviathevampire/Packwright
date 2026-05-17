package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.impl.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for tint sources.
 */
public abstract class Tint implements Cloneable {
    private final String type;

    protected Tint(String type) {
        this.type = type;
    }

    // Static factory methods for different tint types
    public static TintConstant constant(int value) {
        return TintConstant.of(value);
    }

    public static TintTeam team(int value) {
        return TintTeam.of(value);
    }

    public static TintDye dye(int value) {
        return TintDye.of(value);
    }

    // Getter
    public String getType() {
        return type;
    }

    // Clone method to be implemented by subclasses
    @Override
    public abstract Tint clone();

    // ---- Registry + base codec ----
    private static final Map<String, Codec<? extends Tint>> REGISTRY = new ConcurrentHashMap<>();
    public static void register(String type, Codec<? extends Tint> codec) { REGISTRY.put(type, codec); }

    public static final Codec<Tint> CODEC = Codecs.tagged("type", Tint::getType, REGISTRY::get);
}
