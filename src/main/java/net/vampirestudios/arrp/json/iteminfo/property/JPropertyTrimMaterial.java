package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "trim_material" string property.
 */
public class JPropertyTrimMaterial extends JProperty {
	public static final MapCodec<JPropertyTrimMaterial> CODEC = MapCodec.unit(JPropertyTrimMaterial::new);

	static {
		JProperty.register("minecraft:trim_material", CODEC);
	}

	protected JPropertyTrimMaterial() {
		super("minecraft:trim_material");
	}

	public static JPropertyTrimMaterial trimMaterial() {
		return new JPropertyTrimMaterial();
	}
}
