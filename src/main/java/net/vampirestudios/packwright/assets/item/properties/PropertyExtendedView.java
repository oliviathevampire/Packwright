package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:extended_view" boolean property.
 */
public class PropertyExtendedView extends Property {
    public static final String TYPE = "minecraft:extended_view";
    public PropertyExtendedView() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyExtendedView extendedView() {
        return new PropertyExtendedView();
    }

    public static final MapCodec<PropertyExtendedView> CODEC = MapCodec.unit(PropertyExtendedView::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
