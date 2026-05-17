package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class SmithingTransformRecipe extends ResultRecipe {
	public static final Codec<SmithingTransformRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.CODEC.fieldOf("template").forGetter(SmithingTransformRecipe::getTemplate),
			Ingredient.CODEC.fieldOf("base").forGetter(SmithingTransformRecipe::getBase),
			Ingredient.CODEC.fieldOf("addition").forGetter(SmithingTransformRecipe::getAddition),
			Result.CODEC.fieldOf("result").forGetter(SmithingTransformRecipe::getResult)
	).apply(i, SmithingTransformRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("smithing_transform"), CODEC);
	}

	private final Ingredient base;
	private final Ingredient addition;
	private final Ingredient template;

	SmithingTransformRecipe(Ingredient base, Ingredient addition, Ingredient template, Result result) {
		super(Identifier.withDefaultNamespace("smithing_transform"), result);
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
