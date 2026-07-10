package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.function.BiFunction;

public abstract class CookingRecipe extends Recipe {
	private final Ingredient ingredient;
	private final Result result;

	private Float experience;
	private Integer cookingtime;

	CookingRecipe(final Identifier type, final Ingredient ingredient, final Result result) {
		super(type);

		this.ingredient = ingredient;
		this.result = result;
	}

	/** Builds a codec for any cooking recipe subtype, given its two-arg constructor. */
	protected static <T extends CookingRecipe> Codec<T> buildCodec(BiFunction<Ingredient, Result, T> ctor) {
		return RecordCodecBuilder.create(instance -> instance.group(
				ingredientCodec(),
				resultCodec(),
				experienceCodec(),
				cookingTimeCodec()
		).apply(instance, (ingredient, result, xp, time) -> {
			T obj = ctor.apply(ingredient, result);
			obj.experience(xp);
			obj.cookingTime(time);
			return obj;
		}));
	}

	private static <T extends CookingRecipe> RecordCodecBuilder<T, Ingredient> ingredientCodec() {
		return Ingredient.CODEC.fieldOf("ingredient")
				.forGetter(CookingRecipe::getIngredient);
	}

	private static <T extends CookingRecipe> RecordCodecBuilder<T, Result> resultCodec() {
		return Result.CODEC.fieldOf("result")
				.forGetter(CookingRecipe::getResult);
	}

	private static <T extends CookingRecipe> RecordCodecBuilder<T, Float> experienceCodec() {
		return Codec.FLOAT.optionalFieldOf("experience", 0.0f)
				.forGetter(recipe -> recipe.getExperience() == null ? 0.0f : recipe.getExperience());
	}

	private static <T extends CookingRecipe> RecordCodecBuilder<T, Integer> cookingTimeCodec() {
		return Codec.INT.optionalFieldOf("cookingtime", 200)
				.forGetter(recipe -> recipe.getCookingtime() == null ? 200 : recipe.getCookingtime());
	}

	public CookingRecipe experience(final float experience) {
		this.experience = experience;

		return this;
	}

	public CookingRecipe cookingTime(final int ticks) {
		this.cookingtime = ticks;

		return this;
	}

	@Override
	public CookingRecipe group(final String group) {
		return (CookingRecipe) super.group(group);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Result getResult() {
		return result;
	}

	public Float getExperience() {
		return experience;
	}

	public Integer getCookingtime() {
		return cookingtime;
	}
}
