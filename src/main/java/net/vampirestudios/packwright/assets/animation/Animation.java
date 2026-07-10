package net.vampirestudios.packwright.assets.animation;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @see #animation()
 * @see #frame(int)
 */
public class Animation {
	/**
	 * Each element in the "frames" array is either:
	 *   - an integer (frame index, uses global frametime), or
	 *   - an object { "index": N, "time": T }
	 * Encoded as integer when time is absent, object otherwise.
	 */
	private static final Codec<Frame> FRAME_ELEMENT_CODEC =
			Codec.either(
					Codec.INT.xmap(Frame::new, Frame::getIndex),
					Frame.CODEC
			).xmap(
					e -> e.map(f -> f, f -> f),
					f -> f.getTime() == null ? Either.left(f) : Either.right(f)
			);

	public static final Codec<Animation> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.optionalFieldOf("interpolate").forGetter(a -> Optional.ofNullable(a.interpolate)),
			Codec.INT.optionalFieldOf("width").forGetter(a -> Optional.ofNullable(a.width)),
			Codec.INT.optionalFieldOf("height").forGetter(a -> Optional.ofNullable(a.height)),
			Codec.INT.optionalFieldOf("frametime").forGetter(a -> Optional.ofNullable(a.frameTime)),
			FRAME_ELEMENT_CODEC.listOf().optionalFieldOf("frames")
					.forGetter(a -> a.frames == null || a.frames.isEmpty()
							? Optional.empty()
							: Optional.of(List.copyOf(a.frames)))
	).apply(i, (interpolate, width, height, frametime, frames) -> {
		Animation anim = new Animation();
		interpolate.ifPresent(v -> anim.interpolate = v);
		width.ifPresent(v -> anim.width = v);
		height.ifPresent(v -> anim.height = v);
		frametime.ifPresent(v -> anim.frameTime = v);
		frames.ifPresent(v -> anim.frames = new ArrayList<>(v));
		return anim;
	}));

	private Boolean interpolate;
	private Integer width;
	private Integer height;
	private Integer frameTime;
	private List<Frame> frames;

	public static Animation animation() {
		return new Animation();
	}

	public static Frame frame(int index) {
		return new Frame(index);
	}

	public Animation interpolate() {
		this.interpolate = true;
		return this;
	}

	public Animation width(int width) {
		this.width = width;
		return this;
	}

	public Animation height(int height) {
		this.height = height;
		return this;
	}

	public Animation frameTime(int time) {
		this.frameTime = time;
		return this;
	}

	/** Add a frame by index only (uses global frametime). */
	public Animation add(int frameIndex) {
		if (this.frames == null) this.frames = new ArrayList<>();
		this.frames.add(new Frame(frameIndex));
		return this;
	}

	/** Add a frame with a custom per-frame time. */
	public Animation add(Frame frame) {
		if (this.frames == null) this.frames = new ArrayList<>();
		this.frames.add(frame);
		return this;
	}

	public Boolean getInterpolate() { return interpolate; }
	public Integer getWidth()       { return width; }
	public Integer getHeight()      { return height; }
	public Integer getFrameTime()   { return frameTime; }
	public List<Frame> getFrames()  { return frames; }
}
