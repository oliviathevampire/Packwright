package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * The {@code texture} section of a {@code .png.mcmeta} file: rendering hints
 * for the texture.
 */
public class TextureSection {
	public static final Codec<TextureSection> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.optionalFieldOf("blur").forGetter(t -> Optional.ofNullable(t.blur)),
			Codec.BOOL.optionalFieldOf("clamp").forGetter(t -> Optional.ofNullable(t.clamp))
	).apply(i, (blur, clamp) -> {
		TextureSection out = new TextureSection();
		blur.ifPresent(v -> out.blur = v);
		clamp.ifPresent(v -> out.clamp = v);
		return out;
	}));

	private Boolean blur;
	private Boolean clamp;

	public static TextureSection texture() {
		return new TextureSection();
	}

	/** blur the texture when scaled instead of using nearest-neighbor */
	public TextureSection blur() {
		this.blur = true;
		return this;
	}

	/** clamp the texture at the edges instead of wrapping */
	public TextureSection clamp() {
		this.clamp = true;
		return this;
	}

	public Boolean getBlur() { return blur; }
	public Boolean getClamp() { return clamp; }
}
