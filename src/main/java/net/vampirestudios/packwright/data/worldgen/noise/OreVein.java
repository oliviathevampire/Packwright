package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

/** an entry of the {@code ore_veins} list of a {@code worldgen/noise_settings} file */
public record OreVein(
		WorldgenBlockState oreBlock,
		WorldgenBlockState rawOreBlock,
		WorldgenBlockState fillerBlock,
		float rawOreChance,
		DensityFunction density,
		DensityFunction richness,
		DensityFunction fillerGap
) {
	public static final Codec<OreVein> CODEC = RecordCodecBuilder.create(i -> i.group(
			WorldgenBlockState.CODEC.fieldOf("ore_block").forGetter(OreVein::oreBlock),
			WorldgenBlockState.CODEC.fieldOf("raw_ore_block").forGetter(OreVein::rawOreBlock),
			WorldgenBlockState.CODEC.fieldOf("filler_block").forGetter(OreVein::fillerBlock),
			Codec.floatRange(0.0F, 1.0F).fieldOf("raw_ore_chance").forGetter(OreVein::rawOreChance),
			DensityFunction.CODEC.fieldOf("density").forGetter(OreVein::density),
			DensityFunction.CODEC.fieldOf("richness").forGetter(OreVein::richness),
			DensityFunction.CODEC.fieldOf("filler_gap").forGetter(OreVein::fillerGap)
	).apply(i, OreVein::new));
}
