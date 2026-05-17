package net.vampirestudios.arrp.data.recipe;

import net.minecraft.resources.Identifier;

public abstract class ResultRecipe extends Recipe {
	private final Result result;

	ResultRecipe(final Identifier type, final Result result) {
		super(type);
		this.result = result;
	}

	protected Result getResult() { return result; }

	@Override
	public ResultRecipe group(final String group) {
		return (ResultRecipe) super.group(group);
	}
}
