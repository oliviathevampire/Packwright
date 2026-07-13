package net.vampirestudios.packwright.data.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.biomeSources.BiomeSource;
import net.vampirestudios.packwright.data.worldgen.dimension.biomeSources.MultiNoise;

import java.util.Objects;

/**
 * A dimension datapack entry.
 *
 * <pre>{@code
 * Dimension.dimension()
 *     .type(myModId("ember_wastes_type"))
 *     .noiseGenerator(
 *         myModId("ember_wastes"),
 *         Dimension.BiomeSource.multiNoiseBuilder()
 *             .add(
 *                 myModId("lava_deltas"),
 *                 Dimension.BiomeSource.Parameters.of(
 *                     Dimension.BiomeSource.Parameter.span(0.45F, 1.0F),
 *                     Dimension.BiomeSource.Parameter.full(),
 *                     Dimension.BiomeSource.Parameter.span(-1.0F, -0.35F),
 *                     Dimension.BiomeSource.Parameter.span(-1.0F, 0.15F),
 *                     Dimension.BiomeSource.Parameter.full(),
 *                     Dimension.BiomeSource.Parameter.full()
 *                 )
 *             )
 *             .add(
 *                 myModId("ember_wastes"),
 *                 Dimension.BiomeSource.Parameters.full()
 *             )
 *             .build()
 *     );
 * }</pre>
 */
public final class Dimension {
	public static final Codec<Dimension> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("type").forGetter(Dimension::getType),
			Generator.CODEC.fieldOf("generator").forGetter(Dimension::getGenerator)
	).apply(instance, Dimension::new));

	private Identifier type;
	private Generator generator;

	private Dimension() {
		this(Identifier.withDefaultNamespace("overworld"), Generator.noise(
				Identifier.withDefaultNamespace("overworld"),
				BiomeSource.fixed(Identifier.withDefaultNamespace("plains"))
		));
	}

	private Dimension(Identifier type, Generator generator) {
		this.type = Objects.requireNonNull(type, "type");
		this.generator = Objects.requireNonNull(generator, "generator");
	}

	public static Dimension dimension() {
		return new Dimension();
	}

	// -------------------------------------------------------------------------
	// Core setters
	// -------------------------------------------------------------------------

	public Dimension type(Identifier type) {
		this.type = Objects.requireNonNull(type, "type");
		return this;
	}

	public Dimension generator(Generator generator) {
		this.generator = Objects.requireNonNull(generator, "generator");
		return this;
	}

	/**
	 * Sets the noise settings while preserving the current biome source when
	 * the existing generator is also a noise generator.
	 */
	public Dimension noiseGenerator(Identifier settingsId) {
		BiomeSource biomeSource = this.generator instanceof NoiseGenerator noise
				? noise.biomeSource()
				: BiomeSource.fixed(Identifier.withDefaultNamespace("plains")
		);
		this.generator = Generator.noise(settingsId, biomeSource);
		return this;
	}

	/**
	 * Creates a noise generator with both its settings and biome source.
	 */
	public Dimension noiseGenerator(Identifier settingsId, BiomeSource biomeSource) {
		this.generator = Generator.noise(settingsId, biomeSource);
		return this;
	}

	/**
	 * Changes the biome source of the current noise generator.
	 */
	public Dimension biomeSource(BiomeSource biomeSource) {
		Objects.requireNonNull(biomeSource, "biomeSource");

		if (!(this.generator instanceof NoiseGenerator noise)) {
			throw new IllegalStateException("Biome sources can only be assigned to noise generators");
		}

		this.generator = new NoiseGenerator(noise.settings(), biomeSource);

		return this;
	}

	/**
	 * Uses one biome everywhere.
	 */
	public Dimension fixedBiome(Identifier biomeId) {
		return biomeSource(BiomeSource.fixed(biomeId));
	}

	/**
	 * Uses a built-in multi-noise preset such as
	 * {@code minecraft:overworld} or {@code minecraft:nether}.
	 */
	public Dimension multiNoisePreset(Identifier preset) {
		return biomeSource(BiomeSource.multiNoisePreset(preset));
	}

	// -------------------------------------------------------------------------
	// Generator hierarchy
	// -------------------------------------------------------------------------

	/**
	 * Uses an explicit list of multi-noise biome entries.
	 */
	public Dimension multiNoiseBiomeSource(MultiNoise source) {
		return biomeSource(source);
	}

	public Identifier getType() {
		return this.type;
	}

	// -------------------------------------------------------------------------
	// Biome-source hierarchy
	// -------------------------------------------------------------------------

	public Generator getGenerator() {
		return this.generator;
	}

	// -------------------------------------------------------------------------
	// Codec helpers
	// -------------------------------------------------------------------------

	public sealed interface Generator permits NoiseGenerator {
		Codec<Generator> CODEC = new Codec<>() {
			@Override
			public <T> DataResult<Pair<Generator, T>> decode(DynamicOps<T> ops, T input) {
				return ops.getMap(input).flatMap(map -> {
					String rawType = Utils.string(map, ops, "type", "");

					if (Utils.normalizeType(rawType).equals("noise")) {
						return NoiseGenerator.CODEC.codec()
								.decode(ops, input)
								.map(pair -> pair.mapFirst(generator -> generator));
					}
					return DataResult.error(() -> rawType.isEmpty()
							? "Dimension generator is missing its type"
							: "Unsupported dimension generator type: " + rawType
					);
				});
			}

			@Override
			public <T> DataResult<T> encode(Generator input, DynamicOps<T> ops, T prefix) {
				if (input instanceof NoiseGenerator generator)
					return NoiseGenerator.CODEC.codec().encode(generator, ops, prefix);

				return DataResult.error(() -> "Unsupported dimension generator: " + input.getClass().getName());
			}
		};

		static NoiseGenerator noise(Identifier settings, BiomeSource biomeSource) {
			return new NoiseGenerator(settings, biomeSource);
		}
	}

	/**
	 * The {@code minecraft:noise} dimension generator.
	 */
	public record NoiseGenerator(Identifier settings, BiomeSource biomeSource) implements Generator {
		private static final Identifier TYPE = Identifier.withDefaultNamespace("noise");

		public static final MapCodec<NoiseGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Utils.typeCodec(TYPE).fieldOf("type").forGetter(generator -> TYPE),
				Identifier.CODEC.fieldOf("settings").forGetter(NoiseGenerator::settings),
				BiomeSource.CODEC.fieldOf("biome_source").forGetter(NoiseGenerator::biomeSource)
		).apply(instance, (type, settings, biomeSource) -> new NoiseGenerator(settings, biomeSource)));

		public NoiseGenerator {
			Objects.requireNonNull(settings, "settings");
			Objects.requireNonNull(biomeSource, "biomeSource");
		}
	}
}