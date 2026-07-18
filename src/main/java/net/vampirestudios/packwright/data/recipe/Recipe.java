package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.impl.Codecs;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Recipe {
	private static final Map<Identifier, Codec<? extends Recipe>> REGISTRY = new ConcurrentHashMap<>();
	public static final Codec<Recipe> CODEC = Codecs.tagged("type", Recipe::getType, REGISTRY::get, Identifier.CODEC);

	protected final Identifier type;
	protected String group = "";
	/** raw serialized book-category id; interpreted via {@link CraftingBookCategory} or {@link CookingBookCategory} depending on recipe kind */
	protected String category = "misc";
	protected boolean showNotification = true;

	protected Recipe(final Identifier type) {
		this.type = type;
	}

	static <R extends Recipe> void register(Identifier type, Codec<R> codec) {
		REGISTRY.put(type, codec);
	}

	/**
	 * Register a custom recipe type so it participates in {@link #CODEC} dispatching.
	 * Call this once at mod init before any serialization occurs.
	 */
	public static <R extends Recipe> void registerCustom(Identifier type, Codec<R> codec) {
		REGISTRY.put(type, codec);
	}

	public static SmithingTransformRecipe smithingTransform(
			final Ingredient base, final Ingredient addition, final Ingredient template, final Result result) {
		return new SmithingTransformRecipe(base, addition, template, result);
	}

	/** Convenience: default base to tag #minecraft:trimmable_armor. */
	public static SmithingTrimRecipe smithingTrim(final Ingredient addition, final Ingredient template, final Identifier pattern) {
		return new SmithingTrimRecipe(
				Ingredient.ingredient().tag(Identifier.withDefaultNamespace("trimmable_armor")),
				addition, template, pattern
		);
	}

	/** Explicit trim with a given base ingredient. */
	public static SmithingTrimRecipe smithingTrim(final Ingredient base, final Ingredient addition, final Ingredient template, final Identifier pattern) {
		return new SmithingTrimRecipe(base, addition, template, pattern);
	}

	public static StonecuttingRecipe stonecutting(final Ingredient ingredient, final StackedResult result) {
		return new StonecuttingRecipe(ingredient, result);
	}

	// crafting

	public static ShapedRecipe shaped(final Pattern pattern, final Keys keys, final Result result) {
		return new ShapedRecipe(pattern, keys, result);
	}

	public static ShapelessRecipe shapeless(final Ingredients ingredients, final Result result) {
		return new ShapelessRecipe(result, ingredients);
	}

	// cooking

	public static BlastingRecipe blasting(final Ingredient ingredient, final Result result) {
		return new BlastingRecipe(ingredient, result);
	}

	public static SmeltingRecipe smelting(final Ingredient ingredient, final Result result) {
		return new SmeltingRecipe(ingredient, result);
	}

	public static CampfireRecipe campfire(final Ingredient ingredient, final Result result) {
		return new CampfireRecipe(ingredient, result);
	}

	public static SmokingRecipe smoking(final Ingredient ingredient, final Result result) {
		return new SmokingRecipe(ingredient, result);
	}

	public static TransmuteRecipe transmute(final Ingredient ingredient, final Ingredient ingredient2, final Result result) {
		return new TransmuteRecipe(result, ingredient, ingredient2);
	}

	public static DyeRecipe dye(final Ingredient target, final Ingredient dye, final Result result) {
		return new DyeRecipe(target, dye, result);
	}

	public static ImbueRecipe imbue(final Ingredient source, final Ingredient material, final Result result) {
		return new ImbueRecipe(source, material, result);
	}

	/** Convenience: same ingredient used on all four wall positions. */
	public static DecoratedPotRecipe decoratedPot(final Ingredient wallPattern, final Result result) {
		return new DecoratedPotRecipe(wallPattern, wallPattern, wallPattern, wallPattern, result);
	}

	public static DecoratedPotRecipe decoratedPot(
			final Ingredient back, final Ingredient left, final Ingredient right, final Ingredient front, final Result result) {
		return new DecoratedPotRecipe(back, left, right, front, result);
	}

	public static BookCloningRecipe bookCloning(final Ingredient source, final Ingredient material, final Result result) {
		return new BookCloningRecipe(source, material, result);
	}

	public static MapExtendingRecipe mapExtending(final Ingredient map, final Ingredient material, final Result result) {
		return new MapExtendingRecipe(map, material, result);
	}

	public static FireworkRocketRecipe fireworkRocket(
			final Ingredient shell, final Ingredient fuel, final Ingredient star, final Result result) {
		return new FireworkRocketRecipe(shell, fuel, star, result);
	}

	public static FireworkStarRecipe fireworkStar(
			final Ingredient trail, final Ingredient twinkle, final Ingredient fuel, final Ingredient dye, final Result result) {
		return new FireworkStarRecipe(trail, twinkle, fuel, dye, result);
	}

	public static FireworkStarFadeRecipe fireworkStarFade(final Ingredient target, final Ingredient dye, final Result result) {
		return new FireworkStarFadeRecipe(target, dye, result);
	}

	public static BannerDuplicateRecipe bannerDuplicate(final Ingredient banner, final Result result) {
		return new BannerDuplicateRecipe(banner, result);
	}

	public static ShieldDecorationRecipe shieldDecoration(final Ingredient banner, final Ingredient target, final Result result) {
		return new ShieldDecorationRecipe(banner, target, result);
	}

	/** the crafting-table item-repair recipe; has no configurable fields */
	public static RepairItemRecipe repairItem() {
		return RepairItemRecipe.INSTANCE;
	}

	public Recipe group(final String group) {
		this.group = group;
		return this;
	}

	/** raw escape hatch; prefer the typed {@code category(CraftingBookCategory)}/{@code category(CookingBookCategory)} overloads on individual recipe types */
	public Recipe category(String category) {
		this.category = category;
		return this;
	}

	public Recipe showNotification(final boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	public Identifier getType() {
		return type;
	}

	public String getGroup() {
		return group;
	}

	public String getCategory() {
		return category;
	}

	public boolean getShowNotification() {
		return showNotification;
	}
}
