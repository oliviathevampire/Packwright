package net.vampirestudios.packwright.assets.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class Layer {
	public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("texture").forGetter(Layer::getTexture),
			Dyeable.CODEC.optionalFieldOf("dyeable").forGetter(x -> Optional.ofNullable(x.dyeable)),
			Codec.BOOL.optionalFieldOf("use_player_texture", false).forGetter(Layer::isUsePlayerTexture)
	).apply(i, (tex, dye, usePlayerTex) -> {
		Layer out = new Layer();
		out.texture(tex);
		dye.ifPresent(out::dyeable);
		out.usePlayerTexture(usePlayerTex);
		return out;
	}));

	private Identifier texture;
	private Dyeable dyeable;          // nullable
	private boolean usePlayerTexture;  // default false

	public Layer() {}

	// Static factory to match Packwright style (like ItemInfo.item())
	public static Layer layer() {
		return new Layer();
	}

	// Fluent setters
	public Layer texture(Identifier texture) {
		this.texture = texture;
		return this;
	}

	public Layer dyeable(Dyeable dyeable) {
		this.dyeable = dyeable;
		return this;
	}

	/** Convenience if you commonly create dyeable inline. */
	public Layer dyeable(Optional<Integer> colorWhenUndyed) {
		this.dyeable = new Dyeable(colorWhenUndyed);
		return this;
	}

	public Layer usePlayerTexture(boolean flag) {
		this.usePlayerTexture = flag;
		return this;
	}

	// Getters
	public Identifier getTexture() {
		return texture;
	}

	public Dyeable getDyeable() {
		return dyeable;
	}

	public boolean isUsePlayerTexture() {
		return usePlayerTexture;
	}
}
