package net.vampirestudios.arrp.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

final class TreeFoliagePlacerCodecs {
	private TreeFoliagePlacerCodecs() {
	}

	static <T extends TreeFoliagePlacer> MapCodec<T> radiusOffsetHeight(
			String type,
			RadiusOffsetHeightFactory<T> factory,
			ProviderGetter<T> radius,
			ProviderGetter<T> offset,
			IntGetter<T> height
	) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> type),
				IntProvider.CODEC.fieldOf("radius").forGetter(radius::get),
				IntProvider.CODEC.fieldOf("offset").forGetter(offset::get),
				Codec.INT.fieldOf("height").forGetter(height::get)
		).apply(i, (ignored, radiusValue, offsetValue, heightValue) -> factory.create(radiusValue, offsetValue, heightValue)));
	}

	static <T extends TreeFoliagePlacer> MapCodec<T> radiusOffset(
			String type,
			RadiusOffsetFactory<T> factory,
			ProviderGetter<T> radius,
			ProviderGetter<T> offset
	) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> type),
				IntProvider.CODEC.fieldOf("radius").forGetter(radius::get),
				IntProvider.CODEC.fieldOf("offset").forGetter(offset::get)
		).apply(i, (ignored, radiusValue, offsetValue) -> factory.create(radiusValue, offsetValue)));
	}

	@FunctionalInterface
	interface RadiusOffsetHeightFactory<T> {
		T create(IntProvider radius, IntProvider offset, int height);
	}

	@FunctionalInterface
	interface RadiusOffsetFactory<T> {
		T create(IntProvider radius, IntProvider offset);
	}

	@FunctionalInterface
	interface ProviderGetter<T> {
		IntProvider get(T value);
	}

	@FunctionalInterface
	interface IntGetter<T> {
		int get(T value);
	}
}
