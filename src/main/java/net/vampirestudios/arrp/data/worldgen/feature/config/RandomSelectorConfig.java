package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RandomSelectorConfig implements FeatureConfig {
	public static final Codec<RandomSelectorConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			WeightedPlacedFeature.CODEC.listOf().fieldOf("features").forGetter(x -> x.features),
			Identifier.CODEC.fieldOf("default").forGetter(x -> x.defaultFeature)
	).apply(i, (features, defaultFeature) -> new RandomSelectorConfig().features(features).defaultFeature(defaultFeature)));

	private List<WeightedPlacedFeature> features = new ArrayList<>();
	private Identifier defaultFeature;

	public RandomSelectorConfig feature(Identifier feature, float chance) {
		this.features.add(new WeightedPlacedFeature(feature, chance));
		return this;
	}

	public RandomSelectorConfig features(List<WeightedPlacedFeature> features) {
		this.features = new ArrayList<>(features);
		return this;
	}

	public RandomSelectorConfig defaultFeature(Identifier defaultFeature) {
		this.defaultFeature = defaultFeature;
		return this;
	}
}
