package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record WeightedPlacedFeature(Identifier feature, float chance) {
	public static final Codec<WeightedPlacedFeature> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("feature").forGetter(WeightedPlacedFeature::feature),
			Codec.FLOAT.fieldOf("chance").forGetter(WeightedPlacedFeature::chance)
	).apply(i, WeightedPlacedFeature::new));
}
