package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ProbabilityConfig implements FeatureConfig {
	public static final Codec<ProbabilityConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.fieldOf("probability").forGetter(x -> x.probability)
	).apply(i, probability -> new ProbabilityConfig().probability(probability)));

	private float probability = 0.5F;

	public static ProbabilityConfig of(float probability) {
		return new ProbabilityConfig().probability(probability);
	}

	public ProbabilityConfig probability(float probability) {
		this.probability = probability;
		return this;
	}
}
