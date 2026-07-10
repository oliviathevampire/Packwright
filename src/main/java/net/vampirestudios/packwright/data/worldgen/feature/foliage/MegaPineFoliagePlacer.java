package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record MegaPineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider crownHeight) implements TreeFoliagePlacer {
	public static final MapCodec<MegaPineFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:mega_pine_foliage_placer"),
			IntProvider.CODEC.fieldOf("radius").forGetter(MegaPineFoliagePlacer::radius),
			IntProvider.CODEC.fieldOf("offset").forGetter(MegaPineFoliagePlacer::offset),
			IntProvider.CODEC.fieldOf("crown_height").forGetter(MegaPineFoliagePlacer::crownHeight)
	).apply(i, (type, radius, offset, crownHeight) -> new MegaPineFoliagePlacer(radius, offset, crownHeight)));
}
