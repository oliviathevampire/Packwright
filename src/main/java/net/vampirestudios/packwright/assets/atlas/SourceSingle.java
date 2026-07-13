package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Adds a single file. {@code resource} is the texture location
 * ({@code textures/<path>.png}); {@code sprite} optionally renames the sprite
 * (defaults to {@code resource}).
 */
public final class SourceSingle extends AtlasSource {
	public static final String TYPE = "single";
	public static final Codec<SourceSingle> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("resource").forGetter(SourceSingle::getResource),
			Identifier.CODEC.optionalFieldOf("sprite").forGetter(s -> Optional.ofNullable(s.sprite))
	).apply(i, (resource, sprite) -> {
		SourceSingle out = new SourceSingle(resource);
		sprite.ifPresent(v -> out.sprite = v);
		return out;
	}));

	static {
		AtlasSource.register(TYPE, CODEC);
	}

	private final Identifier resource;
	private Identifier sprite;

	public SourceSingle(Identifier resource) {
		super(TYPE);
		this.resource = resource;
	}

	/** the name to give the sprite in the atlas; defaults to {@code resource} */
	public SourceSingle sprite(Identifier sprite) {
		this.sprite = sprite;
		return this;
	}

	public Identifier getResource() { return resource; }
	public Identifier getSprite() { return sprite; }
}
