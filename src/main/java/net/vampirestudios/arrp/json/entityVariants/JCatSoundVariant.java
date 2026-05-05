package net.vampirestudios.arrp.json.entityVariants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JCatSoundVariant {
	public static final Codec<JCatSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			SoundSet.CODEC.fieldOf("adult_sounds").forGetter(x -> x.adultSounds),
			SoundSet.CODEC.fieldOf("baby_sounds").forGetter(x -> x.babySounds)
	).apply(i, (adult, baby) -> {
		JCatSoundVariant out = new JCatSoundVariant();
		out.adultSounds = adult;
		out.babySounds = baby;
		return out;
	}));

	private SoundSet adultSounds;
	private SoundSet babySounds;

	public static JCatSoundVariant catSoundVariant() {
		return new JCatSoundVariant();
	}

	public JCatSoundVariant adultSounds(SoundSet sounds) { this.adultSounds = sounds; return this; }
	public JCatSoundVariant babySounds(SoundSet sounds) { this.babySounds = sounds; return this; }

	public SoundSet getAdultSounds() { return adultSounds; }
	public SoundSet getBabySounds() { return babySounds; }

	public static SoundSet sounds() {
		return new SoundSet();
	}

	public static class SoundSet {
		public static final Codec<SoundSet> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("ambient_sound").forGetter(x -> x.ambientSound),
				Identifier.CODEC.fieldOf("stray_ambient_sound").forGetter(x -> x.strayAmbientSound),
				Identifier.CODEC.fieldOf("hiss_sound").forGetter(x -> x.hissSound),
				Identifier.CODEC.fieldOf("hurt_sound").forGetter(x -> x.hurtSound),
				Identifier.CODEC.fieldOf("death_sound").forGetter(x -> x.deathSound),
				Identifier.CODEC.fieldOf("eat_sound").forGetter(x -> x.eatSound),
				Identifier.CODEC.fieldOf("beg_for_food_sound").forGetter(x -> x.begForFoodSound),
				Identifier.CODEC.fieldOf("purr_sound").forGetter(x -> x.purrSound),
				Identifier.CODEC.fieldOf("purreow_sound").forGetter(x -> x.purreowSound)
		).apply(i, SoundSet::new));

		public SoundSet(Identifier ambientSound, Identifier strayAmbientSound, Identifier hissSound, Identifier hurtSound, Identifier deathSound,
						Identifier eatSound, Identifier begForFoodSound, Identifier purrSound, Identifier purreowSound) {
			this.ambientSound = ambientSound;
			this.strayAmbientSound = strayAmbientSound;
			this.hissSound = hissSound;
			this.hurtSound = hurtSound;
			this.deathSound = deathSound;
			this.eatSound = eatSound;
			this.begForFoodSound = begForFoodSound;
			this.purrSound = purrSound;
			this.purreowSound = purreowSound;
		}

		public SoundSet() {}

		private Identifier ambientSound;
		private Identifier strayAmbientSound;
		private Identifier hissSound;
		private Identifier hurtSound;
		private Identifier deathSound;
		private Identifier eatSound;
		private Identifier begForFoodSound;
		private Identifier purrSound;
		private Identifier purreowSound;

		public SoundSet ambientSound(Identifier id) { this.ambientSound = id; return this; }
		public SoundSet strayAmbientSound(Identifier id) { this.strayAmbientSound = id; return this; }
		public SoundSet hissSound(Identifier id) { this.hissSound = id; return this; }
		public SoundSet hurtSound(Identifier id) { this.hurtSound = id; return this; }
		public SoundSet deathSound(Identifier id) { this.deathSound = id; return this; }
		public SoundSet eatSound(Identifier id) { this.eatSound = id; return this; }
		public SoundSet begForFoodSound(Identifier id) { this.begForFoodSound = id; return this; }
		public SoundSet purrSound(Identifier id) { this.purrSound = id; return this; }
		public SoundSet purreowSound(Identifier id) { this.purreowSound = id; return this; }

		public Identifier getAmbientSound() { return ambientSound; }
		public Identifier getStrayAmbientSound() { return strayAmbientSound; }
		public Identifier getHissSound() { return hissSound; }
		public Identifier getHurtSound() { return hurtSound; }
		public Identifier getDeathSound() { return deathSound; }
		public Identifier getEatSound() { return eatSound; }
		public Identifier getBegForFoodSound() { return begForFoodSound; }
		public Identifier getPurrSound() { return purrSound; }
		public Identifier getPurreowSound() { return purreowSound; }
	}
}
