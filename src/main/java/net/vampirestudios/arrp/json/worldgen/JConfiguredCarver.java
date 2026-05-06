package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

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

	public static JConfiguredCarver canyon() {
		return carver().type("minecraft:canyon");
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
				DebugSettings.CODEC.optionalFieldOf("debug_settings").forGetter(x -> Optional.ofNullable(x.debugSettings)),
				Codec.FLOAT.fieldOf("probability").forGetter(x -> x.probability),
				HeightProvider.CODEC.fieldOf("y").forGetter(x -> x.y),
				FloatProvider.CODEC.fieldOf("yScale").forGetter(x -> x.yScale),
				VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(x -> x.lavaLevel),
				Codec.STRING.fieldOf("replaceable").forGetter(x -> x.replaceable),
				FloatProvider.CODEC.optionalFieldOf("horizontal_radius_multiplier").forGetter(x -> Optional.ofNullable(x.horizontalRadiusMultiplier)),
				FloatProvider.CODEC.optionalFieldOf("vertical_radius_multiplier").forGetter(x -> Optional.ofNullable(x.verticalRadiusMultiplier)),
				FloatProvider.CODEC.optionalFieldOf("floor_level").forGetter(x -> Optional.ofNullable(x.floorLevel)),
				FloatProvider.CODEC.optionalFieldOf("vertical_rotation").forGetter(x -> Optional.ofNullable(x.verticalRotation)),
				CanyonShape.CODEC.optionalFieldOf("shape").forGetter(x -> Optional.ofNullable(x.shape))
		).apply(i, (debugSettings, probability, y, yScale, lavaLevel, replaceable, horizontal, vertical, floor, rotation, shape) -> {
			Config out = new Config();
			out.debugSettings = debugSettings.orElse(null);
			out.probability = probability;
			out.y = y;
			out.yScale = yScale;
			out.lavaLevel = lavaLevel;
			out.replaceable = replaceable;
			out.horizontalRadiusMultiplier = horizontal.orElse(null);
			out.verticalRadiusMultiplier = vertical.orElse(null);
			out.floorLevel = floor.orElse(null);
			out.verticalRotation = rotation.orElse(null);
			out.shape = shape.orElse(null);
			return out;
		}));

		private DebugSettings debugSettings;
		private float probability = 0.1F;
		private HeightProvider y = HeightProvider.uniform(VerticalAnchor.absolute(8), VerticalAnchor.absolute(180));
		private FloatProvider yScale = FloatProvider.constant(1.0F);
		private VerticalAnchor lavaLevel = VerticalAnchor.aboveBottom(8);
		private String replaceable = "#minecraft:overworld_carver_replaceables";
		private FloatProvider horizontalRadiusMultiplier;
		private FloatProvider verticalRadiusMultiplier;
		private FloatProvider floorLevel;
		private FloatProvider verticalRotation;
		private CanyonShape shape;

		public static Config config() { return new Config(); }
		public Config debugSettings(DebugSettings debugSettings) { this.debugSettings = debugSettings; return this; }
		public Config probability(float probability) { this.probability = probability; return this; }
		public Config y(HeightProvider y) { this.y = y; return this; }
		public Config yScale(float yScale) { this.yScale = FloatProvider.constant(yScale); return this; }
		public Config yScale(FloatProvider yScale) { this.yScale = yScale; return this; }
		public Config lavaLevel(VerticalAnchor lavaLevel) { this.lavaLevel = lavaLevel; return this; }
		public Config replaceable(String blockOrTag) { this.replaceable = blockOrTag; return this; }
		public Config horizontalRadiusMultiplier(FloatProvider value) { this.horizontalRadiusMultiplier = value; return this; }
		public Config verticalRadiusMultiplier(FloatProvider value) { this.verticalRadiusMultiplier = value; return this; }
		public Config floorLevel(FloatProvider value) { this.floorLevel = value; return this; }
		public Config verticalRotation(FloatProvider value) { this.verticalRotation = value; return this; }
		public Config shape(CanyonShape shape) { this.shape = shape; return this; }
	}

	public static class DebugSettings {
		public static final Codec<DebugSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.BOOL.optionalFieldOf("debug_mode").forGetter(x -> Optional.ofNullable(x.debugMode)),
				BlockState.CODEC.optionalFieldOf("air_state").forGetter(x -> Optional.ofNullable(x.airState)),
				BlockState.CODEC.optionalFieldOf("water_state").forGetter(x -> Optional.ofNullable(x.waterState)),
				BlockState.CODEC.optionalFieldOf("lava_state").forGetter(x -> Optional.ofNullable(x.lavaState)),
				BlockState.CODEC.optionalFieldOf("barrier_state").forGetter(x -> Optional.ofNullable(x.barrierState))
		).apply(i, (debugMode, air, water, lava, barrier) -> new DebugSettings()
				.debugMode(debugMode.orElse(null))
				.airState(air.orElse(null))
				.waterState(water.orElse(null))
				.lavaState(lava.orElse(null))
				.barrierState(barrier.orElse(null))));

		private Boolean debugMode;
		private BlockState airState;
		private BlockState waterState;
		private BlockState lavaState;
		private BlockState barrierState;

		public static DebugSettings debugSettings() { return new DebugSettings(); }
		public DebugSettings debugMode(Boolean debugMode) { this.debugMode = debugMode; return this; }
		public DebugSettings airState(BlockState state) { this.airState = state; return this; }
		public DebugSettings waterState(BlockState state) { this.waterState = state; return this; }
		public DebugSettings lavaState(BlockState state) { this.lavaState = state; return this; }
		public DebugSettings barrierState(BlockState state) { this.barrierState = state; return this; }
	}

	public static class CanyonShape {
		public static final Codec<CanyonShape> CODEC = RecordCodecBuilder.create(i -> i.group(
				FloatProvider.CODEC.fieldOf("distance_factor").forGetter(x -> x.distanceFactor),
				FloatProvider.CODEC.fieldOf("horizontal_radius_factor").forGetter(x -> x.horizontalRadiusFactor),
				FloatProvider.CODEC.fieldOf("thickness").forGetter(x -> x.thickness),
				Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(x -> x.verticalRadiusCenterFactor),
				Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(x -> x.verticalRadiusDefaultFactor),
				Codec.INT.fieldOf("width_smoothness").forGetter(x -> x.widthSmoothness)
		).apply(i, (distanceFactor, horizontalRadiusFactor, thickness, verticalRadiusCenterFactor, verticalRadiusDefaultFactor, widthSmoothness) ->
				new CanyonShape()
						.distanceFactor(distanceFactor)
						.horizontalRadiusFactor(horizontalRadiusFactor)
						.thickness(thickness)
						.verticalRadiusCenterFactor(verticalRadiusCenterFactor)
						.verticalRadiusDefaultFactor(verticalRadiusDefaultFactor)
						.widthSmoothness(widthSmoothness)));

		private FloatProvider distanceFactor = FloatProvider.uniform(0.75F, 1.0F);
		private FloatProvider horizontalRadiusFactor = FloatProvider.uniform(0.75F, 1.0F);
		private FloatProvider thickness = FloatProvider.trapezoid(0.0F, 6.0F, 2.0F);
		private float verticalRadiusCenterFactor = 0.0F;
		private float verticalRadiusDefaultFactor = 1.0F;
		private int widthSmoothness = 3;

		public static CanyonShape canyonShape() { return new CanyonShape(); }
		public CanyonShape distanceFactor(FloatProvider distanceFactor) { this.distanceFactor = distanceFactor; return this; }
		public CanyonShape horizontalRadiusFactor(FloatProvider horizontalRadiusFactor) { this.horizontalRadiusFactor = horizontalRadiusFactor; return this; }
		public CanyonShape thickness(FloatProvider thickness) { this.thickness = thickness; return this; }
		public CanyonShape verticalRadiusCenterFactor(float verticalRadiusCenterFactor) { this.verticalRadiusCenterFactor = verticalRadiusCenterFactor; return this; }
		public CanyonShape verticalRadiusDefaultFactor(float verticalRadiusDefaultFactor) { this.verticalRadiusDefaultFactor = verticalRadiusDefaultFactor; return this; }
		public CanyonShape widthSmoothness(int widthSmoothness) { this.widthSmoothness = widthSmoothness; return this; }
	}
}
