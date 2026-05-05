package net.vampirestudios.arrp.json.recipe;

import net.minecraft.resources.Identifier;

public class JShapelessRecipe extends JResultRecipe {
	protected final JIngredients ingredients;

	JShapelessRecipe(final JResult result, final JIngredients ingredients) {
		super(Identifier.withDefaultNamespace("crafting_shapeless"), result);

		this.ingredients = ingredients;
	}

	@Override
	public JShapelessRecipe group(final String group) {
		return (JShapelessRecipe) super.group(group);
	}

	@Override
	protected JShapelessRecipe clone() {
		return (JShapelessRecipe) super.clone();
	}
}
