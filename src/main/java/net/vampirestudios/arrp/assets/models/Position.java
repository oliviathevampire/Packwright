package net.vampirestudios.arrp.assets.models;

@SuppressWarnings ("MismatchedReadAndWriteOfArray")
public class Position implements Cloneable {
	private float[] rotation = new float[3];
	private float[] translation = new float[3];
	private float[] scale = new float[3];

	/**
	 * @see Model#position()
	 */
	public Position() {}

	public Position rotation(float x, float y, float z) {
		this.rotation[0] = x;
		this.rotation[1] = y;
		this.rotation[2] = z;
		return this;
	}

	public Position translation(float x, float y, float z) {
		this.translation[0] = x;
		this.translation[1] = y;
		this.translation[2] = z;
		return this;
	}

	public Position scale(float x, float y, float z) {
		this.scale[0] = x;
		this.scale[1] = y;
		this.scale[2] = z;
		return this;
	}

	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
