package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class JBlastingRecipe extends JCookingRecipe {
	public static final Codec<JBlastingRecipe> CODEC = buildCodec(JBlastingRecipe::new);

	static {
		JRecipe.register(Identifier.withDefaultNamespace("blasting"), CODEC);
	}

	JBlastingRecipe(final JIngredient ingredient, final JResult result) {
		super(Identifier.withDefaultNamespace("blasting"), ingredient, result);
	}

	@Override
	public JBlastingRecipe experience(final float experience) {
		return (JBlastingRecipe) super.experience(experience);
	}

	@Override
	public JBlastingRecipe cookingTime(final int ticks) {
		return (JBlastingRecipe) super.cookingTime(ticks);
	}

	@Override
	public JBlastingRecipe group(final String group) {
		return (JBlastingRecipe) super.group(group);
	}
}
