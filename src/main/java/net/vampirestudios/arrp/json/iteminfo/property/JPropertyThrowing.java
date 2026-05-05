package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:throwing" boolean property.
 */
public class JPropertyThrowing extends JProperty {
	// empty object -> only "property" is emitted
	public static final MapCodec<JPropertyThrowing> CODEC = MapCodec.unit(JPropertyThrowing::new);

	static {
		JProperty.register("minecraft:throwing", CODEC);
	}

	protected JPropertyThrowing() {
		super("minecraft:throwing");
	}

	// Static factory method
	public static JPropertyThrowing throwing() {
		return new JPropertyThrowing();
	}
}
