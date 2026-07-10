package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record BlobFoliagePlacer(IntProvider radius, IntProvider offset, int height) implements TreeFoliagePlacer {
	public static final MapCodec<BlobFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffsetHeight(
			"minecraft:blob_foliage_placer",
			BlobFoliagePlacer::new,
			BlobFoliagePlacer::radius,
			BlobFoliagePlacer::offset,
			BlobFoliagePlacer::height
	);
}
