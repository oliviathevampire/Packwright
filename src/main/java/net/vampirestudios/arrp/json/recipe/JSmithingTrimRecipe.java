package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class JSmithingTrimRecipe extends JRecipe {
	// Codec: { "template", "base", "addition" }  (no "result")
	public static final Codec<JSmithingTrimRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			JIngredient.CODEC.fieldOf("template").forGetter(JSmithingTrimRecipe::getTemplate),
			JIngredient.CODEC.fieldOf("base").forGetter(JSmithingTrimRecipe::getBase),
			JIngredient.CODEC.fieldOf("addition").forGetter(JSmithingTrimRecipe::getAddition)
	).apply(i, JSmithingTrimRecipe::new));

	static {
		JRecipe.register(Identifier.withDefaultNamespace("smithing_trim"), CODEC);
	}

	private final JIngredient base;
	private final JIngredient addition;
	private final JIngredient template;

	JSmithingTrimRecipe(JIngredient base, JIngredient addition, JIngredient template) {
		super(Identifier.withDefaultNamespace("smithing_trim"));
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
}
