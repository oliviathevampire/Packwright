package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "bundle/selected_item" string property.
 */
public class PropertyBundleSelectedItem extends Property {
    public static final String TYPE = "minecraft:bundle/selected_item";
    protected PropertyBundleSelectedItem() {
        super(TYPE);
    }

    // Static factory method
    public static PropertyBundleSelectedItem bundleSelectedItem() {
        return new PropertyBundleSelectedItem();
    }

    public static final MapCodec<PropertyBundleSelectedItem> CODEC = MapCodec.unit(PropertyBundleSelectedItem::new);

    static {
        Property.register(TYPE, CODEC);
    }
}
