package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record PoplarFoliagePlacer(
		IntProvider radius,
		IntProvider offset,
		IntProvider height,
		float sideHoleChance
) implements TreeFoliagePlacer {
	public static final MapCodec<PoplarFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:poplar_foliage_placer"),
			IntProvider.CODEC.fieldOf("radius").forGetter(PoplarFoliagePlacer::radius),
			IntProvider.CODEC.fieldOf("offset").forGetter(PoplarFoliagePlacer::offset),
			IntProvider.CODEC.fieldOf("height").forGetter(PoplarFoliagePlacer::height),
			Codec.FLOAT.fieldOf("side_hole_chance").forGetter(PoplarFoliagePlacer::sideHoleChance)
	).apply(i, (type, radius, offset, height, sideHoleChance) -> new PoplarFoliagePlacer(radius, offset, height, sideHoleChance)));
}
