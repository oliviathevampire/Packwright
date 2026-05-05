package net.vampirestudios.arrp.json.equipmentinfo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record JDyeable(Optional<Integer> colorWhenUndyed) {
	public static final Codec<JDyeable> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("color_when_undyed").forGetter(JDyeable::colorWhenUndyed)
	).apply(i, JDyeable::new));
}
