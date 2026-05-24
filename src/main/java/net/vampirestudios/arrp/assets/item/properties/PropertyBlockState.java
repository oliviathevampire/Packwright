package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the "block_state" property.
 */
public class PropertyBlockState extends Property {
    public static final String TYPE = "minecraft:block_state";
    private final Map<String, String> properties = new HashMap<>();

    protected PropertyBlockState() {
        super(TYPE);
    }

    public PropertyBlockState(Map<String, String> properties) {
        super(TYPE);
    }

    // Static factory method
    public static PropertyBlockState blockState() {
        return new PropertyBlockState();
    }

    // Fluent method to add a block state property
    public PropertyBlockState property(String name, String value) {
        this.properties.put(name, value);
        return this;
    }

    // Getter
    public Map<String, String> getProperties() {
        return properties;
    }

    public static final MapCodec<PropertyBlockState> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("properties").forGetter(PropertyBlockState::getProperties)
    ).apply(i, PropertyBlockState::new));

    static {
        Property.register(TYPE, CODEC);
    }
}
