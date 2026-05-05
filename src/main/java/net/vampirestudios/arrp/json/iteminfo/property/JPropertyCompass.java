package net.vampirestudios.arrp.json.iteminfo.property;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:compass" numeric property.
 */
public class JPropertyCompass extends JProperty {
    public static final String TYPE = "minecraft:compass";
    private String target; // Default value
    private boolean wobble;   // Default: true

    public JPropertyCompass() {
        super(TYPE);
    }

    public static JPropertyCompass of(String target, boolean wobble) {
        return new JPropertyCompass().wobble(wobble).target(target);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("target", target);
        json.addProperty("wobble", wobble);
        return json;
    }

    // Getters and Setters
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target for the compass.
     * Possible values: "spawn", "lodestone", "recovery", "none"
     */
    public JPropertyCompass target(String target) {
        this.target = target;
        return this;
    }

    public boolean shouldWobble() {
        return wobble;
    }

    public JPropertyCompass wobble(boolean wobble) {
        this.wobble = wobble;
        return this;
    }

    public static final MapCodec<JPropertyCompass> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.fieldOf("target").forGetter(JPropertyCompass::getTarget),
            Codec.BOOL.optionalFieldOf("wobble", true).forGetter(JPropertyCompass::shouldWobble)
    ).apply(i, JPropertyCompass::of));

    static {
        JProperty.register(TYPE, CODEC.xmap(x -> x, x -> x));
    }
}
