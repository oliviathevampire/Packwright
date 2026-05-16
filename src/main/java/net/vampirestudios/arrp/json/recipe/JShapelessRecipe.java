package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JShapelessRecipe extends JResultRecipe {
	public static final Codec<JShapelessRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			JResult.CODEC.fieldOf("result").forGetter(JShapelessRecipe::getResult),
			JIngredients.CODEC.fieldOf("ingredients").forGetter(JShapelessRecipe::getIngredients)
	).apply(instance, JShapelessRecipe::new));

	static {
		JRecipe.register(Identifier.parse("crafting_shapeless"), CODEC);
	}

	protected final JIngredients ingredients;

	JShapelessRecipe(final JResult result, final JIngredients ingredients) {
		super(Identifier.withDefaultNamespace("crafting_shapeless"), result);
		this.ingredients = ingredients;
	}

	@Override
	public JShapelessRecipe group(final String group) {
		return (JShapelessRecipe) super.group(group);
	}

	public JIngredients getIngredients() {
		return ingredients;
	}
}
