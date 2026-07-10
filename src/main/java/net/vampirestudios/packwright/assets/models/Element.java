package net.vampirestudios.packwright.assets.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public class Element {
	private static final Codec<float[]> VEC3 = Codec.FLOAT.listOf()
			.xmap(l -> new float[]{l.get(0), l.get(1), l.get(2)},
				  a -> List.of(a[0], a[1], a[2]));

	public static final Codec<Element> CODEC = RecordCodecBuilder.create(i -> i.group(
			VEC3.fieldOf("from").forGetter(e -> e.from),
			VEC3.fieldOf("to").forGetter(e -> e.to),
			Rotation.CODEC.optionalFieldOf("rotation").forGetter(e -> Optional.ofNullable(e.rotation)),
			Codec.BOOL.optionalFieldOf("shade").forGetter(e -> Optional.ofNullable(e.shade)),
			Faces.CODEC.optionalFieldOf("faces").forGetter(e -> Optional.ofNullable(e.faces)),
			Codec.INT.optionalFieldOf("light_emission").forGetter(e -> Optional.ofNullable(e.light_emission))
	).apply(i, (from, to, rotation, shade, faces, light) -> {
		Element e = new Element();
		e.from(from[0], from[1], from[2]);
		e.to(to[0], to[1], to[2]);
		rotation.ifPresent(e::rotation);
		shade.ifPresent(e::shade);
		faces.ifPresent(e::faces);
		light.ifPresent(e::light);
		return e;
	}));

	private final float[] from = new float[3];
	private final float[] to   = new float[3];
	private Rotation rotation;
	private Boolean shade;
	private Faces faces;
	private Integer light_emission;

	/** @see Model#element() */
	public Element() {}

	public Element from(float x, float y, float z) {
		this.from[0] = clamp016(x);
		this.from[1] = clamp016(y);
		this.from[2] = clamp016(z);
		return this;
	}

	public Element to(float x, float y, float z) {
		this.to[0] = clamp016(x);
		this.to[1] = clamp016(y);
		this.to[2] = clamp016(z);
		return this;
	}

	public Element bounds(float fx, float fy, float fz, float tx, float ty, float tz) {
		return this.from(fx, fy, fz).to(tx, ty, tz);
	}

	public Element rotation(Rotation rotation) {
		this.rotation = rotation;
		return this;
	}

	public Element shade(Boolean shade) {
		this.shade = shade;
		return this;
	}

	public Element noShade() {
		this.shade = Boolean.FALSE;
		return this;
	}

	public Element faces(Faces faces) {
		this.faces = faces;
		return this;
	}

	public Element light(Integer level) {
		this.light_emission = level == null ? null : clamp015(level);
		return this;
	}

	public Element emissive() {
		this.light_emission = 15;
		return this;
	}

	public Element noLight() {
		this.light_emission = null;
		return this;
	}

	private Faces ensureFaces() {
		if (this.faces == null) this.faces = Model.faces();
		return this.faces;
	}

	public Element north(Face face) { ensureFaces().north(face); return this; }
	public Element south(Face face) { ensureFaces().south(face); return this; }
	public Element west (Face face) { ensureFaces().west(face);  return this; }
	public Element east (Face face) { ensureFaces().east(face);  return this; }
	public Element up   (Face face) { ensureFaces().up(face);    return this; }
	public Element down (Face face) { ensureFaces().down(face);  return this; }

	public Element allFaces(String texVar) {
		var f = ensureFaces();
		f.north(Model.face(texVar));
		f.south(Model.face(texVar));
		f.west (Model.face(texVar));
		f.east (Model.face(texVar));
		f.up   (Model.face(texVar));
		f.down (Model.face(texVar));
		return this;
	}

	private static float clamp016(float v) {
		if (v < 0f) return 0f;
		if (v > 16f) return 16f;
		return v;
	}

	private static int clamp015(int v) {
		if (v < 0) return 0;
		if (v > 15) return 15;
		return v;
	}
}
