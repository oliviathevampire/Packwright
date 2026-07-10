package net.vampirestudios.arrp.data.worldgen.feature.foliage;

import com.mojang.serialization.MapCodec;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public record MegaJungleFoliagePlacer(IntProvider radius, IntProvider offset, int height) implements TreeFoliagePlacer {
	public static final MapCodec<MegaJungleFoliagePlacer> CODEC = TreeFoliagePlacerCodecs.radiusOffsetHeight(
			"minecraft:mega_jungle_foliage_placer",
			MegaJungleFoliagePlacer::new,
			MegaJungleFoliagePlacer::radius,
			MegaJungleFoliagePlacer::offset,
			MegaJungleFoliagePlacer::height
	);
}
