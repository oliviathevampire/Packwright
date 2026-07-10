package net.vampirestudios.packwright.assets.item.specialModels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.item.ItemModel;
import net.vampirestudios.packwright.assets.item.models.ModelSpecial;
import net.vampirestudios.packwright.assets.item.tints.Tint;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents the "minecraft:decorated_pot" special model type.
 */
public class ModelDecoratedPot extends ModelSpecial {
    public static final String TYPE = "minecraft:decorated_pot";
    public static final MapCodec<ModelDecoratedPot> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Tint.CODEC.listOf().optionalFieldOf("tints").forGetter(ModelDecoratedPot::codecGetTints),
            LAZY_SELF.optionalFieldOf("fallback").forGetter(ModelDecoratedPot::codecGetFallback),
            Codec.STRING.listOf().optionalFieldOf("shards").forGetter(m ->
                    m.shards == null ? Optional.empty() : Optional.of(Arrays.asList(m.shards)))
    ).apply(i, (tints, fallback, shards) -> {
        ModelDecoratedPot m = new ModelDecoratedPot();
        ItemModel.applyBase(m, tints, fallback);
        shards.ifPresent(s -> m.shards = s.toArray(String[]::new));
        return m;
    }));

    static {
        ItemModel.register(TYPE, CODEC.xmap(m -> { m.type = TYPE; return m; }, m -> m));
    }

    private String[] shards = new String[4];

    protected ModelDecoratedPot() {
        super("minecraft:decorated_pot");
    }

    public static ModelDecoratedPot decoratedPot() {
        return new ModelDecoratedPot();
    }

    public ModelDecoratedPot shard(int index, String texture) {
        if (index >= 0 && index < 4) {
            this.shards[index] = texture;
        }
        return this;
    }

    public String[] getShards() {
        return shards;
    }
}
