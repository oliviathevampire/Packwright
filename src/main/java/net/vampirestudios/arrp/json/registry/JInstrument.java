package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JInstrument {
	public static final Codec<JInstrument> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("sound_event").forGetter(x -> x.soundEvent),
			Codec.FLOAT.fieldOf("use_duration").forGetter(x -> x.useDuration),
			Codec.FLOAT.fieldOf("range").forGetter(x -> x.range),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description)
	).apply(i, (soundEvent, useDuration, range, description) -> {
		JInstrument out = new JInstrument();
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

	public static JInstrument instrument() {
		return new JInstrument();
	}

	public JInstrument soundEvent(Identifier id) { this.soundEvent = id; return this; }
	public JInstrument useDuration(float useDuration) { this.useDuration = useDuration; return this; }
	public JInstrument range(float range) { this.range = range; return this; }
	public JInstrument description(String description) { this.description = description; return this; }

	public Identifier getSoundEvent() { return soundEvent; }
	public float getUseDuration() { return useDuration; }
	public float getRange() { return range; }
	public String getDescription() { return description; }
}
