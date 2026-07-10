package net.vampirestudios.arrp.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class WorldPreset {
	public static final Codec<WorldPreset> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.unboundedMap(Identifier.CODEC, Dimension.CODEC).fieldOf("dimensions").forGetter(x -> x.dimensions)
	).apply(i, dimensions -> new WorldPreset().dimensions(dimensions)));

	private Map<Identifier, Dimension> dimensions = new LinkedHashMap<>();

	public static WorldPreset preset() { return new WorldPreset(); }
	public WorldPreset dimensions(Map<Identifier, Dimension> dimensions) { this.dimensions = new LinkedHashMap<>(dimensions); return this; }
	public WorldPreset dimension(Identifier id, Dimension dimension) { this.dimensions.put(id, dimension); return this; }
	public WorldPreset overworld(Dimension dimension) { return dimension(vanillaId("overworld"), dimension); }
	public WorldPreset nether(Dimension dimension) { return dimension(vanillaId("the_nether"), dimension); }
	public WorldPreset end(Dimension dimension) { return dimension(vanillaId("the_end"), dimension); }

	public static class Dimension {
		public static final Codec<Dimension> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
				Generator.CODEC.fieldOf("generator").forGetter(x -> x.generator)
		).apply(i, (type, generator) -> new Dimension().type(type).generator(generator)));

		private Identifier type = vanillaId("overworld");
		private Generator generator = new Generator();

		public static Dimension dimension() { return new Dimension(); }
		public Dimension type(Identifier type) { this.type = type; return this; }
		public Dimension generator(Generator generator) { this.generator = generator; return this; }
		public Dimension noiseGenerator(Identifier settings, Generator.BiomeSource biomeSource) {
			this.generator = Generator.noise(settings, biomeSource);
			return this;
		}
	}

	public static class Generator {
		public static final Codec<Generator> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
				// required by the noise generator; fieldOf+orElse always encodes it
				Identifier.CODEC.fieldOf("settings").orElse(vanillaId("overworld")).forGetter(x -> x.settings),
				BiomeSource.CODEC.fieldOf("biome_source").forGetter(x -> x.biomeSource)
		).apply(i, (type, settings, biomeSource) -> new Generator().type(type).settings(settings).biomeSource(biomeSource)));

		private Identifier type = vanillaId("noise");
		private Identifier settings = vanillaId("overworld");
		private BiomeSource biomeSource = BiomeSource.fixed(vanillaId("plains"));

		public static Generator noise(Identifier settings, BiomeSource biomeSource) {
			return new Generator().type(vanillaId("noise")).settings(settings).biomeSource(biomeSource);
		}

		public Generator type(Identifier type) { this.type = type; return this; }
		public Generator settings(Identifier settings) { this.settings = settings; return this; }
		public Generator biomeSource(BiomeSource biomeSource) { this.biomeSource = biomeSource; return this; }

		public static class BiomeSource {
			public static final Codec<BiomeSource> CODEC = RecordCodecBuilder.create(i -> i.group(
					Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
					// required by the fixed biome source; fieldOf+orElse always encodes it
					Identifier.CODEC.fieldOf("biome").orElse(vanillaId("plains")).forGetter(x -> x.biome)
			).apply(i, (type, biome) -> new BiomeSource().type(type).biome(biome)));

			private Identifier type = vanillaId("fixed");
			private Identifier biome = vanillaId("plains");

			public static BiomeSource fixed(Identifier biome) { return new BiomeSource().type(vanillaId("fixed")).biome(biome); }
			public BiomeSource type(Identifier type) { this.type = type; return this; }
			public BiomeSource biome(Identifier biome) { this.biome = biome; return this; }
		}
	}
}
