package net.vampirestudios.arrp.json.iteminfo.tint;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for tint sources.
 */
public abstract class JTint implements Cloneable {
    private final String type;

    protected JTint(String type) {
        this.type = type;
    }

    // Static factory methods for different tint types
    public static JTintConstant constant(int value) {
        return JTintConstant.of(value);
    }

    public static JTintTeam team(int value) {
        return JTintTeam.of(value);
    }

    public static JTintDye dye(int value) {
        return JTintDye.of(value);
    }

    // Getter
    public String getType() {
        return type;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        return json;
    }

    // Clone method to be implemented by subclasses
    @Override
    public abstract JTint clone();

    // ---- Registry + base codec ----
    private static final Map<String, Codec<? extends JTint>> REGISTRY = new ConcurrentHashMap<>();
    public static void register(String type, Codec<? extends JTint> codec) { REGISTRY.put(type, codec); }

    public static final Codec<JTint> CODEC = Codecs.tagged("type", JTint::getType, REGISTRY::get);
}
