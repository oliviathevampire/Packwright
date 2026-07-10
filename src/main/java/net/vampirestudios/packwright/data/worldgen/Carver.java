package net.vampirestudios.packwright.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * A carver in the {@code worldgen/configured_carver} registry, in the 26.2 format:
 * the configuration is nested under a {@code config} field, the vertical scale is the
 * top-level {@code yScale} field, and {@code lava_level}/{@code replaceable} still
 * exist. (26.3 flattened all of this and renamed the registry to {@code worldgen/carver}
 * — see the main branch for that format.)
 */
public class Carver {
	public static final Codec<Carver> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
			Config.CODEC.fieldOf("config").forGetter(x -> x.config)
	).apply(i, (type, config) -> new Carver().type(type).config(config)));

	private Identifier type = Identifier.withDefaultNamespace("cave");
	private Config config = new Config();

	public static Carver carver() {
		return new Carver();
	}

	public static Carver cave() {
		return carver().type("minecraft:cave");
	}

	public static Carver canyon() {
		return carver().type("minecraft:canyon");
	}

	public Carver type(Identifier type) { this.type = type; return this; }
	public Carver type(String type) { return type(Identifier.tryParse(type)); }
	public Carver config(Config config) { this.config = config; return this; }
	public Carver probability(float probability) { this.config.probability(probability); return this; }
	public Carver y(HeightProvider y) { this.config.y(y); return this; }
	public Carver replaceable(Identifier block) { this.config.replaceable(block); return this; }
	public Carver replaceableTag(Identifier tag) { this.config.replaceableTag(tag); return this; }

	public Identifier getType() { return type; }
	public Config getConfig() { return config; }

	public static class Config {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(i -> i.group(
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
		).apply(i, (probability, y, yScale, lavaLevel, replaceable, horizontal, vertical, floor, rotation, shape) -> {
			Config out = new Config();
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

		private float probability = 0.1F;
		private HeightProvider y = HeightProvider.uniform(VerticalAnchor.absolute(8), VerticalAnchor.absolute(180));
		private FloatProvider yScale = FloatProvider.constant(1.0F);
		private VerticalAnchor lavaLevel = VerticalAnchor.aboveBottom(8);
		private String replaceable = "#minecraft:overworld_carver_replaceables";
		// required for cave carvers; harmlessly ignored by canyons
		private FloatProvider horizontalRadiusMultiplier = FloatProvider.uniform(0.7F, 1.4F);
		private FloatProvider verticalRadiusMultiplier = FloatProvider.uniform(0.8F, 1.3F);
		private FloatProvider floorLevel = FloatProvider.uniform(-1.0F, -0.4F);
		private FloatProvider verticalRotation;
		private CanyonShape shape;

		public static Config config() { return new Config(); }
		public Config probability(float probability) { this.probability = probability; return this; }
		public Config y(HeightProvider y) { this.y = y; return this; }
		public Config yScale(float yScale) { this.yScale = FloatProvider.constant(yScale); return this; }
		public Config yScale(FloatProvider yScale) { this.yScale = yScale; return this; }
		public Config lavaLevel(VerticalAnchor lavaLevel) { this.lavaLevel = lavaLevel; return this; }
		/** a single block id */
		public Config replaceable(Identifier block) { this.replaceable = block.toString(); return this; }

		/** a hash-prefixed block tag, e.g. {@code #minecraft:overworld_carver_replaceables} */
		public Config replaceableTag(Identifier tag) { this.replaceable = "#" + tag; return this; }
		public Config horizontalRadiusMultiplier(FloatProvider value) { this.horizontalRadiusMultiplier = value; return this; }
		public Config verticalRadiusMultiplier(FloatProvider value) { this.verticalRadiusMultiplier = value; return this; }
		public Config floorLevel(FloatProvider value) { this.floorLevel = value; return this; }
		public Config verticalRotation(FloatProvider value) { this.verticalRotation = value; return this; }
		public Config shape(CanyonShape shape) { this.shape = shape; return this; }
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
