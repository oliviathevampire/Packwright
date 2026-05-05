package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.json.codec.Codecs;
import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class JRecipe implements Cloneable {
	private static final Map<Identifier, Codec<? extends JRecipe>> REGISTRY = new ConcurrentHashMap<>();
	public static final Codec<JRecipe> CODEC = Codecs.tagged("type", JRecipe::getType, REGISTRY::get, Identifier.CODEC);

	protected final Identifier type;
	protected String group;

	JRecipe(final Identifier type) {
		this.type = type;
	}

	public static <R extends JRecipe> void register(Identifier type, Codec<R> codec) {
		REGISTRY.put(type, codec);
	}

	/** Back-compat alias: old `smithing(...)` now means smithing_transform. */
	@Deprecated
	public static JSmithingTransformRecipe smithing(final JIngredient base, final JIngredient addition, final JIngredient template, final JResult result) {
		return smithingTransform(base, addition, template, result);
	}

	public static JSmithingTransformRecipe smithingTransform(
			final JIngredient base, final JIngredient addition, final JIngredient template, final JResult result) {
		return new JSmithingTransformRecipe(base, addition, template, result);
	}

	/** Convenience: default base to tag #minecraft:trimmable_armor. */
	public static JSmithingTrimRecipe smithingTrim(final JIngredient addition, final JIngredient template) {
		return new JSmithingTrimRecipe(
				JIngredient.ingredient().tag(Identifier.withDefaultNamespace("trimmable_armor")),
				addition, template
		);
	}

	/** Explicit trim with a given base ingredient. */
	public static JSmithingTrimRecipe smithingTrim(final JIngredient base, final JIngredient addition, final JIngredient template) {
		return new JSmithingTrimRecipe(base, addition, template);
	}

	public static JStonecuttingRecipe stonecutting(final JIngredient ingredient, final JStackedResult result) {
		return new JStonecuttingRecipe(ingredient, result);
	}

	// crafting

	public static JShapedRecipe shaped(final JPattern pattern, final JKeys keys, final JResult result) {
		return new JShapedRecipe(pattern, keys, result);
	}

	public static JShapelessRecipe shapeless(final JIngredients ingredients, final JResult result) {
		return new JShapelessRecipe(result, ingredients);
	}

	// cooking

	public static JBlastingRecipe blasting(final JIngredient ingredient, final JResult result) {
		return new JBlastingRecipe(ingredient, result);
	}

	public static JSmeltingRecipe smelting(final JIngredient ingredient, final JResult result) {
		return new JSmeltingRecipe(ingredient, result);
	}

	public static JCampfireRecipe campfire(final JIngredient ingredient, final JResult result) {
		return new JCampfireRecipe(ingredient, result);
	}

	public static JSmokingRecipe smoking(final JIngredient ingredient, final JResult result) {
		return new JSmokingRecipe(ingredient, result);
	}

	public JRecipe group(final String group) {
		this.group = group;

		return this;
	}

	public Identifier getType() {
		return type;
	}

	public String getGroup() {
		return group;
	}

	@Override
	protected JRecipe clone() {
		try {
			return (JRecipe) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}
