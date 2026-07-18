package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/** {@code minecraft:random_spread}: a spaced grid of chunks, offset by a random amount per cell */
public record RandomSpreadPlacement(
		StructurePlacement.LocateOffset locateOffset,
		String frequencyReductionMethod,
		float frequency,
		int salt,
		Optional<StructurePlacement.ExclusionZone> exclusionZone,
		int spacing,
		int separation,
		String spreadType
) implements StructurePlacement {
	public static final MapCodec<RandomSpreadPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:random_spread"),
			StructurePlacement.LocateOffset.CODEC.fieldOf("locate_offset").orElse(StructurePlacement.LocateOffset.ZERO)
					.forGetter(RandomSpreadPlacement::locateOffset),
			Codec.STRING.fieldOf("frequency_reduction_method").orElse("default")
					.forGetter(RandomSpreadPlacement::frequencyReductionMethod),
			Codec.FLOAT.fieldOf("frequency").orElse(1.0F)
					.forGetter(RandomSpreadPlacement::frequency),
			Codec.INT.fieldOf("salt").forGetter(RandomSpreadPlacement::salt),
			StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(RandomSpreadPlacement::exclusionZone),
			Codec.INT.fieldOf("spacing").forGetter(RandomSpreadPlacement::spacing),
			Codec.INT.fieldOf("separation").forGetter(RandomSpreadPlacement::separation),
			Codec.STRING.fieldOf("spread_type").orElse("linear")
					.forGetter(RandomSpreadPlacement::spreadType)
	).apply(i, (type, locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType) ->
			new RandomSpreadPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType)));

	public RandomSpreadPlacement withLocateOffset(int x, int y, int z) {
		return new RandomSpreadPlacement(new StructurePlacement.LocateOffset(x, y, z), frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
	}

	/** one of {@code "default"}, {@code "legacy_type_1"}, {@code "legacy_type_2"}, {@code "legacy_type_3"} */
	public RandomSpreadPlacement withFrequencyReductionMethod(String frequencyReductionMethod) {
		return new RandomSpreadPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
	}

	public RandomSpreadPlacement withFrequency(float frequency) {
		return new RandomSpreadPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
	}

	public RandomSpreadPlacement withExclusionZone(Identifier otherStructureSet, int chunkCount) {
		return new RandomSpreadPlacement(locateOffset, frequencyReductionMethod, frequency, salt,
				Optional.of(new StructurePlacement.ExclusionZone(otherStructureSet, chunkCount)), spacing, separation, spreadType);
	}

	/** one of {@code "linear"}, {@code "triangular"} */
	public RandomSpreadPlacement withSpreadType(String spreadType) {
		return new RandomSpreadPlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
	}
}
