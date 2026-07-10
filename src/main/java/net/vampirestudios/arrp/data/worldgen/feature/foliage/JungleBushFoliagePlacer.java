package net.vampirestudios.arrp.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record JungleBushFoliagePlacer(IntProvider radius, IntProvider offset, int height) implements TreeFoliagePlacer {
	public static final MapCodec<JungleBushFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffsetHeight(
			"minecraft:jungle_bush_foliage_placer",
			JungleBushFoliagePlacer::new,
			JungleBushFoliagePlacer::radius,
			JungleBushFoliagePlacer::offset,
			JungleBushFoliagePlacer::height
	);
}
