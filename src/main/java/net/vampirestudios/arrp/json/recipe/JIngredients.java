package net.vampirestudios.arrp.json.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JIngredients {
	public static final Codec<JIngredients> CODEC = JIngredient.CODEC.listOf()
			.xmap(JIngredients::new, JIngredients::getIngredients)
			.validate(ingredients -> ingredients.isEmpty()
					? DataResult.error(() -> "JIngredients cannot be empty")
					: DataResult.success(ingredients));

	private final List<JIngredient> ingredients = new ArrayList<>();

	public JIngredients() {
	}

	public JIngredients(List<JIngredient> ingredients) {
		this.addAll(ingredients);
	}

	public static JIngredients ingredients() {
		return new JIngredients();
	}

	public static JIngredients ingredients(JIngredient... ingredients) {
		return new JIngredients().addAll(ingredients);
	}

	public JIngredients add(JIngredient ingredient) {
		if (ingredient != null) {
			this.ingredients.add(ingredient);
		}

		return this;
	}

	public JIngredients addAll(JIngredient... ingredients) {
		for (JIngredient ingredient : ingredients) {
			this.add(ingredient);
		}

		return this;
	}

	public JIngredients addAll(List<JIngredient> ingredients) {
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

	public List<JIngredient> getIngredients() {
		return List.copyOf(this.ingredients);
	}

	public JIngredients addItem(Identifier itemId) {
		return this.add(JIngredient.ingredient().item(itemId));
	}

	public JIngredients addFabricCustom(Identifier type, Consumer<JsonObject> data) {
		return this.add(JIngredient.fabricComponents(type, data));
	}

	public JIngredients addItem(Item itemId) {
		return this.add(JIngredient.ingredient().item(itemId));
	}

	public JIngredients addTag(Identifier tagId) {
		return this.add(JIngredient.ingredient().tag(tagId));
	}

	public JIngredients remove(JIngredient ingredient) {
		this.ingredients.remove(ingredient);
		return this;
	}

	public JIngredients clear() {
		this.ingredients.clear();
		return this;
	}

	public boolean contains(JIngredient ingredient) {
		return this.ingredients.contains(ingredient);
	}
}