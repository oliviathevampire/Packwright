package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class SmokingRecipe extends CookingRecipe {
	public static final Codec<SmokingRecipe> CODEC = buildCodec(SmokingRecipe::new);

	static {
		Recipe.register(Identifier.withDefaultNamespace("smoking"), CODEC);
	}
	SmokingRecipe(final Ingredient ingredient, final Result result) {
		super(Identifier.withDefaultNamespace("smoking"), ingredient, result);
	}

	@Override
	public SmokingRecipe experience(final float experience) {
		return (SmokingRecipe) super.experience(experience);
	}

	@Override
	public SmokingRecipe cookingTime(final int ticks) {
		return (SmokingRecipe) super.cookingTime(ticks);
	}

	@Override
	public SmokingRecipe group(final String group) {
		return (SmokingRecipe) super.group(group);
	}

	@Override
	public SmokingRecipe category(final CookingBookCategory category) {
		return (SmokingRecipe) super.category(category);
	}

	@Override
	public SmokingRecipe showNotification(final boolean showNotification) {
		return (SmokingRecipe) super.showNotification(showNotification);
	}
}
