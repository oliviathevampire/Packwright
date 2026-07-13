package net.vampirestudios.packwright.assets.texture;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * GUI sprite scaling that slices the sprite into 4 corners, 4 edges and
 * 1 center; corners are drawn as-is, edges and center are stretched (or
 * tiled) to fill the drawn size.
 */
public final class ScalingNineSlice extends Scaling {
	public static final String TYPE = "nine_slice";

	/** per-side border sizes in pixels; encoded as a single int when all four sides are equal */
	public record Border(int left, int top, int right, int bottom) {
		public static final Codec<Border> OBJECT_CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("left").forGetter(Border::left),
				Codec.INT.fieldOf("top").forGetter(Border::top),
				Codec.INT.fieldOf("right").forGetter(Border::right),
				Codec.INT.fieldOf("bottom").forGetter(Border::bottom)
		).apply(i, Border::new));

		public static final Codec<Border> CODEC = Codec.either(Codec.INT, OBJECT_CODEC).xmap(
				e -> e.map(v -> new Border(v, v, v, v), b -> b),
				b -> b.left() == b.top() && b.left() == b.right() && b.left() == b.bottom()
						? Either.left(b.left()) : Either.right(b)
		);
	}

	public static final Codec<ScalingNineSlice> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("width").forGetter(ScalingNineSlice::getWidth),
			Codec.INT.fieldOf("height").forGetter(ScalingNineSlice::getHeight),
			Border.CODEC.fieldOf("border").forGetter(ScalingNineSlice::getBorder),
			Codec.BOOL.optionalFieldOf("stretch_inner").forGetter(s -> Optional.ofNullable(s.stretchInner))
	).apply(i, (width, height, border, stretchInner) -> {
		ScalingNineSlice out = new ScalingNineSlice(width, height, border.left(), border.top(), border.right(), border.bottom());
		stretchInner.ifPresent(v -> out.stretchInner = v);
		return out;
	}));

	static {
		Scaling.register(TYPE, CODEC);
	}

	private final int width;
	private final int height;
	private final Border border;
	private Boolean stretchInner;

	public ScalingNineSlice(int width, int height, int border) {
		this(width, height, border, border, border, border);
	}

	public ScalingNineSlice(int width, int height, int left, int top, int right, int bottom) {
		super(TYPE);
		this.width = width;
		this.height = height;
		this.border = new Border(left, top, right, bottom);
	}

	/** stretch the inner parts of the sprite instead of tiling them */
	public ScalingNineSlice stretchInner() {
		this.stretchInner = true;
		return this;
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Border getBorder() { return border; }
	public Boolean getStretchInner() { return stretchInner; }
}
