package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class StonecuttingRecipe extends Recipe {
	// vanilla StonecutterRecipe's codec is just { ingredient, result, show_notification } —
	// there is no top-level "count"; a result's count is expressed via the Result object itself.
	public static final Codec<StonecuttingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(StonecuttingRecipe::getIngredient),
			Result.CODEC.fieldOf("result").forGetter(StonecuttingRecipe::getResult),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(StonecuttingRecipe::getShowNotification)
	).apply(instance, (ingredient, result, showNotification) -> {
		StonecuttingRecipe recipe = new StonecuttingRecipe(ingredient, result);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("stonecutting"), CODEC);
	}

	private final Ingredient ingredient;
	private final Result result;

	StonecuttingRecipe(final Ingredient ingredient, final Result result) {
		super(Identifier.withDefaultNamespace("stonecutting"));
		this.ingredient = ingredient;
		this.result = result;
	}

	@Override
	public StonecuttingRecipe group(final String group) {
		return (StonecuttingRecipe) super.group(group);
	}

	@Override
	public StonecuttingRecipe showNotification(final boolean showNotification) {
		return (StonecuttingRecipe) super.showNotification(showNotification);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Result getResult() {
		return result;
	}
}
