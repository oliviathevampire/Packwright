package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JPigSoundVariant {
	public static final Codec<JPigSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			SoundSet.CODEC.fieldOf("adult_sounds").forGetter(x -> x.adultSounds),
			SoundSet.CODEC.fieldOf("baby_sounds").forGetter(x -> x.babySounds)
	).apply(i, (adult, baby) -> {
		JPigSoundVariant out = new JPigSoundVariant();
		out.adultSounds = adult;
		out.babySounds = baby;
		return out;
	}));

	private SoundSet adultSounds;
	private SoundSet babySounds;

	public static JPigSoundVariant pigSoundVariant() {
		return new JPigSoundVariant();
	}

	public JPigSoundVariant adultSounds(SoundSet sounds) { this.adultSounds = sounds; return this; }
	public JPigSoundVariant babySounds(SoundSet sounds) { this.babySounds = sounds; return this; }

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
				Identifier.CODEC.fieldOf("step_sound").forGetter(x -> x.stepSound),
				Identifier.CODEC.fieldOf("eat_sound").forGetter(x -> x.eatSound)
		).apply(i, SoundSet::new));

		public SoundSet(Identifier ambientSound, Identifier hurtSound, Identifier deathSound, Identifier stepSound, Identifier eatSound) {
			this.ambientSound = ambientSound;
			this.hurtSound = hurtSound;
			this.deathSound = deathSound;
			this.stepSound = stepSound;
			this.eatSound = eatSound;
		}

		public SoundSet() {}

		private Identifier ambientSound;
		private Identifier hurtSound;
		private Identifier deathSound;
		private Identifier stepSound;
		private Identifier eatSound;

		public SoundSet ambientSound(Identifier id) { this.ambientSound = id; return this; }
		public SoundSet hurtSound(Identifier id) { this.hurtSound = id; return this; }
		public SoundSet deathSound(Identifier id) { this.deathSound = id; return this; }
		public SoundSet stepSound(Identifier id) { this.stepSound = id; return this; }
		public SoundSet eatSound(Identifier id) { this.eatSound = id; return this; }

		public Identifier getAmbientSound() { return ambientSound; }
		public Identifier getHurtSound() { return hurtSound; }
		public Identifier getDeathSound() { return deathSound; }
		public Identifier getStepSound() { return stepSound; }
		public Identifier getEatSound() { return eatSound; }
	}
}
