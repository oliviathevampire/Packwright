package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class ShapelessRecipe extends ResultRecipe {
	public static final Codec<ShapelessRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Result.CODEC.fieldOf("result").forGetter(ShapelessRecipe::getResult),
			Ingredients.CODEC.fieldOf("ingredients").forGetter(ShapelessRecipe::getIngredients),
			Codec.STRING.fieldOf("group").orElse("").forGetter(ShapelessRecipe::getGroup),
			CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapelessRecipe::getCraftingCategory),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(ShapelessRecipe::getShowNotification)
	).apply(instance, (result, ingredients, group, category, showNotification) -> {
		ShapelessRecipe recipe = new ShapelessRecipe(result, ingredients);
		recipe.group(group);
		recipe.category(category);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_shapeless"), CODEC);
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

	public ShapelessRecipe category(final CraftingBookCategory category) {
		return (ShapelessRecipe) super.category(category.getTypeId());
	}

	public CraftingBookCategory getCraftingCategory() {
		return CraftingBookCategory.fromIdOrDefault(getCategory(), CraftingBookCategory.MISC);
	}

	@Override
	public ShapelessRecipe showNotification(final boolean showNotification) {
		return (ShapelessRecipe) super.showNotification(showNotification);
	}

	public Ingredients getIngredients() {
		return ingredients;
	}
}
