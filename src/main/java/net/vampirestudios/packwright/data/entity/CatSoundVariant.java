package net.vampirestudios.packwright.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class CatSoundVariant {
	public static final Codec<CatSoundVariant> CODEC = RecordCodecBuilder.create(i -> i.group(
			SoundSet.CODEC.fieldOf("adult_sounds").forGetter(x -> x.adultSounds),
			SoundSet.CODEC.fieldOf("baby_sounds").forGetter(x -> x.babySounds)
	).apply(i, (adult, baby) -> {
		CatSoundVariant out = new CatSoundVariant();
		out.adultSounds = adult;
		out.babySounds = baby;
		return out;
	}));

	private SoundSet adultSounds;
	private SoundSet babySounds;

	public static CatSoundVariant catSoundVariant() {
		return new CatSoundVariant();
	}

	public CatSoundVariant adultSounds(SoundSet sounds) { this.adultSounds = sounds; return this; }
	public CatSoundVariant babySounds(SoundSet sounds) { this.babySounds = sounds; return this; }

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

		public SoundSet ambientSound(Identifier id)      { this.ambientSound      = java.util.Objects.requireNonNull(id, "ambientSound");      return this; }
		public SoundSet strayAmbientSound(Identifier id) { this.strayAmbientSound = java.util.Objects.requireNonNull(id, "strayAmbientSound"); return this; }
		public SoundSet hissSound(Identifier id)         { this.hissSound         = java.util.Objects.requireNonNull(id, "hissSound");         return this; }
		public SoundSet hurtSound(Identifier id)         { this.hurtSound         = java.util.Objects.requireNonNull(id, "hurtSound");         return this; }
		public SoundSet deathSound(Identifier id)        { this.deathSound        = java.util.Objects.requireNonNull(id, "deathSound");        return this; }
		public SoundSet eatSound(Identifier id)          { this.eatSound          = java.util.Objects.requireNonNull(id, "eatSound");          return this; }
		public SoundSet begForFoodSound(Identifier id)   { this.begForFoodSound   = java.util.Objects.requireNonNull(id, "begForFoodSound");   return this; }
		public SoundSet purrSound(Identifier id)         { this.purrSound         = java.util.Objects.requireNonNull(id, "purrSound");         return this; }
		public SoundSet purreowSound(Identifier id)      { this.purreowSound      = java.util.Objects.requireNonNull(id, "purreowSound");      return this; }

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
