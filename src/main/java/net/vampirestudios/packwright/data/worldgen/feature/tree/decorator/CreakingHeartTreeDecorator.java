package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CreakingHeartTreeDecorator(float probability) implements TreeDecorator {
	public static final MapCodec<CreakingHeartTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:creaking_heart"),
			Codec.FLOAT.fieldOf("probability").forGetter(CreakingHeartTreeDecorator::probability)
	).apply(i, (type, probability) -> new CreakingHeartTreeDecorator(probability)));
}
