package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class JSmithingTransformRecipe extends JResultRecipe {
	public static final Codec<JSmithingTransformRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			JIngredient.CODEC.fieldOf("template").forGetter(JSmithingTransformRecipe::getTemplate),
			JIngredient.CODEC.fieldOf("base").forGetter(JSmithingTransformRecipe::getBase),
			JIngredient.CODEC.fieldOf("addition").forGetter(JSmithingTransformRecipe::getAddition),
			JResult.CODEC.fieldOf("result").forGetter(JSmithingTransformRecipe::getResult)
	).apply(i, JSmithingTransformRecipe::new));

	static {
		// Register with your base dispatch: Identifier.CODEC.dispatch("type", …)
		JRecipe.register(Identifier.withDefaultNamespace("smithing_transform"), CODEC);
	}

	private final JIngredient base;
	private final JIngredient addition;
	private final JIngredient template;

	JSmithingTransformRecipe(JIngredient base, JIngredient addition, JIngredient template, JResult result) {
		super(Identifier.withDefaultNamespace("smithing_transform"), result);
		this.base = base;
		this.addition = addition;
		this.template = template;
	}

	public JIngredient getBase() {
		return base;
	}

	public JIngredient getAddition() {
		return addition;
	}

	public JIngredient getTemplate() {
		return template;
	}

	@Override
	protected JSmithingTransformRecipe clone() {
		return (JSmithingTransformRecipe) super.clone();
	}
}
