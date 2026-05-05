package net.vampirestudios.arrp.json.equipmentinfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class JLayer {
	public static final Codec<JLayer> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("texture").forGetter(JLayer::getTexture),
			JDyeable.CODEC.optionalFieldOf("dyeable").forGetter(x -> Optional.ofNullable(x.dyeable)),
			Codec.BOOL.optionalFieldOf("use_player_texture", false).forGetter(JLayer::isUsePlayerTexture)
	).apply(i, (tex, dye, usePlayerTex) -> {
		JLayer out = new JLayer();
		out.texture(tex);
		dye.ifPresent(out::dyeable);
		out.usePlayerTexture(usePlayerTex);
		return out;
	}));

	private Identifier texture;
	private JDyeable dyeable;          // nullable
	private boolean usePlayerTexture;  // default false

	public JLayer() {}

	// Static factory to match ARRP style (like JItemInfo.item())
	public static JLayer layer() {
		return new JLayer();
	}

	// Fluent setters
	public JLayer texture(Identifier texture) {
		this.texture = texture;
		return this;
	}

	public JLayer dyeable(JDyeable dyeable) {
		this.dyeable = dyeable;
		return this;
	}

	/** Convenience if you commonly create dyeable inline. */
	public JLayer dyeable(Optional<Integer> colorWhenUndyed) {
		this.dyeable = new JDyeable(colorWhenUndyed);
		return this;
	}

	public JLayer usePlayerTexture(boolean flag) {
		this.usePlayerTexture = flag;
		return this;
	}

	// Getters
	public Identifier getTexture() {
		return texture;
	}

	public JDyeable getDyeable() {
		return dyeable;
	}

	public boolean isUsePlayerTexture() {
		return usePlayerTexture;
	}
}
