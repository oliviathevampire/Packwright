package net.vampirestudios.packwright.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * A carver in the {@code worldgen/carver} registry. All configuration fields are inlined in the
 * root object; {@code replaceable}, {@code lava_level} and {@code debug_settings} no longer exist.
 * <p>
 * Cave-specific fields: {@link #count}, {@link #thickness}, {@link #roomVerticalRadiusMultiplier}
 * (formerly {@code yScale}), {@link #startVerticalRadiusMultiplier}, {@link #weirdThicknessBias}.
 * Canyon-specific fields: {@link #shape} (whose {@code y_scale} replaced the old top-level
 * {@code yScale}) and {@link #verticalRotation}.
 */
public class Carver {
	public static final Codec<Carver> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("type").forGetter(x -> x.type),
			Codec.FLOAT.fieldOf("probability").forGetter(x -> x.probability),
			HeightProvider.CODEC.fieldOf("y").forGetter(x -> x.y),
			IntProvider.CODEC.optionalFieldOf("count").forGetter(x -> Optional.ofNullable(x.count)),
			FloatProvider.CODEC.optionalFieldOf("thickness").forGetter(x -> Optional.ofNullable(x.thickness)),
			FloatProvider.CODEC.optionalFieldOf("room_vertical_radius_multiplier").forGetter(x -> Optional.ofNullable(x.roomVerticalRadiusMultiplier)),
			FloatProvider.CODEC.optionalFieldOf("start_vertical_radius_multiplier").forGetter(x -> Optional.ofNullable(x.startVerticalRadiusMultiplier)),
			Codec.BOOL.optionalFieldOf("weird_thickness_bias").forGetter(x -> Optional.ofNullable(x.weirdThicknessBias)),
			FloatProvider.CODEC.optionalFieldOf("horizontal_radius_multiplier").forGetter(x -> Optional.ofNullable(x.horizontalRadiusMultiplier)),
			FloatProvider.CODEC.optionalFieldOf("vertical_radius_multiplier").forGetter(x -> Optional.ofNullable(x.verticalRadiusMultiplier)),
			FloatProvider.CODEC.optionalFieldOf("floor_level").forGetter(x -> Optional.ofNullable(x.floorLevel)),
			FloatProvider.CODEC.optionalFieldOf("vertical_rotation").forGetter(x -> Optional.ofNullable(x.verticalRotation)),
			CanyonShape.CODEC.optionalFieldOf("shape").forGetter(x -> Optional.ofNullable(x.shape))
	).apply(i, (type, probability, y, count, thickness, roomVertical, startVertical, weirdThicknessBias,
				horizontal, vertical, floor, rotation, shape) -> {
		Carver out = new Carver();
		out.type = type;
		out.probability = probability;
		out.y = y;
		out.count = count.orElse(null);
		out.thickness = thickness.orElse(null);
		out.roomVerticalRadiusMultiplier = roomVertical.orElse(null);
		out.startVerticalRadiusMultiplier = startVertical.orElse(null);
		out.weirdThicknessBias = weirdThicknessBias.orElse(null);
		out.horizontalRadiusMultiplier = horizontal.orElse(null);
		out.verticalRadiusMultiplier = vertical.orElse(null);
		out.floorLevel = floor.orElse(null);
		out.verticalRotation = rotation.orElse(null);
		out.shape = shape.orElse(null);
		return out;
	}));

	private Identifier type = Identifier.withDefaultNamespace("cave");
	private float probability = 0.1F;
	private HeightProvider y = HeightProvider.uniform(VerticalAnchor.absolute(8), VerticalAnchor.absolute(180));
	private IntProvider count;
	private FloatProvider thickness;
	private FloatProvider roomVerticalRadiusMultiplier;
	private FloatProvider startVerticalRadiusMultiplier;
	private Boolean weirdThicknessBias;
	private FloatProvider horizontalRadiusMultiplier;
	private FloatProvider verticalRadiusMultiplier;
	private FloatProvider floorLevel;
	private FloatProvider verticalRotation;
	private CanyonShape shape;

	public static Carver carver() {
		return new Carver();
	}

	/**
	 * a cave carver prefilled with vanilla-like values for the fields the game requires:
	 * {@code count}, {@code thickness}, {@code room_vertical_radius_multiplier},
	 * {@code horizontal_radius_multiplier}, {@code vertical_radius_multiplier} and
	 * {@code floor_level}
	 */
	public static Carver cave() {
		return carver().type("minecraft:cave")
				.count(IntProvider.biasedToBottom(1, 4))
				.thickness(FloatProvider.uniform(1.0F, 2.0F))
				.roomVerticalRadiusMultiplier(FloatProvider.constant(1.0F))
				.horizontalRadiusMultiplier(FloatProvider.uniform(0.7F, 1.4F))
				.verticalRadiusMultiplier(FloatProvider.uniform(0.8F, 1.3F))
				.floorLevel(FloatProvider.uniform(-1.0F, -0.4F));
	}

	public static Carver canyon() {
		return carver().type("minecraft:canyon");
	}

	public Carver type(Identifier type) { this.type = type; return this; }
	public Carver type(String type) { return type(Identifier.tryParse(type)); }
	public Carver probability(float probability) { this.probability = probability; return this; }
	public Carver y(HeightProvider y) { this.y = y; return this; }
	public Carver count(IntProvider count) { this.count = count; return this; }
	public Carver thickness(FloatProvider thickness) { this.thickness = thickness; return this; }
	public Carver roomVerticalRadiusMultiplier(FloatProvider value) { this.roomVerticalRadiusMultiplier = value; return this; }
	public Carver startVerticalRadiusMultiplier(FloatProvider value) { this.startVerticalRadiusMultiplier = value; return this; }
	public Carver weirdThicknessBias(boolean value) { this.weirdThicknessBias = value; return this; }
	public Carver horizontalRadiusMultiplier(FloatProvider value) { this.horizontalRadiusMultiplier = value; return this; }
	public Carver verticalRadiusMultiplier(FloatProvider value) { this.verticalRadiusMultiplier = value; return this; }
	public Carver floorLevel(FloatProvider value) { this.floorLevel = value; return this; }
	public Carver verticalRotation(FloatProvider value) { this.verticalRotation = value; return this; }
	public Carver shape(CanyonShape shape) { this.shape = shape; return this; }

	public Identifier getType() { return type; }
	public float getProbability() { return probability; }
	public HeightProvider getY() { return y; }
	public IntProvider getCount() { return count; }
	public FloatProvider getThickness() { return thickness; }
	public FloatProvider getRoomVerticalRadiusMultiplier() { return roomVerticalRadiusMultiplier; }
	public FloatProvider getStartVerticalRadiusMultiplier() { return startVerticalRadiusMultiplier; }
	public Boolean getWeirdThicknessBias() { return weirdThicknessBias; }
	public FloatProvider getHorizontalRadiusMultiplier() { return horizontalRadiusMultiplier; }
	public FloatProvider getVerticalRadiusMultiplier() { return verticalRadiusMultiplier; }
	public FloatProvider getFloorLevel() { return floorLevel; }
	public FloatProvider getVerticalRotation() { return verticalRotation; }
	public CanyonShape getShape() { return shape; }

	public static class CanyonShape {
		public static final Codec<CanyonShape> CODEC = RecordCodecBuilder.create(i -> i.group(
				FloatProvider.CODEC.fieldOf("distance_factor").forGetter(x -> x.distanceFactor),
				FloatProvider.CODEC.fieldOf("horizontal_radius_factor").forGetter(x -> x.horizontalRadiusFactor),
				FloatProvider.CODEC.fieldOf("thickness").forGetter(x -> x.thickness),
				FloatProvider.CODEC.optionalFieldOf("y_scale").forGetter(x -> Optional.ofNullable(x.yScale)),
				Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(x -> x.verticalRadiusCenterFactor),
				Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(x -> x.verticalRadiusDefaultFactor),
				Codec.INT.fieldOf("width_smoothness").forGetter(x -> x.widthSmoothness)
		).apply(i, (distanceFactor, horizontalRadiusFactor, thickness, yScale, verticalRadiusCenterFactor, verticalRadiusDefaultFactor, widthSmoothness) ->
				new CanyonShape()
						.distanceFactor(distanceFactor)
						.horizontalRadiusFactor(horizontalRadiusFactor)
						.thickness(thickness)
						.yScale(yScale.orElse(null))
						.verticalRadiusCenterFactor(verticalRadiusCenterFactor)
						.verticalRadiusDefaultFactor(verticalRadiusDefaultFactor)
						.widthSmoothness(widthSmoothness)));

		private FloatProvider distanceFactor = FloatProvider.uniform(0.75F, 1.0F);
		private FloatProvider horizontalRadiusFactor = FloatProvider.uniform(0.75F, 1.0F);
		private FloatProvider thickness = FloatProvider.trapezoid(0.0F, 6.0F, 2.0F);
		private FloatProvider yScale;
		private float verticalRadiusCenterFactor = 0.0F;
		private float verticalRadiusDefaultFactor = 1.0F;
		private int widthSmoothness = 3;

		public static CanyonShape canyonShape() { return new CanyonShape(); }
		public CanyonShape distanceFactor(FloatProvider distanceFactor) { this.distanceFactor = distanceFactor; return this; }
		public CanyonShape horizontalRadiusFactor(FloatProvider horizontalRadiusFactor) { this.horizontalRadiusFactor = horizontalRadiusFactor; return this; }
		public CanyonShape thickness(FloatProvider thickness) { this.thickness = thickness; return this; }
		/** replaces the pre-26.3 top-level {@code yScale} field */
		public CanyonShape yScale(FloatProvider yScale) { this.yScale = yScale; return this; }
		public CanyonShape yScale(float yScale) { return yScale(FloatProvider.constant(yScale)); }
		public CanyonShape verticalRadiusCenterFactor(float verticalRadiusCenterFactor) { this.verticalRadiusCenterFactor = verticalRadiusCenterFactor; return this; }
		public CanyonShape verticalRadiusDefaultFactor(float verticalRadiusDefaultFactor) { this.verticalRadiusDefaultFactor = verticalRadiusDefaultFactor; return this; }
		public CanyonShape widthSmoothness(int widthSmoothness) { this.widthSmoothness = widthSmoothness; return this; }
	}
}
