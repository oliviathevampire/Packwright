package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class RandomBooleanSelectorConfig implements FeatureConfig {
	public static final Codec<RandomBooleanSelectorConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("feature_true").forGetter(x -> x.featureTrue),
			Codec.STRING.fieldOf("feature_false").forGetter(x -> x.featureFalse)
	).apply(i, (featureTrue, featureFalse) -> new RandomBooleanSelectorConfig().featureTrue(featureTrue).featureFalse(featureFalse)));

	private String featureTrue;
	private String featureFalse;

	public RandomBooleanSelectorConfig featureTrue(String featureTrue) {
		this.featureTrue = featureTrue;
		return this;
	}

	public RandomBooleanSelectorConfig featureFalse(String featureFalse) {
		this.featureFalse = featureFalse;
		return this;
	}
}
