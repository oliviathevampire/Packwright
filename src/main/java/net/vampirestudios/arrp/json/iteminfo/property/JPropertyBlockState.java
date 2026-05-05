package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the "block_state" property.
 */
public class JPropertyBlockState extends JProperty {
    public static final String TYPE = "minecraft:block_state";
    private final Map<String, String> properties = new HashMap<>();

    protected JPropertyBlockState() {
        super(TYPE);
    }

    public JPropertyBlockState(Map<String, String> properties) {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyBlockState blockState() {
        return new JPropertyBlockState();
    }

    // Fluent method to add a block state property
    public JPropertyBlockState property(String name, String value) {
        this.properties.put(name, value);
        return this;
    }

    // Getter
    public Map<String, String> getProperties() {
        return properties;
    }

    public static final MapCodec<JPropertyBlockState> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("properties").forGetter(JPropertyBlockState::getProperties)
    ).apply(i, JPropertyBlockState::new));

    static {
        JProperty.register(TYPE, CODEC);
    }
}
