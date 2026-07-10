package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BeehiveTreeDecorator(float probability) implements TreeDecorator {
	public static final MapCodec<BeehiveTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:beehive"),
			Codec.FLOAT.fieldOf("probability").forGetter(BeehiveTreeDecorator::probability)
	).apply(i, (type, probability) -> new BeehiveTreeDecorator(probability)));
}
