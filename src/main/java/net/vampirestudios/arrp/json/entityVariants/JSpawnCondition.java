package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for spawn conditions used inside mob variant "spawn_conditions".
 * Serialized as an object with a discriminator field "type": "<namespace:path>".
 */
public abstract class JSpawnCondition {
    private final String type;

    protected JSpawnCondition(String type) {
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    // ===== Registry + base codec =====

    private static final Map<String, Codec<? extends JSpawnCondition>> REGISTRY = new ConcurrentHashMap<>();

    public static void register(String type, MapCodec<? extends JSpawnCondition> codec) {
        REGISTRY.put(type, codec.codec());
    }

    /**
     * Dispatch codec. Reads "type" then uses the registered subtype codec.
     */
    public static final Codec<JSpawnCondition> CODEC =
            Codecs.tagged("type", JSpawnCondition::getType, REGISTRY::get);
}
