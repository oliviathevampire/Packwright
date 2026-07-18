package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * The {@code noise_router} of a noise settings file: sixteen density functions that
 * drive terrain shape, fluids, climate and ore veins. Every function defaults to a
 * constant {@code 0}; {@code final_density} is the one that shapes the terrain
 * (positive = solid, negative = air).
 */
public class NoiseRouter {
	// all eight functions are required by the game; fieldOf+orElse always encodes them
	public static final Codec<NoiseRouter> CODEC = RecordCodecBuilder.create(i -> i.group(
			field("temperature").forGetter(x -> x.temperature),
			field("vegetation").forGetter(x -> x.vegetation),
			field("continents").forGetter(x -> x.continents),
			field("erosion").forGetter(x -> x.erosion),
			field("depth").forGetter(x -> x.depth),
			field("ridges").forGetter(x -> x.ridges),
			field("preliminary_surface_level").forGetter(x -> x.preliminarySurfaceLevel),
			field("final_density").forGetter(x -> x.finalDensity)
	).apply(i, (temperature, vegetation, continents, erosion, depth, ridges, surfaceLevel, finalDensity) -> {
		NoiseRouter out = new NoiseRouter();
		out.temperature = temperature;
		out.vegetation = vegetation;
		out.continents = continents;
		out.erosion = erosion;
		out.depth = depth;
		out.ridges = ridges;
		out.preliminarySurfaceLevel = surfaceLevel;
		out.finalDensity = finalDensity;
		return out;
	}));

	private static MapCodec<DensityFunction> field(String name) {
		return DensityFunction.CODEC.fieldOf(name).orElse(DensityFunctions.zero());
	}

	private DensityFunction temperature = DensityFunctions.zero();
	private DensityFunction vegetation = DensityFunctions.zero();
	private DensityFunction continents = DensityFunctions.zero();
	private DensityFunction erosion = DensityFunctions.zero();
	private DensityFunction depth = DensityFunctions.zero();
	private DensityFunction ridges = DensityFunctions.zero();
	private DensityFunction preliminarySurfaceLevel = DensityFunctions.zero();
	private DensityFunction finalDensity = DensityFunctions.zero();

	public static NoiseRouter router() {
		return new NoiseRouter();
	}

	/** every function is {@code 0} except {@code final_density} */
	public static NoiseRouter simple(DensityFunction finalDensity) {
		return router().finalDensity(finalDensity);
	}

	public NoiseRouter temperature(DensityFunction v) { this.temperature = v; return this; }
	public NoiseRouter vegetation(DensityFunction v) { this.vegetation = v; return this; }
	public NoiseRouter continents(DensityFunction v) { this.continents = v; return this; }
	public NoiseRouter erosion(DensityFunction v) { this.erosion = v; return this; }
	public NoiseRouter depth(DensityFunction v) { this.depth = v; return this; }
	public NoiseRouter ridges(DensityFunction v) { this.ridges = v; return this; }
	public NoiseRouter preliminarySurfaceLevel(DensityFunction v) { this.preliminarySurfaceLevel = v; return this; }
	public NoiseRouter finalDensity(DensityFunction v) { this.finalDensity = v; return this; }
}
