package net.vampirestudios.packwright.data.worldgen.dimension.biomeSources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Entry;
import net.vampirestudios.packwright.data.worldgen.dimension.Parameters;
import net.vampirestudios.packwright.data.worldgen.dimension.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An explicit multi-noise biome source.
 */
public record MultiNoise(List<Entry> biomes) implements BiomeSource {
	private static final Identifier TYPE = Identifier.withDefaultNamespace("multi_noise");

	public static final MapCodec<MultiNoise> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Utils.typeCodec(TYPE).fieldOf("type").forGetter(source -> TYPE),
			Entry.CODEC.listOf().fieldOf("biomes").forGetter(MultiNoise::biomes)
	).apply(instance, (type, biomes) -> new MultiNoise(biomes)));

	public MultiNoise {
		biomes = List.copyOf(biomes);

		if (biomes.isEmpty()) {
			throw new IllegalArgumentException("A multi-noise biome source requires at least one biome entry");
		}
	}

	public static final class Builder {
		private final List<Entry> biomes = new ArrayList<>();

		public Builder() {}

		public Builder add(Identifier biome, Parameters parameters) {
			this.biomes.add(new Entry(parameters, biome));
			return this;
		}

		public Builder add(Entry entry) {
			this.biomes.add(Objects.requireNonNull(entry, "entry"));
			return this;
		}

		public MultiNoise build() {
			return new MultiNoise(this.biomes);
		}
	}
}