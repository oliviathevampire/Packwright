package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record CherryFoliagePlacer(
		IntProvider radius,
		IntProvider offset,
		IntProvider height,
		float wideBottomLayerHoleChance,
		float cornerHoleChance,
		float hangingLeavesChance,
		float hangingLeavesExtensionChance
) implements TreeFoliagePlacer {
	public static final MapCodec<CherryFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:cherry_foliage_placer"),
			IntProvider.CODEC.fieldOf("radius").forGetter(CherryFoliagePlacer::radius),
			IntProvider.CODEC.fieldOf("offset").forGetter(CherryFoliagePlacer::offset),
			IntProvider.CODEC.fieldOf("height").forGetter(CherryFoliagePlacer::height),
			Codec.FLOAT.fieldOf("wide_bottom_layer_hole_chance").forGetter(CherryFoliagePlacer::wideBottomLayerHoleChance),
			Codec.FLOAT.fieldOf("corner_hole_chance").forGetter(CherryFoliagePlacer::cornerHoleChance),
			Codec.FLOAT.fieldOf("hanging_leaves_chance").forGetter(CherryFoliagePlacer::hangingLeavesChance),
			Codec.FLOAT.fieldOf("hanging_leaves_extension_chance").forGetter(CherryFoliagePlacer::hangingLeavesExtensionChance)
	).apply(i, (type, radius, offset, height, wideBottomLayerHoleChance, cornerHoleChance, hangingLeavesChance, hangingLeavesExtensionChance) ->
			new CherryFoliagePlacer(radius, offset, height, wideBottomLayerHoleChance, cornerHoleChance, hangingLeavesChance, hangingLeavesExtensionChance)));
}
