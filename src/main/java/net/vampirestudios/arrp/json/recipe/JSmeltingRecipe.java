package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JSmeltingRecipe extends JCookingRecipe {
	public static final Codec<JSmeltingRecipe> CODEC =
			RecordCodecBuilder.create(instance -> instance.group(
					ingredientCodec(),
					resultCodec(),
					experienceCodec(),
					cookingTimeCodec()
			).apply(instance, (ingredient, result, xp, time) ->
					new JSmeltingRecipe(ingredient, result)
							.experience(xp)
							.cookingTime(time)
			));

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
