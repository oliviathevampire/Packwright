package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JukeboxSong {
	public static final Codec<JukeboxSong> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("sound_event").forGetter(x -> x.soundEvent),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description),
			Codec.FLOAT.fieldOf("length_in_seconds").forGetter(x -> x.lengthInSeconds),
			Codec.intRange(0, 15).fieldOf("comparator_output").forGetter(x -> x.comparatorOutput)
	).apply(i, (soundEvent, description, lengthInSeconds, comparatorOutput) -> {
		JukeboxSong out = new JukeboxSong();
		out.soundEvent = soundEvent;
		out.description = description;
		out.lengthInSeconds = lengthInSeconds;
		out.comparatorOutput = comparatorOutput;
		return out;
	}));

	private Identifier soundEvent;
	private String description;
	private float lengthInSeconds;
	private int comparatorOutput;

	public static JukeboxSong jukeboxSong() {
		return new JukeboxSong();
	}

	public JukeboxSong soundEvent(Identifier id) { this.soundEvent = id; return this; }
	public JukeboxSong description(String description) { this.description = description; return this; }
	public JukeboxSong lengthInSeconds(float lengthInSeconds) { this.lengthInSeconds = lengthInSeconds; return this; }
	public JukeboxSong comparatorOutput(int comparatorOutput) { this.comparatorOutput = comparatorOutput; return this; }

	public Identifier getSoundEvent() { return soundEvent; }
	public String getDescription() { return description; }
	public float getLengthInSeconds() { return lengthInSeconds; }
	public int getComparatorOutput() { return comparatorOutput; }
}
