package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class BlastingRecipe extends CookingRecipe {
	public static final Codec<BlastingRecipe> CODEC = buildCodec(BlastingRecipe::new);

	static {
		Recipe.register(Identifier.withDefaultNamespace("blasting"), CODEC);
	}

	BlastingRecipe(final Ingredient ingredient, final Result result) {
		super(Identifier.withDefaultNamespace("blasting"), ingredient, result);
	}

	@Override
	public BlastingRecipe experience(final float experience) {
		return (BlastingRecipe) super.experience(experience);
	}

	@Override
	public BlastingRecipe cookingTime(final int ticks) {
		return (BlastingRecipe) super.cookingTime(ticks);
	}

	@Override
	public BlastingRecipe group(final String group) {
		return (BlastingRecipe) super.group(group);
	}
}
