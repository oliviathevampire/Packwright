package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:pull" numeric property.
 */
public class JPropertyPull extends JProperty {
	public static final String TYPE = "minecraft:pull";
	public static final MapCodec<JPropertyPull> CODEC = MapCodec.unit(JPropertyPull::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyPull() {
		super(TYPE);
	}

	// Static factory method
	public static JPropertyPull pull() {
		return new JPropertyPull();
	}
}