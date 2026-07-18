package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record SumNumberProvider(List<NumberProvider> summands) implements NumberProvider {
	public static final MapCodec<SumNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:sum"),
			NumberProvider.CODEC.listOf().fieldOf("summands").forGetter(SumNumberProvider::summands)
	).apply(i, (type, summands) -> new SumNumberProvider(summands)));
}
