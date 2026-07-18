package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/** {@code minecraft:crafting_special_firework_star}: gunpowder + dye (+ optional shape/trail/twinkle items) -> firework star. */
public class FireworkStarRecipe extends Recipe {
	public static final Codec<FireworkStarRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.unboundedMap(Shape.CODEC, Ingredient.CODEC).fieldOf("shapes").forGetter(FireworkStarRecipe::getShapes),
			Ingredient.CODEC.fieldOf("trail").forGetter(FireworkStarRecipe::getTrail),
			Ingredient.CODEC.fieldOf("twinkle").forGetter(FireworkStarRecipe::getTwinkle),
			Ingredient.CODEC.fieldOf("fuel").forGetter(FireworkStarRecipe::getFuel),
			Ingredient.CODEC.fieldOf("dye").forGetter(FireworkStarRecipe::getDye),
			Result.CODEC.fieldOf("result").forGetter(FireworkStarRecipe::getResult)
	).apply(instance, FireworkStarRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_firework_star"), CODEC);
	}

	private final Map<Shape, Ingredient> shapes;
	private final Ingredient trail;
	private final Ingredient twinkle;
	private final Ingredient fuel;
	private final Ingredient dye;
	private final Result result;

	FireworkStarRecipe(Map<Shape, Ingredient> shapes, Ingredient trail, Ingredient twinkle, Ingredient fuel, Ingredient dye, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_firework_star"));
		// EnumMap(Map) requires a non-empty source map unless it's already an EnumMap, so build via putAll instead.
		this.shapes = new EnumMap<>(Shape.class);
		this.shapes.putAll(shapes);
		this.trail = trail;
		this.twinkle = twinkle;
		this.fuel = fuel;
		this.dye = dye;
		this.result = result;
	}

	FireworkStarRecipe(Ingredient trail, Ingredient twinkle, Ingredient fuel, Ingredient dye, Result result) {
		this(Map.of(), trail, twinkle, fuel, dye, result);
	}

	/** the item that selects the given explosion shape (in addition to the default small ball) */
	public FireworkStarRecipe shape(Shape shape, Ingredient ingredient) {
		this.shapes.put(shape, ingredient);
		return this;
	}

	public Map<Shape, Ingredient> getShapes() {
		return Map.copyOf(shapes);
	}

	public Ingredient getTrail() {
		return trail;
	}

	public Ingredient getTwinkle() {
		return twinkle;
	}

	public Ingredient getFuel() {
		return fuel;
	}

	public Ingredient getDye() {
		return dye;
	}

	public Result getResult() {
		return result;
	}

	/** mirrors vanilla's {@code FireworkExplosion.Shape} */
	public enum Shape {
		SMALL_BALL("small_ball"),
		LARGE_BALL("large_ball"),
		STAR("star"),
		CREEPER("creeper"),
		BURST("burst");

		public static final Codec<Shape> CODEC = Codec.STRING.comapFlatMap(
				id -> Arrays.stream(values())
						.filter(shape -> shape.id.equals(id))
						.findFirst()
						.map(DataResult::success)
						.orElseGet(() -> DataResult.error(() -> "Unknown firework explosion shape: " + id)),
				Shape::getId
		);

		private final String id;

		Shape(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}
}
