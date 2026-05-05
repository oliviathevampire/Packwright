package net.vampirestudios.arrp.json.models;

public class JElement implements Cloneable {
	// vanilla units: 0..16
	private final float[] from = new float[3];
	private final float[] to   = new float[3];
	private JRotation rotation;
	private Boolean shade;                 // null => omit (default true)
	private JFaces faces;

	// NEW: optional per-element light emission (0..15); null => omit
	private Integer light_emission;

	/** @see JModel#element() */
	public JElement() {}

	/* ---------------- Core setters ---------------- */

	public JElement from(float x, float y, float z) {
		this.from[0] = clamp016(x);
		this.from[1] = clamp016(y);
		this.from[2] = clamp016(z);
		return this;
	}

	public JElement to(float x, float y, float z) {
		this.to[0] = clamp016(x);
		this.to[1] = clamp016(y);
		this.to[2] = clamp016(z);
		return this;
	}

	public JElement bounds(float fx, float fy, float fz, float tx, float ty, float tz) {
		return this.from(fx, fy, fz).to(tx, ty, tz);
	}

	public JElement rotation(JRotation rotation) {
		this.rotation = rotation;
		return this;
	}

	/** Explicitly set shading; null means "omit" (vanilla default true). */
	public JElement shade(Boolean shade) {
		this.shade = shade;
		return this;
	}

	public JElement noShade() {
		this.shade = Boolean.FALSE;
		return this;
	}

	public JElement faces(JFaces faces) {
		this.faces = faces;
		return this;
	}

	/* ---------------- NEW: light emission ---------------- */

	/** Set per-element light emission [0..15]; null to omit. */
	public JElement light(Integer level) {
		if (level == null) {
			this.light_emission = null;
		} else {
			int clamped = clamp015(level);
			this.light_emission = clamped;
		}
		return this;
	}

	/** Convenience: fullbright element (15). */
	public JElement emissive() {
		this.light_emission = 15;
		return this;
	}

	/** Convenience: remove light (omit field). */
	public JElement noLight() {
		this.light_emission = null;
		return this;
	}

	/* ---------------- Face sugar ---------------- */

	private JFaces ensureFaces() {
		if (this.faces == null) this.faces = JModel.faces();
		return this.faces;
	}

	public JElement north(JFace face) { ensureFaces().north(face); return this; }
	public JElement south(JFace face) { ensureFaces().south(face); return this; }
	public JElement west (JFace face) { ensureFaces().west(face);  return this; }
	public JElement east (JFace face) { ensureFaces().east(face);  return this; }
	public JElement up   (JFace face) { ensureFaces().up(face);    return this; }
	public JElement down (JFace face) { ensureFaces().down(face);  return this; }

	/** Quick fill all 6 faces with the same texture var. */
	public JElement allFaces(String texVar) {
		var f = ensureFaces();
		f.north(JModel.face(texVar));
		f.south(JModel.face(texVar));
		f.west (JModel.face(texVar));
		f.east (JModel.face(texVar));
		f.up   (JModel.face(texVar));
		f.down (JModel.face(texVar));
		return this;
	}

	/* ---------------- Clone (deep) ---------------- */

	@Override
	public JElement clone() {
		try {
			JElement c = (JElement) super.clone();
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
