package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "bundle/selected_item" string property.
 */
public class JPropertyBundleSelectedItem extends JProperty {
    public static final String TYPE = "minecraft:bundle/selected_item";
    protected JPropertyBundleSelectedItem() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyBundleSelectedItem bundleSelectedItem() {
        return new JPropertyBundleSelectedItem();
    }

    public static final MapCodec<JPropertyBundleSelectedItem> CODEC = MapCodec.unit(JPropertyBundleSelectedItem::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
