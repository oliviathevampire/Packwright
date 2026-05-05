package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:extended_view" boolean property.
 */
public class JPropertyExtendedView extends JProperty {
    public static final String TYPE = "minecraft:extended_view";
    public JPropertyExtendedView() {
        super(TYPE);
    }

    // Static factory method
    public static JPropertyExtendedView extendedView() {
        return new JPropertyExtendedView();
    }

    public static final MapCodec<JPropertyExtendedView> CODEC = MapCodec.unit(JPropertyExtendedView::new);

    static {
        JProperty.register(TYPE, CODEC);
    }
}
