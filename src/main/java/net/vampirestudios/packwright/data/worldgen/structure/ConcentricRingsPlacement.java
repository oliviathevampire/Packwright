package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.entity.IdOrTag;

import java.util.Optional;

/** {@code minecraft:concentric_rings}: rings of evenly-spaced structures around the origin */
public record ConcentricRingsPlacement(
		StructurePlacement.LocateOffset locateOffset,
		String frequencyReductionMethod,
		float frequency,
		int salt,
		Optional<StructurePlacement.ExclusionZone> exclusionZone,
		int distance,
		int spread,
		int count,
		IdOrTag preferredBiomes
) implements StructurePlacement {
	public static final MapCodec<ConcentricRingsPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:concentric_rings"),
			StructurePlacement.LocateOffset.CODEC.fieldOf("locate_offset").orElse(StructurePlacement.LocateOffset.ZERO)
					.forGetter(ConcentricRingsPlacement::locateOffset),
			Codec.STRING.fieldOf("frequency_reduction_method").orElse("default")
					.forGetter(ConcentricRingsPlacement::frequencyReductionMethod),
			Codec.FLOAT.fieldOf("frequency").orElse(1.0F)
					.forGetter(ConcentricRingsPlacement::frequency),
			Codec.INT.fieldOf("salt").forGetter(ConcentricRingsPlacement::salt),
			StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(ConcentricRingsPlacement::exclusionZone),
			Codec.INT.fieldOf("distance").forGetter(ConcentricRingsPlacement::distance),
			Codec.INT.fieldOf("spread").forGetter(ConcentricRingsPlacement::spread),
			Codec.INT.fieldOf("count").forGetter(ConcentricRingsPlacement::count),
			IdOrTag.CODEC.fieldOf("preferred_biomes").forGetter(ConcentricRingsPlacement::preferredBiomes)
	).apply(i, (type, locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, distance, spread, count, preferredBiomes) ->
			new ConcentricRingsPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, distance, spread, count, preferredBiomes)));

	public ConcentricRingsPlacement withLocateOffset(int x, int y, int z) {
		return new ConcentricRingsPlacement(new StructurePlacement.LocateOffset(x, y, z), frequencyReductionMethod, frequency, salt, exclusionZone, distance, spread, count, preferredBiomes);
	}

	/** one of {@code "default"}, {@code "legacy_type_1"}, {@code "legacy_type_2"}, {@code "legacy_type_3"} */
	public ConcentricRingsPlacement withFrequencyReductionMethod(String frequencyReductionMethod) {
		return new ConcentricRingsPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, distance, spread, count, preferredBiomes);
	}

	public ConcentricRingsPlacement withFrequency(float frequency) {
		return new ConcentricRingsPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, distance, spread, count, preferredBiomes);
	}

	public ConcentricRingsPlacement withExclusionZone(Identifier otherStructureSet, int chunkCount) {
		return new ConcentricRingsPlacement(locateOffset, frequencyReductionMethod, frequency, salt,
				Optional.of(new StructurePlacement.ExclusionZone(otherStructureSet, chunkCount)), distance, spread, count, preferredBiomes);
	}
}
