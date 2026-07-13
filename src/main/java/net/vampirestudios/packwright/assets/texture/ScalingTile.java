package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * GUI sprite scaling that repeats the sprite starting from the top left
 * corner. {@code width}/{@code height} are the size in pixels of one tile.
 */
public final class ScalingTile extends Scaling {
	public static final String TYPE = "tile";
	public static final Codec<ScalingTile> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("width").forGetter(ScalingTile::getWidth),
			Codec.INT.fieldOf("height").forGetter(ScalingTile::getHeight)
	).apply(i, ScalingTile::new));

	static {
		Scaling.register(TYPE, CODEC);
	}

	private final int width;
	private final int height;

	public ScalingTile(int width, int height) {
		super(TYPE);
		this.width = width;
		this.height = height;
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
}
