package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record MangroveFoliagePlacer(IntProvider radius, IntProvider offset, int height) implements TreeFoliagePlacer {
	public static final MapCodec<MangroveFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffsetHeight(
			"minecraft:mangrove_foliage_placer",
			MangroveFoliagePlacer::new,
			MangroveFoliagePlacer::radius,
			MangroveFoliagePlacer::offset,
			MangroveFoliagePlacer::height
	);
}
