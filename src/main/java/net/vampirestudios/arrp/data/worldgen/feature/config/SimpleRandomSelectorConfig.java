package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SimpleRandomSelectorConfig implements FeatureConfig {
	public static final Codec<SimpleRandomSelectorConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.listOf().fieldOf("features").forGetter(x -> x.features)
	).apply(i, features -> new SimpleRandomSelectorConfig().features(features)));

	private List<Identifier> features = new ArrayList<>();

	public SimpleRandomSelectorConfig feature(Identifier feature) {
		this.features.add(feature);
		return this;
	}

	public SimpleRandomSelectorConfig features(List<Identifier> features) {
		this.features = new ArrayList<>(features);
		return this;
	}
}
