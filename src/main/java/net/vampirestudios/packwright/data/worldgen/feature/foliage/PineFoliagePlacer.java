package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record PineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) implements TreeFoliagePlacer {
	public static final MapCodec<PineFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:pine_foliage_placer"),
			IntProvider.CODEC.fieldOf("radius").forGetter(PineFoliagePlacer::radius),
			IntProvider.CODEC.fieldOf("offset").forGetter(PineFoliagePlacer::offset),
			IntProvider.CODEC.fieldOf("height").forGetter(PineFoliagePlacer::height)
	).apply(i, (type, radius, offset, height) -> new PineFoliagePlacer(radius, offset, height)));
}
