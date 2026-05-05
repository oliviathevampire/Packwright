package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "bundle/has_selected_item" boolean property.
 */
public class JPropertyBundleHasSelectedItem extends JProperty {
    public static final String TYPE = "minecraft:bundle/has_selected_item";
    protected JPropertyBundleHasSelectedItem() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyBundleHasSelectedItem bundleHasSelectedItem() {
        return new JPropertyBundleHasSelectedItem();
    }

    public static final MapCodec<JPropertyBundleHasSelectedItem> CODEC = MapCodec.unit(JPropertyBundleHasSelectedItem::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
