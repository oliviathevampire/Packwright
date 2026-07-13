package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.entity.IdOrTag;

import java.util.Optional;

/** {@code minecraft:concentric_rings}: rings of evenly-spaced structures around the origin */
public record ConcentricRingsPlacement(
		int distance,
		int spread,
		int count,
		Optional<IdOrTag> preferredBiomes
) implements StructurePlacement {
	public static final MapCodec<ConcentricRingsPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:concentric_rings"),
			Codec.INT.fieldOf("distance").forGetter(ConcentricRingsPlacement::distance),
			Codec.INT.fieldOf("spread").forGetter(ConcentricRingsPlacement::spread),
			Codec.INT.fieldOf("count").forGetter(ConcentricRingsPlacement::count),
			IdOrTag.CODEC.optionalFieldOf("preferred_biomes").forGetter(ConcentricRingsPlacement::preferredBiomes)
	).apply(i, (type, distance, spread, count, preferredBiomes) -> new ConcentricRingsPlacement(distance, spread, count, preferredBiomes)));
}
