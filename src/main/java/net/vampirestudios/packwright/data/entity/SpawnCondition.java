package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for spawn conditions used inside mob variant "spawn_conditions".
 * Serialized as an object with a discriminator field "type": "<namespace:path>".
 */
public abstract class SpawnCondition {
    private final String type;

    protected SpawnCondition(String type) {
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    // ===== Registry + base codec =====

    private static final Map<String, Codec<? extends SpawnCondition>> REGISTRY = new ConcurrentHashMap<>();

    public static void register(String type, MapCodec<? extends SpawnCondition> codec) {
        REGISTRY.put(type, codec.codec());
    }

    /**
     * Dispatch codec. Reads "type" then uses the registered subtype codec.
     */
    public static final Codec<SpawnCondition> CODEC =
            Codecs.tagged("type", SpawnCondition::getType, REGISTRY::get);
}
