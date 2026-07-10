package net.vampirestudios.packwright.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FlatLevelGeneratorPreset {
	public static final Codec<FlatLevelGeneratorPreset> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("display").forGetter(x -> x.display),
			Settings.CODEC.fieldOf("settings").forGetter(x -> x.settings)
	).apply(i, (display, settings) -> new FlatLevelGeneratorPreset().display(display).settings(settings)));

	private Identifier display = Identifier.withDefaultNamespace("grass_block");
	private Settings settings = new Settings();

	public static FlatLevelGeneratorPreset preset() { return new FlatLevelGeneratorPreset(); }
	public FlatLevelGeneratorPreset display(Identifier itemId) { this.display = itemId; return this; }
	public FlatLevelGeneratorPreset settings(Settings settings) { this.settings = settings; return this; }
	public FlatLevelGeneratorPreset biome(Identifier biome) { this.settings.biome(biome); return this; }
	public FlatLevelGeneratorPreset layer(Identifier block, int height) { this.settings.layer(block, height); return this; }

	public static class Settings {
		public static final Codec<Settings> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("biome").forGetter(x -> x.biome),
				Layer.CODEC.listOf().fieldOf("layers").forGetter(x -> x.layers),
				Codec.BOOL.optionalFieldOf("lakes", false).forGetter(x -> x.lakes),
				Codec.BOOL.optionalFieldOf("features", false).forGetter(x -> x.features)
		).apply(i, (biome, layers, lakes, features) -> new Settings().biome(biome).layers(layers).lakes(lakes).features(features)));

		private Identifier biome = Identifier.withDefaultNamespace("plains");
		private List<Layer> layers = new ArrayList<>();
		private boolean lakes;
		private boolean features;

		public static Settings settings() { return new Settings(); }
		public Settings biome(Identifier biome) { this.biome = biome; return this; }
		public Settings layers(List<Layer> layers) { this.layers = new ArrayList<>(layers); return this; }
		public Settings layer(Identifier block, int height) { this.layers.add(new Layer(block, height)); return this; }
		public Settings lakes(boolean lakes) { this.lakes = lakes; return this; }
		public Settings features(boolean features) { this.features = features; return this; }
	}

	public record Layer(Identifier block, int height) {
		public static final Codec<Layer> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("block").forGetter(Layer::block),
				Codec.INT.fieldOf("height").forGetter(Layer::height)
		).apply(i, Layer::new));
	}
}
