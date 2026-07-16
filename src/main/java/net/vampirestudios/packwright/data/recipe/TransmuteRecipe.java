package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class TransmuteRecipe extends ResultRecipe {
	public static final Codec<TransmuteRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Result.CODEC.fieldOf("result").forGetter(TransmuteRecipe::getResult),
			Ingredient.CODEC.fieldOf("input").forGetter(TransmuteRecipe::getInput),
			Ingredient.CODEC.fieldOf("material").forGetter(TransmuteRecipe::getMaterial),
			IntRange.CODEC.fieldOf("material_count").orElse(IntRange.exactly(1)).forGetter(TransmuteRecipe::getMaterialCount),
			Codec.BOOL.fieldOf("add_material_count_to_result").orElse(false).forGetter(TransmuteRecipe::getAddMaterialCountToResult),
			Codec.STRING.fieldOf("group").orElse("").forGetter(TransmuteRecipe::getGroup),
			CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(TransmuteRecipe::getCraftingCategory),
			Codec.BOOL.fieldOf("show_notification").orElse(true).forGetter(TransmuteRecipe::getShowNotification)
	).apply(instance, (result, input, material, materialCount, addMaterialCountToResult, group, category, showNotification) -> {
		TransmuteRecipe recipe = new TransmuteRecipe(result, input, material);
		recipe.materialCount(materialCount);
		recipe.addMaterialCountToResult(addMaterialCountToResult);
		recipe.group(group);
		recipe.category(category);
		recipe.showNotification(showNotification);
		return recipe;
	}));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_transmute"), CODEC);
	}

	private Ingredient input;
	private Ingredient material;
	private IntRange materialCount = IntRange.exactly(1);
	private boolean addMaterialCountToResult = false;

	TransmuteRecipe(Result result, Ingredient input, Ingredient material) {
		super(Identifier.withDefaultNamespace("crafting_transmute"), result);
		this.input = input;
		this.material = material;
	}

	public TransmuteRecipe input(Ingredient input) {
		this.input = input;
		return this;
	}

	public TransmuteRecipe material(Ingredient material) {
		this.material = material;
		return this;
	}

	/** how many stacks of {@code material} may be consumed alongside {@code input}; defaults to exactly 1 */
	public TransmuteRecipe materialCount(IntRange materialCount) {
		this.materialCount = materialCount;
		return this;
	}

	public TransmuteRecipe addMaterialCountToResult(boolean addMaterialCountToResult) {
		this.addMaterialCountToResult = addMaterialCountToResult;
		return this;
	}

	@Override
	public TransmuteRecipe group(final String group) {
		return (TransmuteRecipe) super.group(group);
	}

	public TransmuteRecipe category(final CraftingBookCategory category) {
		return (TransmuteRecipe) super.category(category.getTypeId());
	}

	public CraftingBookCategory getCraftingCategory() {
		return CraftingBookCategory.fromIdOrDefault(getCategory(), CraftingBookCategory.MISC);
	}

	@Override
	public TransmuteRecipe showNotification(final boolean showNotification) {
		return (TransmuteRecipe) super.showNotification(showNotification);
	}

	public Ingredient getInput() {
		return input;
	}

	public Ingredient getMaterial() {
		return material;
	}

	public IntRange getMaterialCount() {
		return materialCount;
	}

	public boolean getAddMaterialCountToResult() {
		return addMaterialCountToResult;
	}
}
