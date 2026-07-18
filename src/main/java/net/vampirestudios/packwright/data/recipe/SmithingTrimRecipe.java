package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public final class SmithingTrimRecipe extends Recipe {
	// Codec: { "template", "base", "addition", "pattern", "show_notification" }  (no "result")
	public static final Codec<SmithingTrimRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.CODEC.fieldOf("template").forGetter(SmithingTrimRecipe::getTemplate),
			Ingredient.CODEC.fieldOf("base").forGetter(SmithingTrimRecipe::getBase),
			Ingredient.CODEC.fieldOf("addition").forGetter(SmithingTrimRecipe::getAddition),
			Identifier.CODEC.fieldOf("pattern").forGetter(SmithingTrimRecipe::getPattern),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(SmithingTrimRecipe::getShowNotification)
	).apply(i, (template, base, addition, pattern, showNotification) -> {
		SmithingTrimRecipe recipe = new SmithingTrimRecipe(base, addition, template, pattern);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("smithing_trim"), CODEC);
	}

	private final Ingredient base;
	private final Ingredient addition;
	private final Ingredient template;
	/** id of the {@code trim_pattern} registry entry to apply */
	private final Identifier pattern;

	SmithingTrimRecipe(Ingredient base, Ingredient addition, Ingredient template, Identifier pattern) {
		super(Identifier.withDefaultNamespace("smithing_trim"));
		this.base = base;
		this.addition = addition;
		this.template = template;
		this.pattern = pattern;
	}

	@Override
	public SmithingTrimRecipe showNotification(final boolean showNotification) {
		return (SmithingTrimRecipe) super.showNotification(showNotification);
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

	public Identifier getPattern() {
		return pattern;
	}
}
