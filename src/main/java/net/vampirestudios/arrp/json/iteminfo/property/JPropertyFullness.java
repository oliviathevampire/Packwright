package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:fullness" numeric property.
 */
public class JPropertyFullness extends JProperty {
	public static final String TYPE = "minecraft:bundle/fullness";
	public static final MapCodec<JPropertyFullness> CODEC = MapCodec.unit(JPropertyFullness::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyFullness() {
		super(TYPE);
	}

	// Static factory method
	public static JPropertyFullness fullness() {
		return new JPropertyFullness();
	}
}
