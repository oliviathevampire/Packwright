package net.vampirestudios.arrp.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PaleMossTreeDecorator(float groundChance, float leavesChance, float trunkChance) implements TreeDecorator {
	public static final MapCodec<PaleMossTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:pale_moss"),
			Codec.FLOAT.fieldOf("ground_chance").forGetter(PaleMossTreeDecorator::groundChance),
			Codec.FLOAT.fieldOf("leaves_chance").forGetter(PaleMossTreeDecorator::leavesChance),
			Codec.FLOAT.fieldOf("trunk_chance").forGetter(PaleMossTreeDecorator::trunkChance)
	).apply(i, (type, groundChance, leavesChance, trunkChance) -> new PaleMossTreeDecorator(groundChance, leavesChance, trunkChance)));
}
