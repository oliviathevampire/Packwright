package net.vampirestudios.packwright.data.worldgen.dimension.biomeSources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Entry;
import net.vampirestudios.packwright.data.worldgen.dimension.Utils;

import java.util.List;

public sealed interface BiomeSource permits Fixed, MultiNoisePreset, MultiNoise, Checkerboard, TheEnd {

	Codec<BiomeSource> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<BiomeSource, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String rawType = Utils.string(map, ops, "type", "");

				return switch (Utils.normalizeType(rawType)) {
					case "fixed" -> Fixed.CODEC.codec()
							.decode(ops, input)
							.map(pair -> pair.mapFirst(source -> source));

					case "multi_noise" -> {
						if (map.get("preset") != null) {
							yield MultiNoisePreset.CODEC.codec()
									.decode(ops, input)
									.map(pair -> pair.mapFirst(source -> source));
						}

						if (map.get("biomes") != null) {
							yield MultiNoise.CODEC.codec()
									.decode(ops, input)
									.map(pair -> pair.mapFirst(source -> source));
						}

						yield DataResult.error(() -> "A minecraft:multi_noise biome source must contain either preset or biomes");
					}

					case "checkerboard" -> Checkerboard.CODEC.codec()
							.decode(ops, input)
							.map(pair -> pair.mapFirst(source -> source));

					case "the_end" -> TheEnd.CODEC.codec()
							.decode(ops, input)
							.map(pair -> pair.mapFirst(source -> source));

					default -> DataResult.error(() -> rawType.isEmpty()
							? "Biome source is missing its type"
							: "Unsupported biome source type: " + rawType
					);
				};
			});
		}

		@Override
		public <T> DataResult<T> encode(BiomeSource input, DynamicOps<T> ops, T prefix) {
			if (input instanceof Fixed source) {
				return Fixed.CODEC.codec().encode(source, ops, prefix);
			}

			if (input instanceof MultiNoisePreset source) {
				return MultiNoisePreset.CODEC.codec().encode(source, ops, prefix);
			}

			if (input instanceof MultiNoise source) {
				return MultiNoise.CODEC.codec().encode(source, ops, prefix);
			}

			if (input instanceof Checkerboard source) {
				return Checkerboard.CODEC.codec().encode(source, ops, prefix);
			}

			if (input instanceof TheEnd source) {
				return TheEnd.CODEC.codec().encode(source, ops, prefix);
			}

			return DataResult.error(() -> "Unsupported biome source: " + input.getClass().getName());
		}
	};

	/**
	 * Creates a {@code minecraft:fixed} biome source.
	 */
	static Fixed fixed(Identifier biome) {
		return new Fixed(biome);
	}

	/**
	 * Creates a preset-based {@code minecraft:multi_noise} source.
	 */
	static MultiNoisePreset multiNoisePreset(Identifier preset) {
		return new MultiNoisePreset(preset);
	}

	/**
	 * Creates an explicit {@code minecraft:multi_noise} source.
	 */
	static MultiNoise multiNoise(List<Entry> biomes) {
		return new MultiNoise(biomes);
	}

	static MultiNoise multiNoise(Entry... biomes) {
		return new MultiNoise(List.of(biomes));
	}

	static MultiNoise.Builder multiNoiseBuilder() {
		return new MultiNoise.Builder();
	}

	/**
	 * Creates a {@code minecraft:checkerboard} biome source, using the vanilla default scale of 2.
	 */
	static Checkerboard checkerboard(List<Identifier> biomes) {
		return new Checkerboard(biomes);
	}

	/**
	 * Creates a {@code minecraft:checkerboard} biome source with an explicit scale.
	 */
	static Checkerboard checkerboard(List<Identifier> biomes, int scale) {
		return new Checkerboard(biomes, scale);
	}

	/**
	 * Creates a {@code minecraft:the_end} biome source. Has no configurable fields.
	 */
	static TheEnd theEnd() {
		return TheEnd.INSTANCE;
	}

}