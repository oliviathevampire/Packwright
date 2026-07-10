package net.vampirestudios.arrp.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record RandomSpreadFoliagePlacer(IntProvider radius, IntProvider offset, int foliageHeight, int leafPlacementAttempts) implements TreeFoliagePlacer {
	public static final MapCodec<RandomSpreadFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:random_spread_foliage_placer"),
			IntProvider.CODEC.fieldOf("radius").forGetter(RandomSpreadFoliagePlacer::radius),
			IntProvider.CODEC.fieldOf("offset").forGetter(RandomSpreadFoliagePlacer::offset),
			Codec.INT.fieldOf("foliage_height").forGetter(RandomSpreadFoliagePlacer::foliageHeight),
			Codec.INT.fieldOf("leaf_placement_attempts").forGetter(RandomSpreadFoliagePlacer::leafPlacementAttempts)
	).apply(i, (type, radius, offset, foliageHeight, leafPlacementAttempts) ->
			new RandomSpreadFoliagePlacer(radius, offset, foliageHeight, leafPlacementAttempts)));
}
