package net.vampirestudios.packwright.data.worldgen.feature.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record SpruceFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) implements TreeFoliagePlacer {
	public static final MapCodec<SpruceFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:spruce_foliage_placer"),
			IntProvider.CODEC.fieldOf("radius").forGetter(SpruceFoliagePlacer::radius),
			IntProvider.CODEC.fieldOf("offset").forGetter(SpruceFoliagePlacer::offset),
			IntProvider.CODEC.fieldOf("trunk_height").forGetter(SpruceFoliagePlacer::trunkHeight)
	).apply(i, (type, radius, offset, trunkHeight) -> new SpruceFoliagePlacer(radius, offset, trunkHeight)));
}
