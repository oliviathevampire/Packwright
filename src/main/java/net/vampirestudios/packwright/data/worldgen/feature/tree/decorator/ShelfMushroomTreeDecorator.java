package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ShelfMushroomTreeDecorator(float probability) implements TreeDecorator {
	public static final MapCodec<ShelfMushroomTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:shelf_mushroom"),
			Codec.FLOAT.fieldOf("probability").forGetter(ShelfMushroomTreeDecorator::probability)
	).apply(i, (type, probability) -> new ShelfMushroomTreeDecorator(probability)));
}
