package net.vampirestudios.packwright.data.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Ingredients {
	public static final Codec<Ingredients> CODEC = Ingredient.CODEC.listOf()
			.xmap(Ingredients::new, Ingredients::getIngredients)
			.validate(ingredients -> ingredients.isEmpty()
					? DataResult.error(() -> "Ingredients cannot be empty")
					: DataResult.success(ingredients));

	private final List<Ingredient> ingredients = new ArrayList<>();

	public Ingredients() {
	}

	public Ingredients(List<Ingredient> ingredients) {
		this.addAll(ingredients);
	}

	public static Ingredients ingredients() {
		return new Ingredients();
	}

	public static Ingredients ingredients(Ingredient... ingredients) {
		return new Ingredients().addAll(ingredients);
	}

	public Ingredients add(Ingredient ingredient) {
		if (ingredient != null) {
			this.ingredients.add(ingredient);
		}

		return this;
	}

	public Ingredients addAll(Ingredient... ingredients) {
		for (Ingredient ingredient : ingredients) {
			this.add(ingredient);
		}

		return this;
	}

	public Ingredients addAll(List<Ingredient> ingredients) {
		if (ingredients != null) {
			ingredients.forEach(this::add);
		}

		return this;
	}

	public boolean isEmpty() {
		return this.ingredients.isEmpty();
	}

	public int size() {
		return this.ingredients.size();
	}

	public List<Ingredient> getIngredients() {
		return List.copyOf(this.ingredients);
	}

	public Ingredients addItem(Identifier itemId) {
		return this.add(Ingredient.ingredient().item(itemId));
	}

	public Ingredients addFabricCustom(Identifier type, Consumer<JsonObject> data) {
		return this.add(Ingredient.fabricComponents(type, data));
	}

	public Ingredients addItem(Item itemId) {
		return this.add(Ingredient.ingredient().item(itemId));
	}

	public Ingredients addTag(Identifier tagId) {
		return this.add(Ingredient.ingredient().tag(tagId));
	}

	public Ingredients remove(Ingredient ingredient) {
		this.ingredients.remove(ingredient);
		return this;
	}

	public Ingredients clear() {
		this.ingredients.clear();
		return this;
	}

	public boolean contains(Ingredient ingredient) {
		return this.ingredients.contains(ingredient);
	}
}