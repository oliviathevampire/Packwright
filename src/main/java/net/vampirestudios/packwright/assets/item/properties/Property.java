package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.impl.Codecs;

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

    // ===== Registry + codecs =====

    private static final Map<String, MapCodec<? extends Property>> REGISTRY = new ConcurrentHashMap<>();
    public static void register(String property, MapCodec<? extends Property> codec) {
        REGISTRY.put(property, codec);
    }

    public static final MapCodec<Property> MAP_CODEC = Codecs.taggedMap("property", Property::getPropertyType, REGISTRY::get);
    public static final Codec<Property> CODEC = MAP_CODEC.codec();
}
