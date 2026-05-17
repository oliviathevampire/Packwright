package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class CampfireRecipe extends CookingRecipe {
	public static final Codec<CampfireRecipe> CODEC = buildCodec(CampfireRecipe::new);

	static {
		Recipe.register(Identifier.withDefaultNamespace("campfire_cooking"), CODEC);
	}

	CampfireRecipe(final Ingredient ingredient, final Result result) {
		super(Identifier.withDefaultNamespace("campfire_cooking"), ingredient, result);
	}

	@Override
	public CampfireRecipe experience(final float experience) {
		return (CampfireRecipe) super.experience(experience);
	}

	@Override
	public CampfireRecipe cookingTime(final int ticks) {
		return (CampfireRecipe) super.cookingTime(ticks);
	}

	@Override
	public CampfireRecipe group(final String group) {
		return (CampfireRecipe) super.group(group);
	}
}
