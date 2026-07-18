package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Keys {
	private final Map<String, Ingredient> keys = new LinkedHashMap<>();

	/**
	 * Vanilla's {@code ShapedRecipePattern.Data} maps each pattern symbol to exactly one
	 * {@link Ingredient} value (which may itself be a single item, a tag, or an item list) —
	 * not an array of ingredient objects.
	 */
	public static final Codec<Keys> CODEC =
			Codec.unboundedMap(Codec.STRING, Ingredient.CODEC)
					.xmap(Keys::new, Keys::toMap);

	public Keys() {
	}

	private Keys(Map<String, Ingredient> keys) {
		keys.forEach(this::key);
	}

	public static Keys keys() {
		return new Keys();
	}

	public Keys item(String key, Identifier itemId) {
		return this.key(key, Ingredient.ingredient().item(itemId));
	}

	public Keys item(String key, Item itemId) {
		return this.key(key, Ingredient.ingredient().item(itemId));
	}

	public Keys tag(String key, Identifier tagId) {
		return this.key(key, Ingredient.ingredient().tag(tagId));
	}

	public Keys key(String key, Ingredient value) {
		if (value == null || !value.isDefined()) {
			this.keys.remove(key);
			return this;
		}

		this.keys.put(key, value);
		return this;
	}

	/**
	 * Several alternative ingredients for a single symbol, folded into one
	 * tag/list-based {@link Ingredient} (vanilla's key map value is a single Ingredient,
	 * which can itself hold multiple alternative items).
	 */
	public Keys keyAny(String key, Ingredient... values) {
		return this.keyAny(key, values == null ? List.of() : List.of(values));
	}

	public Keys keyAny(String key, List<Ingredient> values) {
		if (values == null || values.isEmpty()) {
			this.keys.remove(key);
			return this;
		}

		return values.size() == 1
				? this.key(key, values.getFirst())
				: this.key(key, Ingredient.alternative(values.toArray(new Ingredient[0])));
	}

	private Map<String, Ingredient> toMap() {
		return new LinkedHashMap<>(this.keys);
	}

	public Map<String, Ingredient> getKeys() {
		return Map.copyOf(this.keys);
	}
}
