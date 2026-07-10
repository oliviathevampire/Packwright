package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record DarkOakFoliagePlacer(IntProvider radius, IntProvider offset) implements TreeFoliagePlacer {
	public static final MapCodec<DarkOakFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffset(
			"minecraft:dark_oak_foliage_placer",
			DarkOakFoliagePlacer::new,
			DarkOakFoliagePlacer::radius,
			DarkOakFoliagePlacer::offset
	);
}
