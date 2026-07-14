package net.vampirestudios.packwright.data.worldgen.feature.tree.size;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record TwoLayersFeatureSize(int limit, int lowerSize, int upperSize, Optional<Integer> minClippedHeight) implements TreeFeatureSize {
	public static final MapCodec<TwoLayersFeatureSize> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:two_layers_feature_size"),
			Codec.INT.fieldOf("limit").orElse(1).forGetter(TwoLayersFeatureSize::limit),
			Codec.INT.fieldOf("lower_size").orElse(0).forGetter(TwoLayersFeatureSize::lowerSize),
			Codec.INT.fieldOf("upper_size").orElse(1).forGetter(TwoLayersFeatureSize::upperSize),
			Codec.INT.optionalFieldOf("min_clipped_height").forGetter(TwoLayersFeatureSize::minClippedHeight)
	).apply(i, (type, limit, lowerSize, upperSize, minClippedHeight) ->
			new TwoLayersFeatureSize(limit, lowerSize, upperSize, minClippedHeight)));

	public TwoLayersFeatureSize minClippedHeight(int value) {
		return new TwoLayersFeatureSize(limit, lowerSize, upperSize, Optional.of(value));
	}
}
