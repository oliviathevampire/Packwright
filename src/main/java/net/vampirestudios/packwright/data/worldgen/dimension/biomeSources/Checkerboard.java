package net.vampirestudios.packwright.data.worldgen.dimension.biomeSources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Utils;

import java.util.List;
import java.util.Objects;

/**
 * A biome source that lays out biomes in a checkerboard pattern of columns.
 * Mirrors vanilla's {@code CheckerboardColumnBiomeSource}.
 */
public record Checkerboard(List<Identifier> biomes, int scale) implements BiomeSource {
	private static final Identifier TYPE = Identifier.withDefaultNamespace("checkerboard");

	public static final MapCodec<Checkerboard> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Utils.typeCodec(TYPE).fieldOf("type").forGetter(source -> TYPE),
			Identifier.CODEC.listOf().fieldOf("biomes").forGetter(Checkerboard::biomes),
			Codec.INT.fieldOf("scale").orElse(2).forGetter(Checkerboard::scale)
	).apply(instance, (type, biomes, scale) -> new Checkerboard(biomes, scale)));

	public Checkerboard {
		Objects.requireNonNull(biomes, "biomes");
		biomes = List.copyOf(biomes);

		if (biomes.isEmpty()) {
			throw new IllegalArgumentException("A checkerboard biome source requires at least one biome entry");
		}
	}

	public Checkerboard(List<Identifier> biomes) {
		this(biomes, 2);
	}
}
