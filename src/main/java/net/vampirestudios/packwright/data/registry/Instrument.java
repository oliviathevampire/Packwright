package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class Instrument {
	public static final Codec<Instrument> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("sound_event").forGetter(x -> x.soundEvent),
			Codec.FLOAT.fieldOf("use_duration").forGetter(x -> x.useDuration),
			Codec.FLOAT.fieldOf("range").forGetter(x -> x.range),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description)
	).apply(i, (soundEvent, useDuration, range, description) -> {
		Instrument out = new Instrument();
		out.soundEvent = soundEvent;
		out.useDuration = useDuration;
		out.range = range;
		out.description = description;
		return out;
	}));

	private Identifier soundEvent;
	private float useDuration;
	private float range;
	private String description;

	public static Instrument instrument() {
		return new Instrument();
	}

	public Instrument soundEvent(Identifier id) { this.soundEvent = id; return this; }

	public Instrument useDuration(float useDuration) { this.useDuration = useDuration; return this; }

	public Instrument range(float range) { this.range = range; return this; }
	public Instrument description(String description) { this.description = description; return this; }

	public Identifier getSoundEvent() { return soundEvent; }
	public float getUseDuration() { return useDuration; }
	public float getRange() { return range; }
	public String getDescription() { return description; }
}
