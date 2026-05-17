package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.function.BiFunction;

public abstract class JCookingRecipe extends JRecipe {
	private final JIngredient ingredient;
	private final JResult result;

	private Float experience;
	private Integer cookingtime;

	JCookingRecipe(final Identifier type, final JIngredient ingredient, final JResult result) {
		super(type);

		this.ingredient = ingredient;
		this.result = result;
	}

	/** Builds a codec for any cooking recipe subtype, given its two-arg constructor. */
	protected static <T extends JCookingRecipe> Codec<T> buildCodec(BiFunction<JIngredient, JResult, T> ctor) {
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

	private static <T extends JCookingRecipe> RecordCodecBuilder<T, JIngredient> ingredientCodec() {
		return JIngredient.CODEC.fieldOf("ingredient")
				.forGetter(JCookingRecipe::getIngredient);
	}

	private static <T extends JCookingRecipe> RecordCodecBuilder<T, JResult> resultCodec() {
		return JResult.CODEC.fieldOf("result")
				.forGetter(JCookingRecipe::getResult);
	}

	private static <T extends JCookingRecipe> RecordCodecBuilder<T, Float> experienceCodec() {
		return Codec.FLOAT.optionalFieldOf("experience", 0.0f)
				.forGetter(recipe -> recipe.getExperience() == null ? 0.0f : recipe.getExperience());
	}

	private static <T extends JCookingRecipe> RecordCodecBuilder<T, Integer> cookingTimeCodec() {
		return Codec.INT.optionalFieldOf("cookingtime", 200)
				.forGetter(recipe -> recipe.getCookingtime() == null ? 200 : recipe.getCookingtime());
	}

	public JCookingRecipe experience(final float experience) {
		this.experience = experience;

		return this;
	}

	public JCookingRecipe cookingTime(final int ticks) {
		this.cookingtime = ticks;

		return this;
	}

	@Override
	public JCookingRecipe group(final String group) {
		return (JCookingRecipe) super.group(group);
	}

	public JIngredient getIngredient() {
		return ingredient;
	}

	public JResult getResult() {
		return result;
	}

	public Float getExperience() {
		return experience;
	}

	public Integer getCookingtime() {
		return cookingtime;
	}
}
