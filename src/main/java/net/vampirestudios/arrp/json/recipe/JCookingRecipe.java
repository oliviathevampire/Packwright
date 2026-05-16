package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

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

	protected static <T extends JCookingRecipe> RecordCodecBuilder<T, JIngredient> ingredientCodec() {
		return JIngredient.CODEC.fieldOf("ingredient")
				.forGetter(JCookingRecipe::getIngredient);
	}

	protected static <T extends JCookingRecipe> RecordCodecBuilder<T, JResult> resultCodec() {
		return JResult.CODEC.fieldOf("result")
				.forGetter(JCookingRecipe::getResult);
	}

	protected static <T extends JCookingRecipe> RecordCodecBuilder<T, Float> experienceCodec() {
		return Codec.FLOAT.optionalFieldOf("experience", 0.0f)
				.forGetter(recipe -> recipe.getExperience() == null ? 0.0f : recipe.getExperience());
	}

	protected static <T extends JCookingRecipe> RecordCodecBuilder<T, Integer> cookingTimeCodec() {
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
