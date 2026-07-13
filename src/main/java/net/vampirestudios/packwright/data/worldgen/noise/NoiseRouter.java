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
	// all sixteen functions are required by the game; fieldOf+orElse always encodes them
	public static final Codec<NoiseRouter> CODEC = RecordCodecBuilder.create(i -> i.group(
			field("barrier").forGetter(x -> x.barrier),
			field("fluid_level_floodedness").forGetter(x -> x.fluidLevelFloodedness),
			field("fluid_level_spread").forGetter(x -> x.fluidLevelSpread),
			field("lava").forGetter(x -> x.lava),
			field("temperature").forGetter(x -> x.temperature),
			field("vegetation").forGetter(x -> x.vegetation),
			field("continents").forGetter(x -> x.continents),
			field("erosion").forGetter(x -> x.erosion),
			field("depth").forGetter(x -> x.depth),
			field("ridges").forGetter(x -> x.ridges),
			field("preliminary_surface_level").forGetter(x -> x.preliminarySurfaceLevel),
			field("initial_density_without_jaggedness").forGetter(x -> x.initialDensityWithoutJaggedness),
			field("final_density").forGetter(x -> x.finalDensity),
			field("vein_toggle").forGetter(x -> x.veinToggle),
			field("vein_ridged").forGetter(x -> x.veinRidged),
			field("vein_gap").forGetter(x -> x.veinGap)
	).apply(i, (barrier, floodedness, spread, lava, temperature, vegetation, continents, erosion, depth, ridges,
				surfaceLevel, initialDensity, finalDensity, veinToggle, veinRidged, veinGap) -> {
		NoiseRouter out = new NoiseRouter();
		out.barrier = barrier;
		out.fluidLevelFloodedness = floodedness;
		out.fluidLevelSpread = spread;
		out.lava = lava;
		out.temperature = temperature;
		out.vegetation = vegetation;
		out.continents = continents;
		out.erosion = erosion;
		out.depth = depth;
		out.ridges = ridges;
		out.preliminarySurfaceLevel = surfaceLevel;
		out.initialDensityWithoutJaggedness = initialDensity;
		out.finalDensity = finalDensity;
		out.veinToggle = veinToggle;
		out.veinRidged = veinRidged;
		out.veinGap = veinGap;
		return out;
	}));

	private static MapCodec<DensityFunction> field(String name) {
		return DensityFunction.CODEC.fieldOf(name).orElse(DensityFunctions.zero());
	}

	private DensityFunction barrier = DensityFunctions.zero();
	private DensityFunction fluidLevelFloodedness = DensityFunctions.zero();
	private DensityFunction fluidLevelSpread = DensityFunctions.zero();
	private DensityFunction lava = DensityFunctions.zero();
	private DensityFunction temperature = DensityFunctions.zero();
	private DensityFunction vegetation = DensityFunctions.zero();
	private DensityFunction continents = DensityFunctions.zero();
	private DensityFunction erosion = DensityFunctions.zero();
	private DensityFunction depth = DensityFunctions.zero();
	private DensityFunction ridges = DensityFunctions.zero();
	private DensityFunction preliminarySurfaceLevel = DensityFunctions.zero();
	private DensityFunction initialDensityWithoutJaggedness = DensityFunctions.zero();
	private DensityFunction finalDensity = DensityFunctions.zero();
	private DensityFunction veinToggle = DensityFunctions.zero();
	private DensityFunction veinRidged = DensityFunctions.zero();
	private DensityFunction veinGap = DensityFunctions.zero();

	public static NoiseRouter router() {
		return new NoiseRouter();
	}

	/** every function is {@code 0} except {@code final_density} */
	public static NoiseRouter simple(DensityFunction finalDensity) {
		return router().finalDensity(finalDensity);
	}

	public NoiseRouter barrier(DensityFunction v) { this.barrier = v; return this; }
	public NoiseRouter fluidLevelFloodedness(DensityFunction v) { this.fluidLevelFloodedness = v; return this; }
	public NoiseRouter fluidLevelSpread(DensityFunction v) { this.fluidLevelSpread = v; return this; }
	public NoiseRouter lava(DensityFunction v) { this.lava = v; return this; }
	public NoiseRouter temperature(DensityFunction v) { this.temperature = v; return this; }
	public NoiseRouter vegetation(DensityFunction v) { this.vegetation = v; return this; }
	public NoiseRouter continents(DensityFunction v) { this.continents = v; return this; }
	public NoiseRouter erosion(DensityFunction v) { this.erosion = v; return this; }
	public NoiseRouter depth(DensityFunction v) { this.depth = v; return this; }
	public NoiseRouter ridges(DensityFunction v) { this.ridges = v; return this; }
	public NoiseRouter preliminarySurfaceLevel(DensityFunction v) { this.preliminarySurfaceLevel = v; return this; }
	public NoiseRouter initialDensityWithoutJaggedness(DensityFunction v) { this.initialDensityWithoutJaggedness = v; return this; }
	public NoiseRouter finalDensity(DensityFunction v) { this.finalDensity = v; return this; }
	public NoiseRouter veinToggle(DensityFunction v) { this.veinToggle = v; return this; }
	public NoiseRouter veinRidged(DensityFunction v) { this.veinRidged = v; return this; }
	public NoiseRouter veinGap(DensityFunction v) { this.veinGap = v; return this; }
}
