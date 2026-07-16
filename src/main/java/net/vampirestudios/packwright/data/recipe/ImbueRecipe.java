package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_imbue}: a 3x3 recipe that carries a {@code source} item's potion contents over to the result. */
public class ImbueRecipe extends ResultRecipe {
	public static final Codec<ImbueRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("source").forGetter(ImbueRecipe::getSource),
			Ingredient.CODEC.fieldOf("material").forGetter(ImbueRecipe::getMaterial),
			Result.CODEC.fieldOf("result").forGetter(ImbueRecipe::getResult),
			Codec.STRING.fieldOf("group").orElse("").forGetter(ImbueRecipe::getGroup),
			CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ImbueRecipe::getCraftingCategory),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(ImbueRecipe::getShowNotification)
	).apply(instance, (source, material, result, group, category, showNotification) -> {
		ImbueRecipe recipe = new ImbueRecipe(source, material, result);
		recipe.group(group);
		recipe.category(category);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_imbue"), CODEC);
	}

	private final Ingredient source;
	private final Ingredient material;

	ImbueRecipe(Ingredient source, Ingredient material, Result result) {
		super(Identifier.withDefaultNamespace("crafting_imbue"), result);
		this.source = source;
		this.material = material;
	}

	@Override
	public ImbueRecipe group(final String group) {
		return (ImbueRecipe) super.group(group);
	}

	public ImbueRecipe category(final CraftingBookCategory category) {
		return (ImbueRecipe) super.category(category.getTypeId());
	}

	public CraftingBookCategory getCraftingCategory() {
		return CraftingBookCategory.fromIdOrDefault(getCategory(), CraftingBookCategory.MISC);
	}

	@Override
	public ImbueRecipe showNotification(final boolean showNotification) {
		return (ImbueRecipe) super.showNotification(showNotification);
	}

	public Ingredient getSource() {
		return source;
	}

	public Ingredient getMaterial() {
		return material;
	}
}
