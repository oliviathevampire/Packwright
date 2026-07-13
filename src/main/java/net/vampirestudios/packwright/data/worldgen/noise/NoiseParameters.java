package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Parameters for an entry in the {@code worldgen/noise} registry.
 */
public record NoiseParameters(
		int firstOctave,
		List<Double> amplitudes
) {
	public static final Codec<NoiseParameters> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT
					.fieldOf("firstOctave")
					.forGetter(NoiseParameters::firstOctave),
			Codec.DOUBLE.listOf()
					.fieldOf("amplitudes")
					.forGetter(NoiseParameters::amplitudes)
	).apply(instance, NoiseParameters::new));

	public NoiseParameters {
		amplitudes = List.copyOf(amplitudes);

		if (amplitudes.isEmpty()) {
			throw new IllegalArgumentException("Noise amplitudes cannot be empty");
		}
	}

	public static NoiseParameters of(int firstOctave, double... amplitudes) {
		return new NoiseParameters(
				firstOctave,
				Arrays.stream(amplitudes).boxed().toList()
		);
	}
}