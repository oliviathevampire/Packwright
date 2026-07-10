package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class StonecuttingRecipe extends Recipe {
	public static final Codec<StonecuttingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(StonecuttingRecipe::getIngredient),
			Result.CODEC.fieldOf("result").forGetter(StonecuttingRecipe::getResult),
			Codec.INT.optionalFieldOf("count", 1).forGetter(StonecuttingRecipe::getCount)
	).apply(instance, StonecuttingRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("stonecutting"), CODEC);
	}

	private final Ingredient ingredient;
	private final Result result;
	private final int count;

	StonecuttingRecipe(final Ingredient ingredient, final StackedResult result) {
		this(ingredient, result, result.count);
	}

	StonecuttingRecipe(final Ingredient ingredient, final Result result, final int count) {
		super(Identifier.withDefaultNamespace("stonecutting"));
		this.ingredient = ingredient;
		this.result = result;
		this.count = count;
	}

	@Override
	public StonecuttingRecipe group(final String group) {
		return (StonecuttingRecipe) super.group(group);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Result getResult() {
		return result;
	}

	public int getCount() {
		return count;
	}
}