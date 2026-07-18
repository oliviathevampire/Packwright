package net.vampirestudios.packwright.data.worldgen.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Objects;

/**
 * One biome entry in an explicit multi-noise source.
 */
public record Entry(Parameters parameters, Identifier biome) {
	public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Parameters.CODEC.fieldOf("parameters").forGetter(Entry::parameters),
			Identifier.CODEC.fieldOf("biome").forGetter(Entry::biome)
	).apply(instance, Entry::new));

	public Entry {
		Objects.requireNonNull(parameters, "parameters");
		Objects.requireNonNull(biome, "biome");
	}

	public static Entry of(Identifier biome, Parameters parameters) {
		return new Entry(parameters, biome);
	}
}