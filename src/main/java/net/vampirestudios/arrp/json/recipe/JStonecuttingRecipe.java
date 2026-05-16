package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JStonecuttingRecipe extends JRecipe {
	public static final Codec<JStonecuttingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			JIngredient.CODEC.fieldOf("ingredient").forGetter(JStonecuttingRecipe::getIngredient),
			JResult.CODEC.fieldOf("result").forGetter(JStonecuttingRecipe::getResult),
			Codec.INT.optionalFieldOf("count", 1).forGetter(JStonecuttingRecipe::getCount)
	).apply(instance, JStonecuttingRecipe::new));

	static {
		JRecipe.register(Identifier.withDefaultNamespace("stonecutting"), CODEC);
	}

	private final JIngredient ingredient;
	private final JResult result;
	private final int count;

	JStonecuttingRecipe(final JIngredient ingredient, final JStackedResult result) {
		this(ingredient, result, result.count);
	}

	JStonecuttingRecipe(final JIngredient ingredient, final JResult result, final int count) {
		super(Identifier.withDefaultNamespace("stonecutting"));
		this.ingredient = ingredient;
		this.result = result;
		this.count = count;
	}

	@Override
	public JStonecuttingRecipe group(final String group) {
		return (JStonecuttingRecipe) super.group(group);
	}

	public JIngredient getIngredient() {
		return ingredient;
	}

	public JResult getResult() {
		return result;
	}

	public int getCount() {
		return count;
	}
}