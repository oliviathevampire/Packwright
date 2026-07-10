package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class Instrument {
	public static final Codec<Instrument> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("sound_event").forGetter(x -> x.soundEvent),
			Codec.FLOAT.fieldOf("use_duration").forGetter(x -> x.useDuration),
			Codec.INT.optionalFieldOf("durability_damage").forGetter(x -> Optional.ofNullable(x.durabilityDamage)),
			Codec.FLOAT.fieldOf("range").forGetter(x -> x.range),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description)
	).apply(i, (soundEvent, useDuration, durabilityDamage, range, description) -> {
		Instrument out = new Instrument();
		out.soundEvent = soundEvent;
		out.useDuration = useDuration;
		out.durabilityDamage = durabilityDamage.orElse(null);
		out.range = range;
		out.description = description;
		return out;
	}));

	private Identifier soundEvent;
	private float useDuration;
	private Integer durabilityDamage;
	private float range;
	private String description;

	public static Instrument instrument() {
		return new Instrument();
	}

	public Instrument soundEvent(Identifier id) { this.soundEvent = id; return this; }

	/** {@code 0} means no cooldown (allowed since 26.3) */
	public Instrument useDuration(float useDuration) { this.useDuration = useDuration; return this; }

	/** durability consumed on each use, non-negative (since 26.3) */
	public Instrument durabilityDamage(int durabilityDamage) {
		if (durabilityDamage < 0) {
			throw new IllegalArgumentException("durability_damage must be non-negative: " + durabilityDamage);
		}
		this.durabilityDamage = durabilityDamage;
		return this;
	}

	public Instrument range(float range) { this.range = range; return this; }
	public Instrument description(String description) { this.description = description; return this; }

	public Identifier getSoundEvent() { return soundEvent; }
	public float getUseDuration() { return useDuration; }
	public Integer getDurabilityDamage() { return durabilityDamage; }
	public float getRange() { return range; }
	public String getDescription() { return description; }
}
