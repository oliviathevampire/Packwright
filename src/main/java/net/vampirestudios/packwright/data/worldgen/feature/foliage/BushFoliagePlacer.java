package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record BushFoliagePlacer(IntProvider radius, IntProvider offset, int height) implements TreeFoliagePlacer {
	public static final MapCodec<BushFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffsetHeight(
			"minecraft:bush_foliage_placer",
			BushFoliagePlacer::new,
			BushFoliagePlacer::radius,
			BushFoliagePlacer::offset,
			BushFoliagePlacer::height
	);
}
