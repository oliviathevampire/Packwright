package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for properties.
 */
public abstract class JProperty {
    private final String type;

    protected JProperty(String type) {
        this.type = type;
    }

    public String getPropertyType() {
        return type;
    }

    // ===== Registry + base codec =====

    private static final Map<String, Codec<? extends JProperty>> REGISTRY = new ConcurrentHashMap<>();
    public static void register(String property, MapCodec<? extends JProperty> codec) {
        REGISTRY.put(property, codec.codec());
    }

    public static final Codec<JProperty> CODEC = Codecs.tagged("property", JProperty::getPropertyType, REGISTRY::get);
}
