package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class CowSoundVariant {
	public static final Codec<CowSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("ambient_sound").forGetter(x -> x.ambientSound),
			Identifier.CODEC.fieldOf("hurt_sound").forGetter(x -> x.hurtSound),
			Identifier.CODEC.fieldOf("death_sound").forGetter(x -> x.deathSound),
			Identifier.CODEC.fieldOf("step_sound").forGetter(x -> x.stepSound)
	).apply(i, CowSoundVariant::new));

	private Identifier ambientSound;
	private Identifier hurtSound;
	private Identifier deathSound;
	private Identifier stepSound;

	public CowSoundVariant(Identifier ambientSound, Identifier hurtSound, Identifier deathSound, Identifier stepSound) {
		this.ambientSound = ambientSound;
		this.hurtSound = hurtSound;
		this.deathSound = deathSound;
		this.stepSound = stepSound;
	}

	public CowSoundVariant() {}

	public static CowSoundVariant cowSoundVariant() {
		return new CowSoundVariant();
	}

	public CowSoundVariant ambientSound(Identifier id) { this.ambientSound = Objects.requireNonNull(id, "ambientSound"); return this; }
	public CowSoundVariant hurtSound(Identifier id)    { this.hurtSound    = Objects.requireNonNull(id, "hurtSound");    return this; }
	public CowSoundVariant deathSound(Identifier id)   { this.deathSound   = Objects.requireNonNull(id, "deathSound");   return this; }
	public CowSoundVariant stepSound(Identifier id)    { this.stepSound    = Objects.requireNonNull(id, "stepSound");    return this; }

	public Identifier getAmbientSound() { return ambientSound; }
	public Identifier getHurtSound() { return hurtSound; }
	public Identifier getDeathSound() { return deathSound; }
	public Identifier getStepSound() { return stepSound; }
}
