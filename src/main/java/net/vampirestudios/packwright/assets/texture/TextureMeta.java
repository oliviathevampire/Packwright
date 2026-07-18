package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.assets.animation.Animation;

import java.util.Optional;

/**
 * A complete {@code .png.mcmeta} file. Composes all vanilla sections:
 * {@code animation}, {@code texture} (blur/clamp), {@code gui} (sprite
 * scaling) and {@code villager} (hat visibility). All sections are optional.
 *
 * @see #meta()
 * @see net.vampirestudios.packwright.api.RuntimeResourcePack#addTextureMeta
 */
public class TextureMeta {
	public static final Codec<TextureMeta> CODEC = RecordCodecBuilder.create(i -> i.group(
			Animation.CODEC.optionalFieldOf("animation").forGetter(m -> Optional.ofNullable(m.animation)),
			TextureSection.CODEC.optionalFieldOf("texture").forGetter(m -> Optional.ofNullable(m.texture)),
			GuiSection.CODEC.optionalFieldOf("gui").forGetter(m -> Optional.ofNullable(m.gui)),
			VillagerSection.CODEC.optionalFieldOf("villager").forGetter(m -> Optional.ofNullable(m.villager))
	).apply(i, (animation, texture, gui, villager) -> {
		TextureMeta out = new TextureMeta();
		animation.ifPresent(v -> out.animation = v);
		texture.ifPresent(v -> out.texture = v);
		gui.ifPresent(v -> out.gui = v);
		villager.ifPresent(v -> out.villager = v);
		return out;
	}));

	private Animation animation;
	private TextureSection texture;
	private GuiSection gui;
	private VillagerSection villager;

	public static TextureMeta meta() {
		return new TextureMeta();
	}

	public TextureMeta animation(Animation animation) {
		this.animation = animation;
		return this;
	}

	public TextureMeta texture(TextureSection texture) {
		this.texture = texture;
		return this;
	}

	public TextureMeta gui(GuiSection gui) {
		this.gui = gui;
		return this;
	}

	/** shortcut for {@code gui(GuiSection.gui(scaling))} */
	public TextureMeta guiScaling(Scaling scaling) {
		return gui(GuiSection.gui(scaling));
	}

	public TextureMeta villager(VillagerSection villager) {
		this.villager = villager;
		return this;
	}

	public Animation getAnimation() { return animation; }
	public TextureSection getTexture() { return texture; }
	public GuiSection getGui() { return gui; }
	public VillagerSection getVillager() { return villager; }
}
