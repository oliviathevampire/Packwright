package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record WeightedListNumberProvider(List<WeightedNumberProviderEntry> distribution) implements NumberProvider {
	public static final MapCodec<WeightedListNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:weighted_list"),
			WeightedNumberProviderEntry.CODEC.listOf().fieldOf("distribution").forGetter(WeightedListNumberProvider::distribution)
	).apply(i, (type, distribution) -> new WeightedListNumberProvider(distribution)));
}
