package net.vampirestudios.arrp.assets.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class Display {
	public static final Codec<Display> CODEC = RecordCodecBuilder.create(i -> i.group(
			Position.CODEC.optionalFieldOf("thirdperson_righthand").forGetter(d -> Optional.ofNullable(d.thirdperson_righthand)),
			Position.CODEC.optionalFieldOf("thirdperson_lefthand").forGetter(d -> Optional.ofNullable(d.thirdperson_lefthand)),
			Position.CODEC.optionalFieldOf("firstperson_righthand").forGetter(d -> Optional.ofNullable(d.firstperson_righthand)),
			Position.CODEC.optionalFieldOf("firstperson_lefthand").forGetter(d -> Optional.ofNullable(d.firstperson_lefthand)),
			Position.CODEC.optionalFieldOf("gui").forGetter(d -> Optional.ofNullable(d.gui)),
			Position.CODEC.optionalFieldOf("head").forGetter(d -> Optional.ofNullable(d.head)),
			Position.CODEC.optionalFieldOf("ground").forGetter(d -> Optional.ofNullable(d.ground)),
			Position.CODEC.optionalFieldOf("fixed").forGetter(d -> Optional.ofNullable(d.fixed))
	).apply(i, (tpr, tpl, fpr, fpl, gui, head, ground, fixed) -> {
		Display d = new Display();
		tpr.ifPresent(d::setThirdperson_righthand);
		tpl.ifPresent(d::setThirdperson_lefthand);
		fpr.ifPresent(d::setFirstperson_righthand);
		fpl.ifPresent(d::setFirstperson_lefthand);
		gui.ifPresent(d::setGui);
		head.ifPresent(d::setHead);
		ground.ifPresent(d::setGround);
		fixed.ifPresent(d::setFixed);
		return d;
	}));

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

	public Display setThirdperson_righthand(Position thirdPersonRightHand) {
		this.thirdperson_righthand = thirdPersonRightHand;
		return this;
	}

	public Display setThirdperson_lefthand(Position thirdPersonLeftHand) {
		this.thirdperson_lefthand = thirdPersonLeftHand;
		return this;
	}

	public Display setFirstperson_righthand(Position firstPersonRightHand) {
		this.firstperson_righthand = firstPersonRightHand;
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
}
