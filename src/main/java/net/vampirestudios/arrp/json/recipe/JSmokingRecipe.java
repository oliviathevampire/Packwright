package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class JSmokingRecipe extends JCookingRecipe {
	public static final Codec<JSmokingRecipe> CODEC = buildCodec(JSmokingRecipe::new);

	static {
		JRecipe.register(Identifier.withDefaultNamespace("smoking"), CODEC);
	}
	JSmokingRecipe(final JIngredient ingredient, final JResult result) {
		super(Identifier.withDefaultNamespace("smoking"), ingredient, result);
	}

	@Override
	public JSmokingRecipe experience(final float experience) {
		return (JSmokingRecipe) super.experience(experience);
	}

	@Override
	public JSmokingRecipe cookingTime(final int ticks) {
		return (JSmokingRecipe) super.cookingTime(ticks);
	}

	@Override
	public JSmokingRecipe group(final String group) {
		return (JSmokingRecipe) super.group(group);
	}
}
