package net.vampirestudios.arrp.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record FancyFoliagePlacer(IntProvider radius, IntProvider offset, int height) implements TreeFoliagePlacer {
	public static final MapCodec<FancyFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffsetHeight(
			"minecraft:fancy_foliage_placer",
			FancyFoliagePlacer::new,
			FancyFoliagePlacer::radius,
			FancyFoliagePlacer::offset,
			FancyFoliagePlacer::height
	);
}
