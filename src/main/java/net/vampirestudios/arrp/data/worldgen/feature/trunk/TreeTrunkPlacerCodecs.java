package net.vampirestudios.arrp.data.worldgen.feature.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

final class TreeTrunkPlacerCodecs {
	private TreeTrunkPlacerCodecs() {
	}

	static <T extends TreeTrunkPlacer> MapCodec<T> baseHeight(
			String type,
			BaseHeightFactory<T> factory,
			IntGetter<T> baseHeight,
			IntGetter<T> heightRandA,
			IntGetter<T> heightRandB
	) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> type),
				Codec.INT.fieldOf("base_height").forGetter(baseHeight::get),
				Codec.INT.fieldOf("height_rand_a").forGetter(heightRandA::get),
				Codec.INT.fieldOf("height_rand_b").forGetter(heightRandB::get)
		).apply(i, (ignored, base, randA, randB) -> factory.create(base, randA, randB)));
	}

	@FunctionalInterface
	interface BaseHeightFactory<T> {
		T create(int baseHeight, int heightRandA, int heightRandB);
	}

	@FunctionalInterface
	interface IntGetter<T> {
		int get(T value);
	}
}
