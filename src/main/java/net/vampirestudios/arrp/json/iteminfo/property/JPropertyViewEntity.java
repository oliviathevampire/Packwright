package net.vampirestudios.arrp.json.iteminfo.property;

import com.mojang.serialization.MapCodec;

/**
 * Represents the "minecraft:view_entity" boolean property.
 */
public class JPropertyViewEntity extends JProperty {
	public static final MapCodec<JPropertyViewEntity> CODEC = MapCodec.unit(JPropertyViewEntity::new);

	static {
		JProperty.register("minecraft:view_entity", CODEC);
	}

	public JPropertyViewEntity() {
		super("minecraft:view_entity");
	}
}
