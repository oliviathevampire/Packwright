package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TrunkVineTreeDecorator() implements TreeDecorator {
	public static final MapCodec<TrunkVineTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:trunk_vine")
	).apply(i, type -> new TrunkVineTreeDecorator()));
}
