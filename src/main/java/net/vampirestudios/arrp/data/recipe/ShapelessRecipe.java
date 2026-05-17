package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class ShapelessRecipe extends ResultRecipe {
	public static final Codec<ShapelessRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Result.CODEC.fieldOf("result").forGetter(ShapelessRecipe::getResult),
			Ingredients.CODEC.fieldOf("ingredients").forGetter(ShapelessRecipe::getIngredients)
	).apply(instance, ShapelessRecipe::new));

	static {
		Recipe.register(Identifier.parse("crafting_shapeless"), CODEC);
	}

	protected final Ingredients ingredients;

	ShapelessRecipe(final Result result, final Ingredients ingredients) {
		super(Identifier.withDefaultNamespace("crafting_shapeless"), result);
		this.ingredients = ingredients;
	}

	@Override
	public ShapelessRecipe group(final String group) {
		return (ShapelessRecipe) super.group(group);
	}

	public Ingredients getIngredients() {
		return ingredients;
	}
}
