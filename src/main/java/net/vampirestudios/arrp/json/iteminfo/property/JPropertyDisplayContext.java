package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "display_context" string property.
 */
public class JPropertyDisplayContext extends JProperty {
	public static final String TYPE = "minecraft:display_context";
	public static final MapCodec<JPropertyDisplayContext> CODEC = MapCodec.unit(JPropertyDisplayContext::new);

	static {
		JProperty.register(TYPE, CODEC);
	}

	protected JPropertyDisplayContext() {
		super(TYPE);
	}

	// Static factory method
	public static JPropertyDisplayContext displayContext() {
		return new JPropertyDisplayContext();
	}
}
