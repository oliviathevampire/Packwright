package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.resources.Identifier;

public class MoonBrightnessSpawnCondition extends SpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "moon_brightness");

    public static final MapCodec<MoonBrightnessSpawnCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(MinMaxBounds.Doubles.CODEC.fieldOf("range").forGetter(MoonBrightnessSpawnCondition::getRange))
                    .apply(instance, doubles -> {
						var a = new MoonBrightnessSpawnCondition();
						a.range = doubles;
						return a;
					})
    );

    static {
        SpawnCondition.register(TYPE.toString(), MAP_CODEC);
    }

    private MinMaxBounds.Doubles range;

    public MoonBrightnessSpawnCondition() {
        super(TYPE.toString());
    }

    public static MoonBrightnessSpawnCondition moonBrightness() { return new MoonBrightnessSpawnCondition(); }

    public MoonBrightnessSpawnCondition range(MinMaxBounds.Doubles range) {
        this.range = range;
        return this;
    }

    public MinMaxBounds.Doubles getRange() {
        return range;
    }
}
