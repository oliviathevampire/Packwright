package net.vampirestudios.arrp.assets.models;

public class Display implements Cloneable {
	private Position thirdperson_righthand;
	private Position thirdperson_lefthand;
	private Position firstperson_righthand;
	private Position firstperson_lefthand;
	private Position gui;
	private Position head;
	private Position ground;
	private Position fixed;

	/**
	 * @see Model#display()
	 */
	public Display() {}

	public Display setThirdperson_righthand(Position thirdperson_righthand) {
		this.thirdperson_righthand = thirdperson_righthand;
		return this;
	}

	public Display setThirdperson_lefthand(Position thirdperson_lefthand) {
		this.thirdperson_lefthand = thirdperson_lefthand;
		return this;
	}

	public Display setFirstperson_righthand(Position firstperson_righthand) {
		this.firstperson_righthand = firstperson_righthand;
		return this;
	}

	public Display setFirstperson_lefthand(Position firstperson_lefthand) {
		this.firstperson_lefthand = firstperson_lefthand;
		return this;
	}

	public Display setGui(Position gui) {
		this.gui = gui;
		return this;
	}

	public Display setHead(Position head) {
		this.head = head;
		return this;
	}

	public Display setGround(Position ground) {
		this.ground = ground;
		return this;
	}

	public Display setFixed(Position fixed) {
		this.fixed = fixed;
		return this;
	}

	@Override
	public Display clone() {
		try {
			return (Display) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
