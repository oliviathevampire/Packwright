package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CocoaTreeDecorator(float probability) implements TreeDecorator {
	public static final MapCodec<CocoaTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:cocoa"),
			Codec.FLOAT.fieldOf("probability").forGetter(CocoaTreeDecorator::probability)
	).apply(i, (type, probability) -> new CocoaTreeDecorator(probability)));
}
