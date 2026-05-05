package net.vampirestudios.arrp.json.iteminfo.property;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:component" select property.
 */
public class JPropertyComponent extends JProperty {
    public static final String TYPE = "minecraft:component";
    private String component;

    public JPropertyComponent() {
        super(TYPE);
    }

    public JPropertyComponent(String component) {
        this();
        this.component = component;
    }

    /**
     * Creates a new JPropertyComponent for a specific component name.
     *
     * @param componentName The name of the component (e.g., "minecraft:item_name").
     */
    public static JPropertyComponent component(String componentName) {
        JPropertyComponent property = new JPropertyComponent();
        property.component = componentName;
        return property;
    }

    public String getComponent() {
        return component;
    }

    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("component", component);
        return json;
    }

    public static final MapCodec<JPropertyComponent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.fieldOf("component").forGetter(JPropertyComponent::getComponent)
    ).apply(i, JPropertyComponent::new));

    static {
        JProperty.register(TYPE, CODEC.xmap(x -> x, x -> x));
    }
}
