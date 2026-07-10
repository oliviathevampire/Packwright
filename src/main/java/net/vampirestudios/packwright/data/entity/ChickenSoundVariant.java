package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class ChickenSoundVariant {
	public static final Codec<ChickenSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			SoundSet.CODEC.fieldOf("adult_sounds").forGetter(x -> x.adultSounds),
			SoundSet.CODEC.fieldOf("baby_sounds").forGetter(x -> x.babySounds)
	).apply(i, (adult, baby) -> {
		ChickenSoundVariant out = new ChickenSoundVariant();
		out.adultSounds = adult;
		out.babySounds = baby;
		return out;
	}));

	private SoundSet adultSounds;
	private SoundSet babySounds;

	public ChickenSoundVariant(SoundSet adultSounds, SoundSet babySounds) {
		this.adultSounds = adultSounds;
		this.babySounds = babySounds;
	}

	public ChickenSoundVariant() {}

	public static ChickenSoundVariant chickenSoundVariant() {
		return new ChickenSoundVariant();
	}

	public ChickenSoundVariant adultSounds(SoundSet sounds) { this.adultSounds = sounds; return this; }
	public ChickenSoundVariant babySounds(SoundSet sounds) { this.babySounds = sounds; return this; }

	public SoundSet getAdultSounds() { return adultSounds; }
	public SoundSet getBabySounds() { return babySounds; }

	public static SoundSet sounds() {
		return new SoundSet();
	}

	public static class SoundSet {
		public static final Codec<SoundSet> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("ambient_sound").forGetter(x -> x.ambientSound),
				Identifier.CODEC.fieldOf("hurt_sound").forGetter(x -> x.hurtSound),
				Identifier.CODEC.fieldOf("death_sound").forGetter(x -> x.deathSound),
				Identifier.CODEC.fieldOf("step_sound").forGetter(x -> x.stepSound)
		).apply(i, SoundSet::new));

		public SoundSet(Identifier ambientSound, Identifier hurtSound, Identifier deathSound, Identifier stepSound) {
			this.ambientSound = ambientSound;
			this.hurtSound = hurtSound;
			this.deathSound = deathSound;
			this.stepSound = stepSound;
		}

		public SoundSet() {
		}

		private Identifier ambientSound;
		private Identifier hurtSound;
		private Identifier deathSound;
		private Identifier stepSound;

		public SoundSet ambientSound(Identifier id) { this.ambientSound = id; return this; }
		public SoundSet hurtSound(Identifier id) { this.hurtSound = id; return this; }
		public SoundSet deathSound(Identifier id) { this.deathSound = id; return this; }
		public SoundSet stepSound(Identifier id) { this.stepSound = id; return this; }

		public Identifier getAmbientSound() { return ambientSound; }
		public Identifier getHurtSound() { return hurtSound; }
		public Identifier getDeathSound() { return deathSound; }
		public Identifier getStepSound() { return stepSound; }
	}
}
