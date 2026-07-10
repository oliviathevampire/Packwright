package net.vampirestudios.arrp.data.worldgen.feature.tree.size;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record ThreeLayersFeatureSize(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, Optional<Integer> minClippedHeight) implements TreeFeatureSize {
	public static final MapCodec<ThreeLayersFeatureSize> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:three_layers_feature_size"),
			Codec.INT.fieldOf("limit").forGetter(ThreeLayersFeatureSize::limit),
			Codec.INT.fieldOf("upper_limit").forGetter(ThreeLayersFeatureSize::upperLimit),
			Codec.INT.fieldOf("lower_size").forGetter(ThreeLayersFeatureSize::lowerSize),
			Codec.INT.fieldOf("middle_size").forGetter(ThreeLayersFeatureSize::middleSize),
			Codec.INT.fieldOf("upper_size").forGetter(ThreeLayersFeatureSize::upperSize),
			Codec.INT.optionalFieldOf("min_clipped_height").forGetter(ThreeLayersFeatureSize::minClippedHeight)
	).apply(i, (type, limit, upperLimit, lowerSize, middleSize, upperSize, minClippedHeight) ->
			new ThreeLayersFeatureSize(limit, upperLimit, lowerSize, middleSize, upperSize, minClippedHeight)));

	public ThreeLayersFeatureSize minClippedHeight(int value) {
		return new ThreeLayersFeatureSize(limit, upperLimit, lowerSize, middleSize, upperSize, Optional.of(value));
	}
}
