package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.impl.Codecs;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Recipe {
	private static final Map<Identifier, Codec<? extends Recipe>> REGISTRY = new ConcurrentHashMap<>();
	public static final Codec<Recipe> CODEC = Codecs.tagged("type", Recipe::getType, REGISTRY::get, Identifier.CODEC);

	protected final Identifier type;
	protected String group;
	protected String category;

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

	@Deprecated
	public static SmithingTransformRecipe smithing(final Ingredient base, final Ingredient addition, final Ingredient template, final Result result) {
		return smithingTransform(base, addition, template, result);
	}

	public static SmithingTransformRecipe smithingTransform(
			final Ingredient base, final Ingredient addition, final Ingredient template, final Result result) {
		return new SmithingTransformRecipe(base, addition, template, result);
	}

	/** Convenience: default base to tag #minecraft:trimmable_armor. */
	public static SmithingTrimRecipe smithingTrim(final Ingredient addition, final Ingredient template) {
		return new SmithingTrimRecipe(
				Ingredient.ingredient().tag(Identifier.withDefaultNamespace("trimmable_armor")),
				addition, template
		);
	}

	/** Explicit trim with a given base ingredient. */
	public static SmithingTrimRecipe smithingTrim(final Ingredient base, final Ingredient addition, final Ingredient template) {
		return new SmithingTrimRecipe(base, addition, template);
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

	public Recipe group(final String group) {
		this.group = group;
		return this;
	}

	public Recipe category(String category) {
		this.category = category;
		return this;
	}

	public Recipe category(SmeltingTypes category) {
		return this.category(category.getTypeId());
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
}
