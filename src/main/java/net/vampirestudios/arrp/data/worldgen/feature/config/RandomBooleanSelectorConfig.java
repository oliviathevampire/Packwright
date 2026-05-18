package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class RandomBooleanSelectorConfig implements FeatureConfig {
	public static final Codec<RandomBooleanSelectorConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("feature_true").forGetter(x -> x.featureTrue),
			Identifier.CODEC.fieldOf("feature_false").forGetter(x -> x.featureFalse)
	).apply(i, (featureTrue, featureFalse) -> new RandomBooleanSelectorConfig().featureTrue(featureTrue).featureFalse(featureFalse)));

	private Identifier featureTrue;
	private Identifier featureFalse;

	public RandomBooleanSelectorConfig featureTrue(Identifier featureTrue) {
		this.featureTrue = featureTrue;
		return this;
	}

	public RandomBooleanSelectorConfig featureFalse(Identifier featureFalse) {
		this.featureFalse = featureFalse;
		return this;
	}
}
