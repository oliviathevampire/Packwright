package net.vampirestudios.arrp.assets.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents the "minecraft:component" select property.
 */
public class PropertyComponent extends Property {
    public static final String TYPE = "minecraft:component";
    private String component;

    public PropertyComponent() {
        super(TYPE);
    }

    public PropertyComponent(String component) {
        this();
        this.component = component;
    }

    /**
     * Creates a new PropertyComponent for a specific component name.
     *
     * @param componentName The name of the component (e.g., "minecraft:item_name").
     */
    public static PropertyComponent component(String componentName) {
        PropertyComponent property = new PropertyComponent();
        property.component = componentName;
        return property;
    }

    public String getComponent() {
        return component;
    }

    public static final MapCodec<PropertyComponent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.fieldOf("component").forGetter(PropertyComponent::getComponent)
    ).apply(i, PropertyComponent::new));

    static {
        Property.register(TYPE, CODEC);
    }
}
