package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public final class SmithingTransformRecipe extends ResultRecipe {
	public static final Codec<SmithingTransformRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.CODEC.optionalFieldOf("template").forGetter(SmithingTransformRecipe::getTemplate),
			Ingredient.CODEC.fieldOf("base").forGetter(SmithingTransformRecipe::getBase),
			Ingredient.CODEC.optionalFieldOf("addition").forGetter(SmithingTransformRecipe::getAddition),
			Result.CODEC.fieldOf("result").forGetter(SmithingTransformRecipe::getResult),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(SmithingTransformRecipe::getShowNotification)
	).apply(i, (template, base, addition, result, showNotification) -> {
		SmithingTransformRecipe recipe = new SmithingTransformRecipe(base, addition, template, result);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("smithing_transform"), CODEC);
	}

	private final Ingredient base;
	private final Optional<Ingredient> addition;
	private final Optional<Ingredient> template;

	SmithingTransformRecipe(Ingredient base, Optional<Ingredient> addition, Optional<Ingredient> template, Result result) {
		super(Identifier.withDefaultNamespace("smithing_transform"), result);
		this.base = base;
		this.addition = addition;
		this.template = template;
	}

	/** Convenience for callers passing plain (possibly-null) ingredients rather than Optionals. */
	SmithingTransformRecipe(Ingredient base, Ingredient addition, Ingredient template, Result result) {
		this(base, Optional.ofNullable(addition), Optional.ofNullable(template), result);
	}

	@Override
	public SmithingTransformRecipe showNotification(final boolean showNotification) {
		return (SmithingTransformRecipe) super.showNotification(showNotification);
	}

	public Ingredient getBase() {
		return base;
	}

	public Optional<Ingredient> getAddition() {
		return addition;
	}

	public Optional<Ingredient> getTemplate() {
		return template;
	}
}
