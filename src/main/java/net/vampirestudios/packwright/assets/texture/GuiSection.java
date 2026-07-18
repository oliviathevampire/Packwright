package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * The {@code gui} section of a {@code .png.mcmeta} file; only used for
 * textures under {@code textures/gui/sprites/}. Controls how the sprite is
 * scaled when drawn at a size different from the texture.
 */
public class GuiSection {
	public static final Codec<GuiSection> CODEC = RecordCodecBuilder.create(i -> i.group(
			Scaling.CODEC.optionalFieldOf("scaling").forGetter(g -> Optional.ofNullable(g.scaling))
	).apply(i, scaling -> {
		GuiSection out = new GuiSection();
		scaling.ifPresent(v -> out.scaling = v);
		return out;
	}));

	private Scaling scaling;

	public static GuiSection gui() {
		return new GuiSection();
	}

	public static GuiSection gui(Scaling scaling) {
		return new GuiSection().scaling(scaling);
	}

	public GuiSection scaling(Scaling scaling) {
		this.scaling = scaling;
		return this;
	}

	public Scaling getScaling() { return scaling; }
}
