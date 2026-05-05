package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:pulling" boolean property.
 */
public class JPropertyPulling extends JProperty {
	public static final String TYPE = "minecraft:pulling";
	public static final MapCodec<JPropertyPulling> CODEC = MapCodec.unit(JPropertyPulling::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyPulling() {
		super(TYPE);
	}

	// Static factory method
	public static JPropertyPulling pulling() {
		return new JPropertyPulling();
	}
}