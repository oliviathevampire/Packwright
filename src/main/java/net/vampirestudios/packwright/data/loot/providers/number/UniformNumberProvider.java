package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record UniformNumberProvider(NumberProvider min, NumberProvider max) implements NumberProvider {
	public static final MapCodec<UniformNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:uniform"),
			NumberProvider.CODEC.fieldOf("min").forGetter(UniformNumberProvider::min),
			NumberProvider.CODEC.fieldOf("max").forGetter(UniformNumberProvider::max)
	).apply(i, (type, min, max) -> new UniformNumberProvider(min, max)));
}
