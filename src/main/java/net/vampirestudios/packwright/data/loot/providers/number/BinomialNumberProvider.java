package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BinomialNumberProvider(NumberProvider n, NumberProvider p) implements NumberProvider {
	public static final MapCodec<BinomialNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:binomial"),
			NumberProvider.CODEC.fieldOf("n").forGetter(BinomialNumberProvider::n),
			NumberProvider.CODEC.fieldOf("p").forGetter(BinomialNumberProvider::p)
	).apply(i, (type, n, p) -> new BinomialNumberProvider(n, p)));
}
