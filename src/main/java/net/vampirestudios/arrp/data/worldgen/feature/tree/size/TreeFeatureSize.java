package net.vampirestudios.arrp.data.worldgen.feature.tree.size;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;

import java.util.Optional;

public sealed interface TreeFeatureSize permits TwoLayersFeatureSize, ThreeLayersFeatureSize {
	Codec<TreeFeatureSize> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<TreeFeatureSize, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "two_layers_feature_size" -> TwoLayersFeatureSize.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "three_layers_feature_size" -> ThreeLayersFeatureSize.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported tree feature size type");
			});
		}

		@Override
		public <T> DataResult<T> encode(TreeFeatureSize input, DynamicOps<T> ops, T prefix) {
			if (input instanceof TwoLayersFeatureSize size) return TwoLayersFeatureSize.CODEC.codec().encode(size, ops, prefix);
			if (input instanceof ThreeLayersFeatureSize size) return ThreeLayersFeatureSize.CODEC.codec().encode(size, ops, prefix);
			return DataResult.error(() -> "Unsupported tree feature size: " + input.getClass().getSimpleName());
		}
	};

	static TwoLayersFeatureSize twoLayers(int limit, int lowerSize, int upperSize) {
		return twoLayers(limit, lowerSize, upperSize, Optional.empty());
	}

	static TwoLayersFeatureSize twoLayers(int limit, int lowerSize, int upperSize, Optional<Integer> minClippedHeight) {
		return new TwoLayersFeatureSize(limit, lowerSize, upperSize, minClippedHeight);
	}

	static ThreeLayersFeatureSize threeLayers(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize) {
		return threeLayers(limit, upperLimit, lowerSize, middleSize, upperSize, Optional.empty());
	}

	static ThreeLayersFeatureSize threeLayers(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, Optional<Integer> minClippedHeight) {
		return new ThreeLayersFeatureSize(limit, upperLimit, lowerSize, middleSize, upperSize, minClippedHeight);
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
