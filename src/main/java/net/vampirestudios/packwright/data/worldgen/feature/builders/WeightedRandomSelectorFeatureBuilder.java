package net.vampirestudios.packwright.data.worldgen.feature.builders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

/** {@code minecraft:weighted_random_selector} in vanilla; distinct from the deprecated {@code random_selector}'s {@code {feature, chance}} shape */
public final class WeightedRandomSelectorFeatureBuilder extends FeatureBuilder {
	private final List<WeightedFeature> features = new ArrayList<>();

	public WeightedRandomSelectorFeatureBuilder() {
		super("minecraft:weighted_random_selector");
	}

	public WeightedRandomSelectorFeatureBuilder feature(PlacedFeature feature, int weight) {
		features.add(new WeightedFeature(feature, weight));
		this.feature.property("features", features, WeightedFeature.CODEC);
		return this;
	}

	public record WeightedFeature(PlacedFeature data, int weight) {
		public static final Codec<WeightedFeature> CODEC = RecordCodecBuilder.create(i -> i.group(
				PlacedFeature.CODEC.fieldOf("data").forGetter(WeightedFeature::data),
				Codec.INT.fieldOf("weight").forGetter(WeightedFeature::weight)
		).apply(i, WeightedFeature::new));
	}
}
