package net.vampirestudios.arrp.assets.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public class Position {
	private static final Codec<float[]> VEC3 = Codec.FLOAT.listOf()
			.xmap(l -> new float[]{l.get(0), l.get(1), l.get(2)},
				  a -> List.of(a[0], a[1], a[2]));

	public static final Codec<Position> CODEC = RecordCodecBuilder.create(i -> i.group(
			VEC3.optionalFieldOf("rotation").forGetter(p -> Optional.ofNullable(p.rotation)),
			VEC3.optionalFieldOf("translation").forGetter(p -> Optional.ofNullable(p.translation)),
			VEC3.optionalFieldOf("scale").forGetter(p -> Optional.ofNullable(p.scale))
	).apply(i, (rot, trans, scale) -> {
		Position p = new Position();
		rot.ifPresent(v -> p.rotation = v);
		trans.ifPresent(v -> p.translation = v);
		scale.ifPresent(v -> p.scale = v);
		return p;
	}));

	private float[] rotation;
	private float[] translation;
	private float[] scale;

	/** @see Model#position() */
	public Position() {}

	public Position rotation(float x, float y, float z) {
		if (this.rotation == null) this.rotation = new float[3];
		this.rotation[0] = x;
		this.rotation[1] = y;
		this.rotation[2] = z;
		return this;
	}

	public Position translation(float x, float y, float z) {
		if (this.translation == null) this.translation = new float[3];
		this.translation[0] = x;
		this.translation[1] = y;
		this.translation[2] = z;
		return this;
	}

	public Position scale(float x, float y, float z) {
		if (this.scale == null) this.scale = new float[3];
		this.scale[0] = x;
		this.scale[1] = y;
		this.scale[2] = z;
		return this;
	}
}
