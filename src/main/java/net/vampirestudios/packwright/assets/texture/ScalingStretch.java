package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

/**
 * The default GUI sprite scaling: the sprite is simply stretched
 * to the size it is drawn at.
 */
public final class ScalingStretch extends Scaling {
	public static final String TYPE = "stretch";
	public static final Codec<ScalingStretch> CODEC = MapCodec.unit(ScalingStretch::new).codec();

	static {
		Scaling.register(TYPE, CODEC);
	}

	public ScalingStretch() {
		super(TYPE);
	}
}
