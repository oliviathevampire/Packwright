package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class SimpleRandomSelectorConfig implements FeatureConfig {
	public static final Codec<SimpleRandomSelectorConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.listOf().fieldOf("features").forGetter(x -> x.features)
	).apply(i, features -> new SimpleRandomSelectorConfig().features(features)));

	private List<String> features = new ArrayList<>();

	public SimpleRandomSelectorConfig feature(String feature) {
		this.features.add(feature);
		return this;
	}

	public SimpleRandomSelectorConfig features(List<String> features) {
		this.features = new ArrayList<>(features);
		return this;
	}
}
