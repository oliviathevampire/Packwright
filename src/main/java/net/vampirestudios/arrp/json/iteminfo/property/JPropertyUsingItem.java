package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:using_item" boolean property.
 */
public class JPropertyUsingItem extends JProperty {
	public static final String TYPE = "minecraft:using_item";
	public static final MapCodec<JPropertyUsingItem> CODEC = MapCodec.unit(JPropertyUsingItem::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	public JPropertyUsingItem() {
		super(TYPE);
	}
}
