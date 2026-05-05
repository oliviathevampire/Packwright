package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class JConfiguredCarver {
	public static final Codec<JConfiguredCarver> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
			Config.CODEC.fieldOf("config").forGetter(x -> x.config)
	).apply(i, (type, config) -> new JConfiguredCarver().type(type).config(config)));

	private Identifier type = Identifier.withDefaultNamespace("cave");
	private Config config = new Config();

	public static JConfiguredCarver carver() {
		return new JConfiguredCarver();
	}

	public JConfiguredCarver type(Identifier type) { this.type = type; return this; }
	public JConfiguredCarver type(String type) { return type(Identifier.tryParse(type)); }
	public JConfiguredCarver config(Config config) { this.config = config; return this; }
	public JConfiguredCarver probability(float probability) { this.config.probability(probability); return this; }
	public JConfiguredCarver replaceable(String blockOrTag) { this.config.replaceable(blockOrTag); return this; }

	public Identifier getType() { return type; }
	public Config getConfig() { return config; }

	public static class Config {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.FLOAT.fieldOf("probability").forGetter(x -> x.probability),
				HeightProvider.CODEC.fieldOf("y").forGetter(x -> x.y),
				FloatProvider.CODEC.fieldOf("yScale").forGetter(x -> x.yScale),
				VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(x -> x.lavaLevel),
				Codec.STRING.listOf().fieldOf("replaceable").forGetter(x -> x.replaceable),
				FloatProvider.CODEC.optionalFieldOf("horizontal_radius_multiplier", FloatProvider.constant(1.0F)).forGetter(x -> x.horizontalRadiusMultiplier),
				FloatProvider.CODEC.optionalFieldOf("vertical_radius_multiplier", FloatProvider.constant(1.0F)).forGetter(x -> x.verticalRadiusMultiplier),
				FloatProvider.CODEC.optionalFieldOf("floor_level", FloatProvider.constant(-1.0F)).forGetter(x -> x.floorLevel),
				FloatProvider.CODEC.optionalFieldOf("vertical_rotation", FloatProvider.constant(0.0F)).forGetter(x -> x.verticalRotation),
				CanyonShape.CODEC.optionalFieldOf("shape", new CanyonShape()).forGetter(x -> x.shape)
		).apply(i, (probability, y, yScale, lavaLevel, replaceable, horizontal, vertical, floor, rotation, shape) -> {
			Config out = new Config();
			out.probability = probability;
			out.y = y;
			out.yScale = yScale;
			out.lavaLevel = lavaLevel;
			out.replaceable = new ArrayList<>(replaceable);
			out.horizontalRadiusMultiplier = horizontal;
			out.verticalRadiusMultiplier = vertical;
			out.floorLevel = floor;
			out.verticalRotation = rotation;
			out.shape = shape;
			return out;
		}));

		private float probability = 0.1F;
		private HeightProvider y = HeightProvider.uniform(VerticalAnchor.absolute(8), VerticalAnchor.absolute(180));
		private FloatProvider yScale = FloatProvider.constant(1.0F);
		private VerticalAnchor lavaLevel = VerticalAnchor.aboveBottom(8);
		private List<String> replaceable = new ArrayList<>();
		private FloatProvider horizontalRadiusMultiplier = FloatProvider.constant(1.0F);
		private FloatProvider verticalRadiusMultiplier = FloatProvider.constant(1.0F);
		private FloatProvider floorLevel = FloatProvider.constant(-1.0F);
		private FloatProvider verticalRotation = FloatProvider.constant(0.0F);
		private CanyonShape shape = new CanyonShape();

		public static Config config() { return new Config(); }
		public Config probability(float probability) { this.probability = probability; return this; }
		public Config y(HeightProvider y) { this.y = y; return this; }
		public Config yScale(FloatProvider yScale) { this.yScale = yScale; return this; }
		public Config lavaLevel(VerticalAnchor lavaLevel) { this.lavaLevel = lavaLevel; return this; }
		public Config replaceable(String blockOrTag) { this.replaceable.add(blockOrTag); return this; }
		public Config horizontalRadiusMultiplier(FloatProvider value) { this.horizontalRadiusMultiplier = value; return this; }
		public Config verticalRadiusMultiplier(FloatProvider value) { this.verticalRadiusMultiplier = value; return this; }
		public Config floorLevel(FloatProvider value) { this.floorLevel = value; return this; }
		public Config verticalRotation(FloatProvider value) { this.verticalRotation = value; return this; }
		public Config shape(CanyonShape shape) { this.shape = shape; return this; }
	}

	public record VerticalAnchor(String type, int value) {
		public static final Codec<VerticalAnchor> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(VerticalAnchor::type),
				Codec.INT.fieldOf("value").forGetter(VerticalAnchor::value)
		).apply(i, VerticalAnchor::new));
		public static VerticalAnchor absolute(int value) { return new VerticalAnchor("absolute", value); }
		public static VerticalAnchor aboveBottom(int value) { return new VerticalAnchor("above_bottom", value); }
		public static VerticalAnchor belowTop(int value) { return new VerticalAnchor("below_top", value); }
	}

	public record HeightProvider(String type, VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
		public static final Codec<HeightProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(HeightProvider::type),
				VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(HeightProvider::minInclusive),
				VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(HeightProvider::maxInclusive)
		).apply(i, HeightProvider::new));
		public static HeightProvider uniform(VerticalAnchor min, VerticalAnchor max) { return new HeightProvider("minecraft:uniform", min, max); }
	}

	public record FloatProvider(String type, float value, float minInclusive, float maxInclusive) {
		public static final Codec<FloatProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(FloatProvider::type),
				Codec.FLOAT.optionalFieldOf("value", 0.0F).forGetter(FloatProvider::value),
				Codec.FLOAT.optionalFieldOf("min_inclusive", 0.0F).forGetter(FloatProvider::minInclusive),
				Codec.FLOAT.optionalFieldOf("max_inclusive", 0.0F).forGetter(FloatProvider::maxInclusive)
		).apply(i, FloatProvider::new));
		public static FloatProvider constant(float value) { return new FloatProvider("minecraft:constant", value, value, value); }
		public static FloatProvider uniform(float min, float max) { return new FloatProvider("minecraft:uniform", 0.0F, min, max); }
	}

	public static class CanyonShape {
		public static final Codec<CanyonShape> CODEC = RecordCodecBuilder.create(i -> i.group(
				FloatProvider.CODEC.optionalFieldOf("distance_factor", FloatProvider.constant(1.0F)).forGetter(x -> x.distanceFactor),
				FloatProvider.CODEC.optionalFieldOf("thickness", FloatProvider.constant(1.0F)).forGetter(x -> x.thickness),
				Codec.INT.optionalFieldOf("width_smoothness", 3).forGetter(x -> x.widthSmoothness)
		).apply(i, (distanceFactor, thickness, widthSmoothness) -> new CanyonShape().distanceFactor(distanceFactor).thickness(thickness).widthSmoothness(widthSmoothness)));

		private FloatProvider distanceFactor = FloatProvider.constant(1.0F);
		private FloatProvider thickness = FloatProvider.constant(1.0F);
		private int widthSmoothness = 3;
		public CanyonShape distanceFactor(FloatProvider distanceFactor) { this.distanceFactor = distanceFactor; return this; }
		public CanyonShape thickness(FloatProvider thickness) { this.thickness = thickness; return this; }
		public CanyonShape widthSmoothness(int widthSmoothness) { this.widthSmoothness = widthSmoothness; return this; }
	}
}
