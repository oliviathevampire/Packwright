package net.vampirestudios.arrp.assets.models;

public class Faces implements Cloneable {
	private Face up;
	private Face down;
	private Face north;
	private Face south;
	private Face east;
	private Face west;

	/**
	 * @see Model#faces()
	 */
	public Faces() {}

	public Faces up(Face face) {
		this.up = face;
		return this;
	}

	public Faces down(Face face) {
		this.down = face;
		return this;
	}

	public Faces north(Face face) {
		this.north = face;
		return this;
	}

	public Faces south(Face face) {
		this.south = face;
		return this;
	}

	public Faces east(Face face) {
		this.east = face;
		return this;
	}

	public Faces west(Face face) {
		this.west = face;
		return this;
	}

	public static Faces allSame(Face face) {
		Faces faces = new Faces();
		faces.up(face);
		faces.down(face);
		faces.north(face);
		faces.south(face);
		faces.east(face);
		faces.west(face);
		return faces;
	}

	@Override
	public Faces clone() {
		try {
			return (Faces) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
