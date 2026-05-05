package net.vampirestudios.arrp.json.recipe;

import net.minecraft.resources.Identifier;

public class JSmeltingRecipe extends JCookingRecipe {
	JSmeltingRecipe(final JIngredient ingredient, final JResult result) {
		super(Identifier.withDefaultNamespace("smelting"), ingredient, result);
	}

	@Override
	public JSmeltingRecipe experience(final float experience) {
		return (JSmeltingRecipe) super.experience(experience);
	}

	@Override
	public JSmeltingRecipe cookingTime(final int ticks) {
		return (JSmeltingRecipe) super.cookingTime(ticks);
	}

	@Override
	public JSmeltingRecipe group(final String group) {
		return (JSmeltingRecipe) super.group(group);
	}

	@Override
	protected JSmeltingRecipe clone() {
		return (JSmeltingRecipe) super.clone();
	}
}
