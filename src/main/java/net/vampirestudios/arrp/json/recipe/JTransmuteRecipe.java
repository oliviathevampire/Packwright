package net.vampirestudios.arrp.json.recipe;

import net.minecraft.resources.Identifier;

public class JTransmuteRecipe extends JResultRecipe {
	private String category;
	private JIngredient input;
	private JIngredient material;

	JTransmuteRecipe(JResult result, String category, JIngredient input, JIngredient material) {
		super(Identifier.withDefaultNamespace("crafting_transmute"), result);

		this.category = category;
		this.input = input;
		this.material = material;
	}

	public JTransmuteRecipe category(String category) {
		this.category = category;
		return this;
	}

	public JTransmuteRecipe input(JIngredient input) {
		this.input = input;
		return this;
	}

	public JTransmuteRecipe material(JIngredient material) {
		this.material = material;
		return this;
	}

	@Override
	public JTransmuteRecipe group(final String group) {
		return (JTransmuteRecipe) super.group(group);
	}

	@Override
	protected JTransmuteRecipe clone() {
		return (JTransmuteRecipe) super.clone();
	}
}
