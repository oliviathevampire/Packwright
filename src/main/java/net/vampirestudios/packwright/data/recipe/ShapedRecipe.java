package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class ShapedRecipe extends ResultRecipe {
	public static final Codec<ShapedRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Pattern.CODEC.fieldOf("pattern").forGetter(ShapedRecipe::getPattern),
			Keys.CODEC.fieldOf("key").forGetter(ShapedRecipe::getKey),
			Result.CODEC.fieldOf("result").forGetter(ShapedRecipe::getResult),
			Codec.STRING.fieldOf("group").orElse("").forGetter(ShapedRecipe::getGroup),
			CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::getCraftingCategory),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(ShapedRecipe::getShowNotification)
	).apply(instance, (pattern, key, result, group, category, showNotification) -> {
		ShapedRecipe recipe = new ShapedRecipe(pattern, key, result);
		recipe.group(group);
		recipe.category(category);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_shaped"), CODEC);
	}

	protected final Pattern pattern;
	protected final Keys key;

	ShapedRecipe(Pattern pattern, Keys keys, Result result) {
		super(Identifier.withDefaultNamespace("crafting_shaped"), result);

		this.pattern = pattern;
		this.key = keys;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Keys getKey() {
		return key;
	}

	@Override
	public ShapedRecipe group(final String group) {
		return (ShapedRecipe) super.group(group);
	}

	public ShapedRecipe category(final CraftingBookCategory category) {
		return (ShapedRecipe) super.category(category.getTypeId());
	}

	public CraftingBookCategory getCraftingCategory() {
		return CraftingBookCategory.fromIdOrDefault(getCategory(), CraftingBookCategory.MISC);
	}

	@Override
	public ShapedRecipe showNotification(final boolean showNotification) {
		return (ShapedRecipe) super.showNotification(showNotification);
	}
}
