package net.vampirestudios.packwright.data.worldgen.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

/**
 * The climate parameters used to select a biome.
 */
public record Parameters(Parameter temperature, Parameter humidity, Parameter continentalness, Parameter erosion,
				  Parameter depth, Parameter weirdness, float offset) {
	public static final Codec<Parameters> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Parameter.CODEC.fieldOf("temperature").forGetter(Parameters::temperature),
			Parameter.CODEC.fieldOf("humidity").forGetter(Parameters::humidity),
			Parameter.CODEC.fieldOf("continentalness").forGetter(Parameters::continentalness),
			Parameter.CODEC.fieldOf("erosion").forGetter(Parameters::erosion),
			Parameter.CODEC.fieldOf("depth").forGetter(Parameters::depth),
			Parameter.CODEC.fieldOf("weirdness").forGetter(Parameters::weirdness),
			Codec.FLOAT.fieldOf("offset").forGetter(Parameters::offset)
	).apply(instance, Parameters::new));

	public Parameters {
		Objects.requireNonNull(temperature, "temperature");
		Objects.requireNonNull(humidity, "humidity");
		Objects.requireNonNull(continentalness, "continentalness");
		Objects.requireNonNull(erosion, "erosion");
		Objects.requireNonNull(depth, "depth");
		Objects.requireNonNull(weirdness, "weirdness");
	}

	public static Parameters of(Parameter temperature, Parameter humidity, Parameter continentalness, Parameter erosion,
								Parameter depth, Parameter weirdness) {
		return new Parameters(temperature, humidity, continentalness, erosion, depth, weirdness, 0.0F);
	}

	public static Parameters full() {
		return of(Parameter.full(), Parameter.full(), Parameter.full(), Parameter.full(), Parameter.full(), Parameter.full());
	}
}