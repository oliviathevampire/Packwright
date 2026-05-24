package net.vampirestudios.arrp.assets.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Optional;

public class Face {
	private static final Codec<float[]> VEC4 = Codec.FLOAT.listOf()
			.xmap(l -> new float[]{l.get(0), l.get(1), l.get(2), l.get(3)},
				  a -> List.of(a[0], a[1], a[2], a[3]));

	public static final Codec<Face> CODEC = RecordCodecBuilder.create(i -> i.group(
			VEC4.optionalFieldOf("uv").forGetter(f -> Optional.ofNullable(f.uv)),
			Codec.STRING.fieldOf("texture").forGetter(f -> f.texture),
			Codec.STRING.optionalFieldOf("cullface").forGetter(f -> Optional.ofNullable(f.cullface)),
			Codec.INT.optionalFieldOf("rotation").forGetter(f -> Optional.ofNullable(f.rotation)),
			Codec.INT.optionalFieldOf("tintindex").forGetter(f -> Optional.ofNullable(f.tintindex))
	).apply(i, (uv, texture, cullface, rotation, tintindex) -> {
		Face face = new Face(texture, true);
		uv.ifPresent(v -> face.uv = v);
		cullface.ifPresent(cf -> face.cullface = cf);
		rotation.ifPresent(r -> face.rotation = r);
		tintindex.ifPresent(t -> face.tintindex = t);
		return face;
	}));

	private float[] uv;
	private final String texture;
	private String cullface;
	private Integer rotation;
	private Integer tintindex;

	/**
	 * @see Model#face(String)
	 */
	public Face(String texture) {
		this.texture = '#' + texture;
	}

	private Face(String texture, boolean raw) {
		this.texture = texture;
	}

	public Face uv(float x1, float y1, float x2, float y2) {
		if (this.uv == null) this.uv = new float[4];
		this.uv[0] = x1;
		this.uv[1] = y1;
		this.uv[2] = x2;
		this.uv[3] = y2;
		return this;
	}

	public Face cullface(Direction direction) {
		this.cullface = direction.getSerializedName();
		return this;
	}

	public Face rot90()  { this.rotation = 90;  return this; }
	public Face rot180() { this.rotation = 180; return this; }
	public Face rot270() { this.rotation = 270; return this; }

	public Face tintIndex(int index) {
		this.tintindex = index;
		return this;
	}
}
