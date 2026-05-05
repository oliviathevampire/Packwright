package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "selected" boolean property.
 */
public class JPropertySelected extends JProperty {
	// empty object -> only "property" is emitted
	public static final MapCodec<JPropertySelected> CODEC = MapCodec.unit(JPropertySelected::new);

	static {
		JProperty.register("minecraft:selected", CODEC);
	}

	protected JPropertySelected() {
		super("minecraft:selected");
	}

	// Static factory method
	public static JPropertySelected selected() {
		return new JPropertySelected();
	}
}
