package net.vampirestudios.arrp.data.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeightedPlacedFeature(PlacedFeature feature, float chance) {
	public static final Codec<WeightedPlacedFeature> CODEC = RecordCodecBuilder.create(i -> i.group(
			PlacedFeature.CODEC.fieldOf("feature").forGetter(WeightedPlacedFeature::feature),
			Codec.FLOAT.fieldOf("chance").forGetter(WeightedPlacedFeature::chance)
	).apply(i, WeightedPlacedFeature::new));
}
