package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** the {@code aquifers} block of a {@code worldgen/noise_settings} file: the density functions that shape aquifer placement */
public record AquiferSettings(
		DensityFunction barrier,
		DensityFunction fluidLevelFloodedness,
		DensityFunction fluidLevelSpread,
		DensityFunction lava,
		DensityFunction exclusion,
		DensityFunction surfaceLevel
) {
	public static final Codec<AquiferSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
			DensityFunction.CODEC.fieldOf("barrier").forGetter(AquiferSettings::barrier),
			DensityFunction.CODEC.fieldOf("fluid_level_floodedness").forGetter(AquiferSettings::fluidLevelFloodedness),
			DensityFunction.CODEC.fieldOf("fluid_level_spread").forGetter(AquiferSettings::fluidLevelSpread),
			DensityFunction.CODEC.fieldOf("lava").forGetter(AquiferSettings::lava),
			DensityFunction.CODEC.fieldOf("exclusion").forGetter(AquiferSettings::exclusion),
			DensityFunction.CODEC.fieldOf("surface_level").forGetter(AquiferSettings::surfaceLevel)
	).apply(i, AquiferSettings::new));
}
