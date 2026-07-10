package net.vampirestudios.packwright.data.worldgen.feature.builders.geode;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GeodeCrack(double generateCrackChance, double baseCrackSize, int crackPointOffset) {
	public static final Codec<GeodeCrack> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.DOUBLE.fieldOf("generate_crack_chance").forGetter(GeodeCrack::generateCrackChance),
			Codec.DOUBLE.fieldOf("base_crack_size").forGetter(GeodeCrack::baseCrackSize),
			Codec.INT.fieldOf("crack_point_offset").forGetter(GeodeCrack::crackPointOffset)
	).apply(i, GeodeCrack::new));
}
