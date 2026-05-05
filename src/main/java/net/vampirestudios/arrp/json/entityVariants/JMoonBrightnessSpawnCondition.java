package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.resources.Identifier;

public class JMoonBrightnessSpawnCondition extends JSpawnCondition {
    public static final Identifier TYPE = Identifier.fromNamespaceAndPath("minecraft", "moon_brightness");

    public static final MapCodec<JMoonBrightnessSpawnCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(MinMaxBounds.Doubles.CODEC.fieldOf("range").forGetter(JMoonBrightnessSpawnCondition::getRange))
                    .apply(instance, doubles -> {
						var a = new JMoonBrightnessSpawnCondition();
						a.range = doubles;
						return a;
					})
    );

    static {
        JSpawnCondition.register(TYPE.toString(), MAP_CODEC);
    }

    private MinMaxBounds.Doubles range;

    public JMoonBrightnessSpawnCondition() {
        super(TYPE.toString());
    }

    public static JMoonBrightnessSpawnCondition moonBrightness() { return new JMoonBrightnessSpawnCondition(); }

    public JMoonBrightnessSpawnCondition range(MinMaxBounds.Doubles range) {
        this.range = range;
        return this;
    }

    public MinMaxBounds.Doubles getRange() {
        return range;
    }
}
