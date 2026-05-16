package net.vampirestudios.arrp.json.recipe;

import net.minecraft.resources.Identifier;

public abstract class JResultRecipe extends JRecipe {
	private final JResult result;

	JResultRecipe(final Identifier type, final JResult result) {
		super(type);
		this.result = result;
	}

	protected JResult getResult() { return result; }

	@Override
	public JResultRecipe group(final String group) {
		return (JResultRecipe) super.group(group);
	}
}
