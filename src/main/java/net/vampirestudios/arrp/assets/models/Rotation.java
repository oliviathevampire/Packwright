package net.vampirestudios.arrp.assets.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Optional;

public class Rotation {
	private static final Codec<float[]> VEC3 = Codec.FLOAT.listOf()
			.xmap(l -> new float[]{l.get(0), l.get(1), l.get(2)},
				  a -> List.of(a[0], a[1], a[2]));

	private static final Codec<Character> CHAR_CODEC =
			Codec.STRING.xmap(s -> s.charAt(0), c -> String.valueOf(c));

	public static final Codec<Rotation> CODEC = RecordCodecBuilder.create(i -> i.group(
			VEC3.optionalFieldOf("origin").forGetter(r -> {
				float[] o = r.origin;
				return (o[0] == 0f && o[1] == 0f && o[2] == 0f) ? Optional.empty() : Optional.of(o);
			}),
			CHAR_CODEC.fieldOf("axis").forGetter(r -> r.axis),
			Codec.FLOAT.optionalFieldOf("angle").forGetter(r -> Optional.ofNullable(r.angle)),
			Codec.BOOL.optionalFieldOf("rescale").forGetter(r -> Optional.ofNullable(r.rescale))
	).apply(i, (origin, axis, angle, rescale) -> {
		Rotation rot = new Rotation(axis);
		origin.ifPresent(v -> { rot.origin[0] = v[0]; rot.origin[1] = v[1]; rot.origin[2] = v[2]; });
		angle.ifPresent(a -> rot.angle = a);
		rescale.ifPresent(rs -> rot.rescale = rs);
		return rot;
	}));

	private final float[] origin = new float[3];
	private final char axis;
	private Float angle;
	private Boolean rescale;

	/** @see Model#rotation(Direction.Axis) */
	public Rotation(Direction.Axis axis) {
		this.axis = axis.getSerializedName().charAt(0);
	}

	private Rotation(char axis) {
		this.axis = axis;
	}

	public Rotation origin(float x, float y, float z) {
		this.origin[0] = x;
		this.origin[1] = y;
		this.origin[2] = z;
		return this;
	}

	public Rotation angle(Float angle) {
		this.angle = angle;
		return this;
	}

	public Rotation rescale() {
		this.rescale = true;
		return this;
	}
}
