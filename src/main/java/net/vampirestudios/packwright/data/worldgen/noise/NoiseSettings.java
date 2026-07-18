package net.vampirestudios.packwright.data.worldgen.noise;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;
import net.vampirestudios.packwright.data.worldgen.dimension.Parameters;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A {@code worldgen/noise_settings} file, fully typed: blocks are
 * {@link WorldgenBlockState}s, the router is a {@link NoiseRouter} of
 * {@link DensityFunction}s, and spawn targets are climate {@link Parameters} points.
 */
public class NoiseSettings {

	public static final Codec<NoiseSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.optionalFieldOf("sea_level").forGetter(x -> Optional.ofNullable(x.seaLevel)),
			Codec.BOOL.optionalFieldOf("legacy_random_source").forGetter(x -> Optional.ofNullable(x.legacyRandomSource)),
			WorldgenBlockState.CODEC.optionalFieldOf("default_block").forGetter(x -> Optional.ofNullable(x.defaultBlock)),
			WorldgenBlockState.CODEC.optionalFieldOf("default_fluid").forGetter(x -> Optional.ofNullable(x.defaultFluid)),
			NoiseShape.CODEC.optionalFieldOf("noise").forGetter(x -> Optional.ofNullable(x.noise)),
			// spawn_target is required by the game; fieldOf+orElse always encodes it
			Parameters.CODEC.listOf().fieldOf("spawn_target").orElse(List.of()).forGetter(x -> List.copyOf(x.spawnTarget)),
			// a material rule is either a worldgen/material_rule registry id or an inline rule
			Codec.either(Identifier.CODEC, MaterialRule.CODEC).optionalFieldOf("material_rule").forGetter(NoiseSettings::materialRuleEither),
			AquiferSettings.CODEC.optionalFieldOf("aquifers").forGetter(x -> Optional.ofNullable(x.aquifers)),
			// ore_veins is required by the game; fieldOf+orElse always encodes it
			OreVein.CODEC.listOf().fieldOf("ore_veins").orElse(List.of()).forGetter(x -> List.copyOf(x.oreVeins)),
			// disable_mob_generation is required by the game; fieldOf+orElse always encodes it
			Codec.BOOL.fieldOf("disable_mob_generation").orElse(false).forGetter(x -> x.disableMobGeneration),
			NoiseRouter.CODEC.optionalFieldOf("noise_router").forGetter(x -> Optional.ofNullable(x.noiseRouter))
	).apply(i, (seaLevel, legacyRandomSource, defaultBlock, defaultFluid, noise, spawnTarget, materialRule,
				aquifers, oreVeins, disableMobGeneration, noiseRouter) -> {
		NoiseSettings out = new NoiseSettings();
		out.seaLevel = seaLevel.orElse(null);
		out.legacyRandomSource = legacyRandomSource.orElse(null);
		out.defaultBlock = defaultBlock.orElse(null);
		out.defaultFluid = defaultFluid.orElse(null);
		out.noise = noise.orElse(null);
		out.spawnTarget = new ArrayList<>(spawnTarget);
		materialRule.ifPresent(either -> either.ifLeft(id -> out.materialRuleId = id).ifRight(rule -> out.materialRule = rule));
		out.aquifers = aquifers.orElse(null);
		out.oreVeins = new ArrayList<>(oreVeins);
		out.disableMobGeneration = disableMobGeneration;
		out.noiseRouter = noiseRouter.orElse(null);
		return out;
	}));

	private Integer seaLevel;
	private Boolean legacyRandomSource;
	private WorldgenBlockState defaultBlock;
	private WorldgenBlockState defaultFluid;
	private NoiseShape noise;
	private List<Parameters> spawnTarget = new ArrayList<>();
	private Identifier materialRuleId;
	private MaterialRule materialRule;
	private AquiferSettings aquifers;
	private List<OreVein> oreVeins = new ArrayList<>();
	// required by the game; defaulted so a bare settings object still parses
	private boolean disableMobGeneration = false;
	private NoiseRouter noiseRouter;

	public static NoiseSettings settings() {
		return new NoiseSettings();
	}

	private Optional<Either<Identifier, MaterialRule>> materialRuleEither() {
		if (this.materialRuleId != null) return Optional.of(Either.left(this.materialRuleId));
		if (this.materialRule != null) return Optional.of(Either.right(this.materialRule));
		return Optional.empty();
	}

	// Core setters

	public NoiseSettings seaLevel(int v) { this.seaLevel = v; return this; }
	public NoiseSettings legacyRandomSource(boolean v) { this.legacyRandomSource = v; return this; }
	public NoiseSettings defaultBlock(WorldgenBlockState v) { this.defaultBlock = v; return this; }
	public NoiseSettings defaultFluid(WorldgenBlockState v) { this.defaultFluid = v; return this; }
	public NoiseSettings noise(NoiseShape v) { this.noise = v; return this; }
	public NoiseSettings spawnTarget(List<Parameters> v) { this.spawnTarget = v == null ? new ArrayList<>() : new ArrayList<>(v); return this; }
	public NoiseSettings addSpawnTarget(Parameters v) { this.spawnTarget.add(v); return this; }

	/** reference a {@code worldgen/material_rule} registry entry by id */
	public NoiseSettings materialRule(Identifier id) {
		this.materialRuleId = id;
		this.materialRule = null;
		return this;
	}

	/** an inline material rule */
	public NoiseSettings materialRule(MaterialRule rule) {
		this.materialRule = rule;
		this.materialRuleId = null;
		return this;
	}

	public NoiseSettings aquifers(AquiferSettings v) { this.aquifers = v; return this; }
	public NoiseSettings oreVeins(List<OreVein> v) { this.oreVeins = v == null ? new ArrayList<>() : new ArrayList<>(v); return this; }
	public NoiseSettings addOreVein(OreVein v) { this.oreVeins.add(v); return this; }
	public NoiseSettings disableMobGeneration(boolean v) { this.disableMobGeneration = v; return this; }
	public NoiseSettings noiseRouter(NoiseRouter v) { this.noiseRouter = v; return this; }

	/**
	 * a minimal noise router: every routing density function is {@code 0} except
	 * {@code final_density}, which shapes the terrain (positive = solid, negative = air)
	 */
	public NoiseSettings simpleNoiseRouter(DensityFunction finalDensity) {
		return noiseRouter(NoiseRouter.simple(finalDensity));
	}

	/**
	 * a router whose {@code final_density} is a simple y gradient: solid below
	 * {@code fromY}, air above {@code toY}, producing flat terrain in between
	 */
	public NoiseSettings simpleNoiseRouterGradient(int fromY, int toY) {
		return simpleNoiseRouter(DensityFunctions.yClampedGradient(fromY, toY, 1.0, -1.0));
	}

	// Convenience helpers

	public NoiseSettings defaultBlockId(Identifier id) {
		this.defaultBlock = id == null ? null : WorldgenBlockState.blockState(id);
		return this;
	}

	public NoiseSettings defaultFluidId(Identifier id) {
		this.defaultFluid = id == null ? null : WorldgenBlockState.blockState(id);
		return this;
	}

	public NoiseSettings noiseSimple(int minY, int height, int sizeHorizontal, int sizeVertical) {
		return noise(NoiseShape.of(minY, height, sizeHorizontal, sizeVertical));
	}

	/** the {@code noise} block: vertical range and sampling cell sizes */
	public static class NoiseShape {
		public static final Codec<NoiseShape> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("min_y").forGetter(x -> x.minY),
				Codec.INT.fieldOf("height").forGetter(x -> x.height),
				Codec.INT.fieldOf("size_horizontal").forGetter(x -> x.sizeHorizontal),
				Codec.INT.fieldOf("size_vertical").forGetter(x -> x.sizeVertical)
		).apply(i, NoiseShape::of));

		private int minY;
		private int height;
		private int sizeHorizontal;
		private int sizeVertical;

		public static NoiseShape of(int minY, int height, int sizeHorizontal, int sizeVertical) {
			NoiseShape out = new NoiseShape();
			out.minY = minY;
			out.height = height;
			out.sizeHorizontal = sizeHorizontal;
			out.sizeVertical = sizeVertical;
			return out;
		}

		public int getMinY() { return minY; }
		public int getHeight() { return height; }
		public int getSizeHorizontal() { return sizeHorizontal; }
		public int getSizeVertical() { return sizeVertical; }
	}
}
