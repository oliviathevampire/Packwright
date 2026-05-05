package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class JFlatLevelGeneratorPreset {
	public static final Codec<JFlatLevelGeneratorPreset> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("display").forGetter(x -> x.display),
			Settings.CODEC.fieldOf("settings").forGetter(x -> x.settings)
	).apply(i, (display, settings) -> new JFlatLevelGeneratorPreset().display(display).settings(settings)));

	private String display = "minecraft:grass_block";
	private Settings settings = new Settings();

	public static JFlatLevelGeneratorPreset preset() { return new JFlatLevelGeneratorPreset(); }
	public JFlatLevelGeneratorPreset display(String itemId) { this.display = itemId; return this; }
	public JFlatLevelGeneratorPreset settings(Settings settings) { this.settings = settings; return this; }
	public JFlatLevelGeneratorPreset biome(String biome) { this.settings.biome(biome); return this; }
	public JFlatLevelGeneratorPreset layer(String block, int height) { this.settings.layer(block, height); return this; }

	public static class Settings {
		public static final Codec<Settings> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("biome").forGetter(x -> x.biome),
				Layer.CODEC.listOf().fieldOf("layers").forGetter(x -> x.layers),
				Codec.BOOL.optionalFieldOf("lakes", false).forGetter(x -> x.lakes),
				Codec.BOOL.optionalFieldOf("features", false).forGetter(x -> x.features)
		).apply(i, (biome, layers, lakes, features) -> new Settings().biome(biome).layers(layers).lakes(lakes).features(features)));

		private String biome = "minecraft:plains";
		private List<Layer> layers = new ArrayList<>();
		private boolean lakes;
		private boolean features;

		public static Settings settings() { return new Settings(); }
		public Settings biome(String biome) { this.biome = biome; return this; }
		public Settings layers(List<Layer> layers) { this.layers = new ArrayList<>(layers); return this; }
		public Settings layer(String block, int height) { this.layers.add(new Layer(block, height)); return this; }
		public Settings lakes(boolean lakes) { this.lakes = lakes; return this; }
		public Settings features(boolean features) { this.features = features; return this; }
	}

	public record Layer(String block, int height) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("block").forGetter(Layer::block),
				Codec.INT.fieldOf("height").forGetter(Layer::height)
		).apply(i, Layer::new));
	}
}
