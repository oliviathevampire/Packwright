package net.vampirestudios.arrp.assets.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record Dyeable(Optional<Integer> colorWhenUndyed) {
	public static final Codec<Dyeable> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("color_when_undyed").forGetter(Dyeable::colorWhenUndyed)
	).apply(i, Dyeable::new));
}
