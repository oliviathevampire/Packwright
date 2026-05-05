package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map of Environment Attributes for a holder (biome, dimension, etc.):
 *
 * "attributes": {
 *   "minecraft:visual/fog_color": "#ffaaff",
 *   "minecraft:visual/cloud_opacity": 0.5,
 *   "minecraft:gameplay/water_evaporates": true
 * }
 *
 * Independent from JBiome so it can be reused elsewhere.
 */
public class JEnvironmentAttributes implements Cloneable {

    private final Map<String, EnvironmentAttributeValue> values = new LinkedHashMap<>();

    public static final Codec<JEnvironmentAttributes> CODEC =
            Codec.unboundedMap(Codec.STRING, EnvironmentAttributeValue.CODEC)
                    .xmap(JEnvironmentAttributes::fromMap, JEnvironmentAttributes::toMap);

    private static JEnvironmentAttributes fromMap(Map<String, EnvironmentAttributeValue> map) {
        JEnvironmentAttributes attrs = new JEnvironmentAttributes();
        attrs.values.putAll(map);
        return attrs;
    }

    private Map<String, EnvironmentAttributeValue> toMap() {
        return new LinkedHashMap<>(this.values);
    }

    // ----------------- Builder-style setters -----------------

    public JEnvironmentAttributes putString(String key, String value) {
        if (key != null && value != null) {
            this.values.put(key, EnvironmentAttributeValue.ofString(value));
        }
        return this;
    }

    public JEnvironmentAttributes putNumber(String key, double value) {
        if (key != null) {
            this.values.put(key, EnvironmentAttributeValue.ofNumber(value));
        }
        return this;
    }

    public JEnvironmentAttributes putBoolean(String key, boolean value) {
        if (key != null) {
            this.values.put(key, EnvironmentAttributeValue.ofBoolean(value));
        }
        return this;
    }

    // ----------------- Accessors -----------------

    public Map<String, EnvironmentAttributeValue> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public EnvironmentAttributeValue get(String key) {
        return this.values.get(key);
    }

    @Override
    public JEnvironmentAttributes clone() {
        try {
            JEnvironmentAttributes clone = (JEnvironmentAttributes) super.clone();
            clone.values.clear();
            clone.values.putAll(this.values);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
