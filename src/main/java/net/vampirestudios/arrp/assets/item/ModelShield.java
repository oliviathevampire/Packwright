package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.assets.item.ItemModel;

import java.util.Optional;

/**
 * Represents the "minecraft:shield" special model type.
 */
public class ModelShield extends ModelSpecial {
    public static final String TYPE = "minecraft:shield";

    // { "pattern": "<ns:path>", "base_texture": "<ns:path>" } — both optional
    public static final MapCodec<ModelShield> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.optionalFieldOf("pattern").forGetter(s -> Optional.ofNullable(s.pattern)),
            Codec.STRING.optionalFieldOf("base_texture").forGetter(s -> Optional.ofNullable(s.baseTexture))
    ).apply(i, (opPattern, opBase) -> {
        ModelShield s = new ModelShield();
        opPattern.ifPresent(s::pattern);
        opBase.ifPresent(s::baseTexture);
        return s;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String pattern;  // Pattern texture
    private String baseTexture; // Base texture

    protected ModelShield() {
        super("minecraft:shield");
    }

    // Fluent methods
    public ModelShield pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public ModelShield baseTexture(String baseTexture) {
        this.baseTexture = baseTexture;
        return this;
    }

    // Getters
    public String getPattern() {
        return pattern;
    }

    public String getBaseTexture() {
        return baseTexture;
    }
}
