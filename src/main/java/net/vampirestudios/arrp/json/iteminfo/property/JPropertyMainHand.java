package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:main_hand" property.
 */
public class JPropertyMainHand extends JProperty {
	// empty object -> only "property" is emitted
	public static final MapCodec<JPropertyMainHand> CODEC = MapCodec.unit(JPropertyMainHand::new);

	static {
		JProperty.register("minecraft:main_hand", CODEC);
	}

	public JPropertyMainHand() {
		super("minecraft:main_hand");
	}

	public static JPropertyMainHand mainHand() {
		return new JPropertyMainHand();
	}
}
