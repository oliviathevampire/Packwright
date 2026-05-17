package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class JCowSoundVariant {
	public static final Codec<JCowSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("ambient_sound").forGetter(x -> x.ambientSound),
			Identifier.CODEC.fieldOf("hurt_sound").forGetter(x -> x.hurtSound),
			Identifier.CODEC.fieldOf("death_sound").forGetter(x -> x.deathSound),
			Identifier.CODEC.fieldOf("step_sound").forGetter(x -> x.stepSound)
	).apply(i, JCowSoundVariant::new));

	private Identifier ambientSound;
	private Identifier hurtSound;
	private Identifier deathSound;
	private Identifier stepSound;

	public JCowSoundVariant(Identifier ambientSound, Identifier hurtSound, Identifier deathSound, Identifier stepSound) {
		this.ambientSound = ambientSound;
		this.hurtSound = hurtSound;
		this.deathSound = deathSound;
		this.stepSound = stepSound;
	}

	public JCowSoundVariant() {}

	public static JCowSoundVariant cowSoundVariant() {
		return new JCowSoundVariant();
	}

	public JCowSoundVariant ambientSound(Identifier id) { this.ambientSound = Objects.requireNonNull(id, "ambientSound"); return this; }
	public JCowSoundVariant hurtSound(Identifier id)    { this.hurtSound    = Objects.requireNonNull(id, "hurtSound");    return this; }
	public JCowSoundVariant deathSound(Identifier id)   { this.deathSound   = Objects.requireNonNull(id, "deathSound");   return this; }
	public JCowSoundVariant stepSound(Identifier id)    { this.stepSound    = Objects.requireNonNull(id, "stepSound");    return this; }

	public Identifier getAmbientSound() { return ambientSound; }
	public Identifier getHurtSound() { return hurtSound; }
	public Identifier getDeathSound() { return deathSound; }
	public Identifier getStepSound() { return stepSound; }
}
