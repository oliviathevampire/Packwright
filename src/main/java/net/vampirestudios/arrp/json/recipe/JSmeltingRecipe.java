package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class JSmeltingRecipe extends JCookingRecipe {
	public static final Codec<JSmeltingRecipe> CODEC = buildCodec(JSmeltingRecipe::new);

	static {
		JRecipe.register(Identifier.withDefaultNamespace("smelting"), CODEC);
	}

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
}
