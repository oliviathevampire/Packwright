package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class RandomSelectorConfig implements FeatureConfig {
	public static final Codec<RandomSelectorConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			WeightedPlacedFeature.CODEC.listOf().fieldOf("features").forGetter(x -> x.features),
			Codec.STRING.fieldOf("default").forGetter(x -> x.defaultFeature)
	).apply(i, (features, defaultFeature) -> new RandomSelectorConfig().features(features).defaultFeature(defaultFeature)));

	private List<WeightedPlacedFeature> features = new ArrayList<>();
	private String defaultFeature;

	public RandomSelectorConfig feature(String feature, float chance) {
		this.features.add(new WeightedPlacedFeature(feature, chance));
		return this;
	}

	public RandomSelectorConfig features(List<WeightedPlacedFeature> features) {
		this.features = new ArrayList<>(features);
		return this;
	}

	public RandomSelectorConfig defaultFeature(String defaultFeature) {
		this.defaultFeature = defaultFeature;
		return this;
	}
}
