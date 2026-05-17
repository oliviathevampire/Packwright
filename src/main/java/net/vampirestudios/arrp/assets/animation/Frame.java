package net.vampirestudios.arrp.assets.animation;


public class Frame implements Cloneable {
	private final int index;
	private Integer time;

	/**
	 * @see Animation#frame(int)
	 */
	public Frame(int index) {this.index = index;}

	public Frame time(int time) {
		this.time = time;
		return this;
	}

	@Override
	public Frame clone() {
		try {
			return (Frame) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
