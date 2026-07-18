package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeightedNumberProviderEntry(NumberProvider data, int weight) {
	public static final Codec<WeightedNumberProviderEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
			NumberProvider.CODEC.fieldOf("data").forGetter(WeightedNumberProviderEntry::data),
			Codec.INT.fieldOf("weight").forGetter(WeightedNumberProviderEntry::weight)
	).apply(i, WeightedNumberProviderEntry::new));

	public WeightedNumberProviderEntry {
		if (weight <= 0) {
			throw new IllegalArgumentException("weight must be positive: " + weight);
		}
	}
}
