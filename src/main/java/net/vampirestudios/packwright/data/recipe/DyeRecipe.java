package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_dye}: dyes a {@code target} item with one or more {@code dye} items. */
public class DyeRecipe extends ResultRecipe {
	public static final Codec<DyeRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("target").forGetter(DyeRecipe::getTarget),
			Ingredient.CODEC.fieldOf("dye").forGetter(DyeRecipe::getDye),
			Result.CODEC.fieldOf("result").forGetter(DyeRecipe::getResult),
			Codec.STRING.fieldOf("group").orElse("").forGetter(DyeRecipe::getGroup),
			CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(DyeRecipe::getCraftingCategory),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(DyeRecipe::getShowNotification)
	).apply(instance, (target, dye, result, group, category, showNotification) -> {
		DyeRecipe recipe = new DyeRecipe(target, dye, result);
		recipe.group(group);
		recipe.category(category);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_dye"), CODEC);
	}

	private final Ingredient target;
	private final Ingredient dye;

	DyeRecipe(Ingredient target, Ingredient dye, Result result) {
		super(Identifier.withDefaultNamespace("crafting_dye"), result);
		this.target = target;
		this.dye = dye;
	}

	@Override
	public DyeRecipe group(final String group) {
		return (DyeRecipe) super.group(group);
	}

	public DyeRecipe category(final CraftingBookCategory category) {
		return (DyeRecipe) super.category(category.getTypeId());
	}

	public CraftingBookCategory getCraftingCategory() {
		return CraftingBookCategory.fromIdOrDefault(getCategory(), CraftingBookCategory.MISC);
	}

	@Override
	public DyeRecipe showNotification(final boolean showNotification) {
		return (DyeRecipe) super.showNotification(showNotification);
	}

	public Ingredient getTarget() {
		return target;
	}

	public Ingredient getDye() {
		return dye;
	}
}
