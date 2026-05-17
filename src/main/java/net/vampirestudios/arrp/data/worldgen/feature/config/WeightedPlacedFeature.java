package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeightedPlacedFeature(String feature, float chance) {
	public static final Codec<WeightedPlacedFeature> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("feature").forGetter(WeightedPlacedFeature::feature),
			Codec.FLOAT.fieldOf("chance").forGetter(WeightedPlacedFeature::chance)
	).apply(i, WeightedPlacedFeature::new));
}
