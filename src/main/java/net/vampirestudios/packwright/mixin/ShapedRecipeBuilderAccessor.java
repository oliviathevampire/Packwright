package net.vampirestudios.packwright.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ShapedRecipeBuilder.class)
public interface ShapedRecipeBuilderAccessor {
	@Accessor
	HolderGetter<Item> getItems();

	@Accessor
	RecipeCategory getCategory();

	@Accessor
	ItemStackTemplate getResult();

	@Accessor
	List<String> getRows();

	@Accessor
	Map<Character, Ingredient> getKey();

	@Accessor
	RecipeUnlockAdvancementBuilder getAdvancementBuilder();

	@Accessor
	String getGroup();

	@Accessor
	boolean isShowNotification();
}
