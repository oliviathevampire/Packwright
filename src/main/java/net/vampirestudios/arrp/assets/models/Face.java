package net.vampirestudios.arrp.assets.models;

import net.minecraft.core.Direction;

public class Face implements Cloneable {
	private final float[] uv = new float[4];
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

	public Face uv(float x1, float y1, float x2, float y2) {
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

	public Face rot90() {
		this.rotation = 90;
		return this;
	}

	public Face rot180() {
		this.rotation = 180;
		return this;
	}

	public Face rot270() {
		this.rotation = 270;
		return this;
	}

	public Face tintIndex(int index) {
		this.tintindex = index;
		return this;
	}

	@Override
	public Face clone() {
		try {
			return (Face) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
