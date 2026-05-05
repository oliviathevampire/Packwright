package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class JWorldPreset {
	public static final Codec<JWorldPreset> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.unboundedMap(Codec.STRING, Dimension.CODEC).fieldOf("dimensions").forGetter(x -> x.dimensions)
	).apply(i, dimensions -> new JWorldPreset().dimensions(dimensions)));

	private Map<String, Dimension> dimensions = new LinkedHashMap<>();

	public static JWorldPreset preset() { return new JWorldPreset(); }
	public JWorldPreset dimensions(Map<String, Dimension> dimensions) { this.dimensions = new LinkedHashMap<>(dimensions); return this; }
	public JWorldPreset dimension(String id, Dimension dimension) { this.dimensions.put(id, dimension); return this; }
	public JWorldPreset overworld(Dimension dimension) { return dimension("minecraft:overworld", dimension); }
	public JWorldPreset nether(Dimension dimension) { return dimension("minecraft:the_nether", dimension); }
	public JWorldPreset end(Dimension dimension) { return dimension("minecraft:the_end", dimension); }

	public static class Dimension {
		public static final Codec<Dimension> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> x.type),
				Generator.CODEC.fieldOf("generator").forGetter(x -> x.generator)
		).apply(i, (type, generator) -> new Dimension().type(type).generator(generator)));

		private String type = "minecraft:overworld";
		private Generator generator = new Generator();

		public static Dimension dimension() { return new Dimension(); }
		public Dimension type(String type) { this.type = type; return this; }
		public Dimension generator(Generator generator) { this.generator = generator; return this; }
		public Dimension noiseGenerator(String settings, Generator.BiomeSource biomeSource) {
			this.generator = Generator.noise(settings, biomeSource);
			return this;
		}
	}

	public static class Generator {
		public static final Codec<Generator> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> x.type),
				Codec.STRING.optionalFieldOf("settings", "minecraft:overworld").forGetter(x -> x.settings),
				BiomeSource.CODEC.fieldOf("biome_source").forGetter(x -> x.biomeSource)
		).apply(i, (type, settings, biomeSource) -> new Generator().type(type).settings(settings).biomeSource(biomeSource)));

		private String type = "minecraft:noise";
		private String settings = "minecraft:overworld";
		private BiomeSource biomeSource = BiomeSource.fixed("minecraft:plains");

		public static Generator noise(String settings, BiomeSource biomeSource) {
			return new Generator().type("minecraft:noise").settings(settings).biomeSource(biomeSource);
		}

		public Generator type(String type) { this.type = type; return this; }
		public Generator settings(String settings) { this.settings = settings; return this; }
		public Generator biomeSource(BiomeSource biomeSource) { this.biomeSource = biomeSource; return this; }

		public static class BiomeSource {
			public static final Codec<BiomeSource> CODEC = RecordCodecBuilder.create(i -> i.group(
					Codec.STRING.fieldOf("type").forGetter(x -> x.type),
					Codec.STRING.optionalFieldOf("biome", "minecraft:plains").forGetter(x -> x.biome)
			).apply(i, (type, biome) -> new BiomeSource().type(type).biome(biome)));

			private String type = "minecraft:fixed";
			private String biome = "minecraft:plains";

			public static BiomeSource fixed(String biome) { return new BiomeSource().type("minecraft:fixed").biome(biome); }
			public BiomeSource type(String type) { this.type = type; return this; }
			public BiomeSource biome(String biome) { this.biome = biome; return this; }
		}
	}
}
