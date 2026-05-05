package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;

public class JJukeboxSong {
	public static final Codec<JJukeboxSong> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("sound_event").forGetter(x -> x.soundEvent),
			ComponentSerialization.CODEC.fieldOf("description").forGetter(x -> x.description),
			Codec.FLOAT.fieldOf("length_in_seconds").forGetter(x -> x.lengthInSeconds),
			Codec.intRange(0, 15).fieldOf("comparator_output").forGetter(x -> x.comparatorOutput)
	).apply(i, (soundEvent, description, lengthInSeconds, comparatorOutput) -> {
		JJukeboxSong out = new JJukeboxSong();
		out.soundEvent = soundEvent;
		out.description = description;
		out.lengthInSeconds = lengthInSeconds;
		out.comparatorOutput = comparatorOutput;
		return out;
	}));

	private Identifier soundEvent;
	private Component description;
	private float lengthInSeconds;
	private int comparatorOutput;

	public static JJukeboxSong jukeboxSong() {
		return new JJukeboxSong();
	}

	public JJukeboxSong soundEvent(Identifier id) { this.soundEvent = id; return this; }
	public JJukeboxSong description(Component description) { this.description = description; return this; }
	public JJukeboxSong description(String description) { return this.description(Component.literal(description)); }
	public JJukeboxSong lengthInSeconds(float lengthInSeconds) { this.lengthInSeconds = lengthInSeconds; return this; }
	public JJukeboxSong comparatorOutput(int comparatorOutput) { this.comparatorOutput = comparatorOutput; return this; }

	public Identifier getSoundEvent() { return soundEvent; }
	public Component getDescription() { return description; }
	public float getLengthInSeconds() { return lengthInSeconds; }
	public int getComparatorOutput() { return comparatorOutput; }
}
