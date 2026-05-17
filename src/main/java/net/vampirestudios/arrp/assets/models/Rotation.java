package net.vampirestudios.arrp.assets.models;

import net.minecraft.core.Direction;

public class Rotation {
	private final float[] origin = new float[3];
	private final char axis;
	private Float angle;
	private Boolean rescale;

	/**
	 * @see Model#rotation(Direction.Axis)
	 */
	public Rotation(Direction.Axis axis) {
		this.axis = axis.getSerializedName().charAt(0);
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

	@Override
	public Rotation clone() {
		try {
			return (Rotation) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
