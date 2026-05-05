package net.vampirestudios.arrp.json.iteminfo.model.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.iteminfo.model.JItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SkullBlock;
import java.util.Locale;
import java.util.Optional;

/**
 * Represents the "minecraft:head" special model type.
 */
public class JModelHead extends JModelSpecial {
	public static final String TYPE = "minecraft:head";

	public static final MapCodec<JModelHead> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			SkullBlock.Types.CODEC.fieldOf("kind").forGetter(m -> m.kind),
			Codec.FLOAT.optionalFieldOf("animation", 0.0F).forGetter(m -> m.animation),
			Identifier.CODEC.optionalFieldOf("texture").forGetter(m -> m.texture)
	).apply(i, JModelHead::new));

	static {
		JItemModel.register(TYPE, CODEC.xmap(m -> {
			m.type = TYPE;
			return m;
		}, m -> m));
	}

	private SkullBlock.Type kind = SkullBlock.Types.PLAYER; // safe default
	private float animation = 0.0F;
	private Optional<Identifier> texture = Optional.empty();

	public JModelHead() {
		super(TYPE);
	}

	public JModelHead(SkullBlock.Type kind, float animation, Optional<Identifier> texture) {
		super(TYPE);
		this.kind = kind;
		this.animation = animation;
		this.texture = texture != null ? texture : Optional.empty();
	}

	private static SkullBlock.Type parseKind(String s) {
		String n = s.toLowerCase(Locale.ROOT);
		for (var t : SkullBlock.Types.values()) if (t.getSerializedName().equals(n)) return t;
		throw new IllegalArgumentException("Unknown head kind: " + s);
	}

	// ---- API ----
	public SkullBlock.Type getKind() {
		return kind;
	}

	public JModelHead kind(SkullBlock.Type kind) {
		this.kind = kind;
		return this;
	}

	public JModelHead kind(String kind) {
		this.kind = parseKind(kind);
		return this;
	}

	public float getAnimation() {
		return animation;
	}

	public JModelHead animation(float animation) {
		this.animation = animation;
		return this;
	}

	public Optional<Identifier> getTexture() {
		return texture;
	}

	public JModelHead texture(Optional<Identifier> texture) {
		this.texture = texture;
		return this;
	}

	public JModelHead texture(Identifier texture) {
		this.texture = Optional.ofNullable(texture);
		return this;
	}

	@Override
	public JModelHead clone() {
		JModelHead cloned = new JModelHead(this.kind, this.animation, this.texture);
		cloned.base(this.getBase());
		return cloned;
	}
}
