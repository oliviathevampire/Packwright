package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class SmithingTrimRecipe extends Recipe {
	// Codec: { "template", "base", "addition" }  (no "result")
	public static final Codec<SmithingTrimRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.CODEC.fieldOf("template").forGetter(SmithingTrimRecipe::getTemplate),
			Ingredient.CODEC.fieldOf("base").forGetter(SmithingTrimRecipe::getBase),
			Ingredient.CODEC.fieldOf("addition").forGetter(SmithingTrimRecipe::getAddition)
	).apply(i, SmithingTrimRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("smithing_trim"), CODEC);
	}

	private final Ingredient base;
	private final Ingredient addition;
	private final Ingredient template;

	SmithingTrimRecipe(Ingredient base, Ingredient addition, Ingredient template) {
		super(Identifier.withDefaultNamespace("smithing_trim"));
		this.base = base;
		this.addition = addition;
		this.template = template;
	}

	public Ingredient getBase() {
		return base;
	}

	public Ingredient getAddition() {
		return addition;
	}

	public Ingredient getTemplate() {
		return template;
	}
}
