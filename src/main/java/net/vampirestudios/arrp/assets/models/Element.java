package net.vampirestudios.arrp.assets.models;

public class Element implements Cloneable {
	// vanilla units: 0..16
	private final float[] from = new float[3];
	private final float[] to   = new float[3];
	private Rotation rotation;
	private Boolean shade;                 // null => omit (default true)
	private Faces faces;

	// NEW: optional per-element light emission (0..15); null => omit
	private Integer light_emission;

	/** @see Model#element() */
	public Element() {}

	/* ---------------- Core setters ---------------- */

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

	/** Explicitly set shading; null means "omit" (vanilla default true). */
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

	/* ---------------- NEW: light emission ---------------- */

	/** Set per-element light emission [0..15]; null to omit. */
	public Element light(Integer level) {
		if (level == null) {
			this.light_emission = null;
		} else {
			int clamped = clamp015(level);
			this.light_emission = clamped;
		}
		return this;
	}

	/** Convenience: fullbright element (15). */
	public Element emissive() {
		this.light_emission = 15;
		return this;
	}

	/** Convenience: remove light (omit field). */
	public Element noLight() {
		this.light_emission = null;
		return this;
	}

	/* ---------------- Face sugar ---------------- */

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

	/** Quick fill all 6 faces with the same texture var. */
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

	/* ---------------- Clone (deep) ---------------- */

	@Override
	public Element clone() {
		try {
			Element c = (Element) super.clone();
			System.arraycopy(this.from, 0, c.from, 0, 3);
			System.arraycopy(this.to,   0, c.to,   0, 3);
			c.rotation = this.rotation == null ? null : this.rotation.clone();
			c.faces    = this.faces    == null ? null : this.faces.clone();
			// shade/light_emission are boxed, safe to copy refs
			c.shade = this.shade;
			c.light_emission = this.light_emission;
			return c;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/* ---------------- Internal ---------------- */

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
