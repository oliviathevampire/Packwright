package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record NumberDispatcherProvider(List<NumberDispatcherCase> cases, NumberProvider defaultProvider) implements NumberProvider {
	public static final MapCodec<NumberDispatcherProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:number_dispatcher"),
			NumberDispatcherCase.CODEC.listOf().fieldOf("cases").forGetter(NumberDispatcherProvider::cases),
			NumberProvider.CODEC.optionalFieldOf("default", NumberProvider.constant(0.0F)).forGetter(NumberDispatcherProvider::defaultProvider)
	).apply(i, (type, cases, defaultProvider) -> new NumberDispatcherProvider(cases, defaultProvider)));
}
