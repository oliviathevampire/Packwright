package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class JCampfireRecipe extends JCookingRecipe {
	public static final Codec<JCampfireRecipe> CODEC = buildCodec(JCampfireRecipe::new);

	static {
		JRecipe.register(Identifier.withDefaultNamespace("campfire_cooking"), CODEC);
	}

	JCampfireRecipe(final JIngredient ingredient, final JResult result) {
		super(Identifier.withDefaultNamespace("campfire_cooking"), ingredient, result);
	}

	@Override
	public JCampfireRecipe experience(final float experience) {
		return (JCampfireRecipe) super.experience(experience);
	}

	@Override
	public JCampfireRecipe cookingTime(final int ticks) {
		return (JCampfireRecipe) super.cookingTime(ticks);
	}

	@Override
	public JCampfireRecipe group(final String group) {
		return (JCampfireRecipe) super.group(group);
	}
}
