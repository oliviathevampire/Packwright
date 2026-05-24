package net.vampirestudios.arrp.assets.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class Frame {
	public static final Codec<Frame> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("index").forGetter(f -> f.index),
			Codec.INT.optionalFieldOf("time").forGetter(f -> Optional.ofNullable(f.time))
	).apply(i, (index, time) -> {
		Frame frame = new Frame(index);
		time.ifPresent(t -> frame.time = t);
		return frame;
	}));

	private final int index;
	private Integer time;

	public Frame(int index) { this.index = index; }

	public Frame time(int time) {
		this.time = time;
		return this;
	}

	public int getIndex() { return index; }
	public Integer getTime() { return time; }
}
