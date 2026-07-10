package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LeaveVineTreeDecorator(float probability) implements TreeDecorator {
	public static final MapCodec<LeaveVineTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:leave_vine"),
			Codec.FLOAT.fieldOf("probability").forGetter(LeaveVineTreeDecorator::probability)
	).apply(i, (type, probability) -> new LeaveVineTreeDecorator(probability)));
}
