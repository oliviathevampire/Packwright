package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.impl.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for properties.
 */
public abstract class Property {
    private final String type;

    protected Property(String type) {
        this.type = type;
    }

    public String getPropertyType() {
        return type;
    }

    // ===== Registry + base codec =====

    private static final Map<String, Codec<? extends Property>> REGISTRY = new ConcurrentHashMap<>();
    public static void register(String property, MapCodec<? extends Property> codec) {
        REGISTRY.put(property, codec.codec());
    }

    public static final Codec<Property> CODEC = Codecs.tagged("property", Property::getPropertyType, REGISTRY::get);
}
