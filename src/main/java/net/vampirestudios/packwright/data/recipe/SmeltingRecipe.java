package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class SmeltingRecipe extends CookingRecipe {
	public static final Codec<SmeltingRecipe> CODEC = buildCodec(SmeltingRecipe::new);

	static {
		Recipe.register(Identifier.withDefaultNamespace("smelting"), CODEC);
	}

	SmeltingRecipe(final Ingredient ingredient, final Result result) {
		super(Identifier.withDefaultNamespace("smelting"), ingredient, result);
	}

	@Override
	public SmeltingRecipe experience(final float experience) {
		return (SmeltingRecipe) super.experience(experience);
	}

	@Override
	public SmeltingRecipe cookingTime(final int ticks) {
		return (SmeltingRecipe) super.cookingTime(ticks);
	}

	@Override
	public SmeltingRecipe group(final String group) {
		return (SmeltingRecipe) super.group(group);
	}
}
