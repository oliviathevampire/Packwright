package net.vampirestudios.arrp.json.recipe;

import net.minecraft.resources.Identifier;

public class JStonecuttingRecipe extends JRecipe {
	private final JIngredient ingredient;
	private final Identifier result;
	private final int count;

	JStonecuttingRecipe(final JIngredient ingredient, final JStackedResult result) {
		super(Identifier.withDefaultNamespace("stonecutting"));
		this.ingredient = ingredient;
		this.result = result.item;
		this.count = result.count;
	}

	@Override
	public JStonecuttingRecipe group(final String group) {
		return (JStonecuttingRecipe) super.group(group);
	}

	@Override
	protected JStonecuttingRecipe clone() {
		return (JStonecuttingRecipe) super.clone();
	}
}
