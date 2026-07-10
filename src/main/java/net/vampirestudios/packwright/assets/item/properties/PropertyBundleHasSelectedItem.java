package net.vampirestudios.packwright.assets.item.properties;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "bundle/has_selected_item" boolean property.
 */
public class PropertyBundleHasSelectedItem extends Property {
    public static final String TYPE = "minecraft:bundle/has_selected_item";
    protected PropertyBundleHasSelectedItem() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyBundleHasSelectedItem bundleHasSelectedItem() {
        return new PropertyBundleHasSelectedItem();
    }

    public static final MapCodec<PropertyBundleHasSelectedItem> CODEC = MapCodec.unit(PropertyBundleHasSelectedItem::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
