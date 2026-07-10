package net.vampirestudios.arrp.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record AcaciaFoliagePlacer(IntProvider radius, IntProvider offset) implements TreeFoliagePlacer {
	public static final MapCodec<AcaciaFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffset(
			"minecraft:acacia_foliage_placer",
			AcaciaFoliagePlacer::new,
			AcaciaFoliagePlacer::radius,
			AcaciaFoliagePlacer::offset
	);
}
