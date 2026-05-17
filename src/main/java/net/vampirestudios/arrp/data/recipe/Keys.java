package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.vampirestudios.arrp.impl.Codecs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Keys {
	private final Map<String, List<Ingredient>> keys = new LinkedHashMap<>();

	public static final Codec<Keys> CODEC =
			Codec.unboundedMap(Codec.STRING, Codecs.oneOrList(Ingredient.CODEC))
					.xmap(Keys::new, Keys::toMap);

	public Keys() {
	}

	private Keys(Map<String, List<Ingredient>> keys) {
		keys.forEach(this::keyAny);
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
		return this.keyAny(key, List.of(value));
	}

	public Keys keyAny(String key, Ingredient... values) {
		return this.keyAny(key, List.of(values));
	}

	public Keys keyAny(String key, List<Ingredient> values) {
		if (values == null || values.isEmpty()) {
			this.keys.remove(key);
			return this;
		}

		this.keys.put(key, List.copyOf(values));
		return this;
	}

	private Map<String, List<Ingredient>> toMap() {
		return new LinkedHashMap<>(this.keys);
	}

	public Map<String, List<Ingredient>> getKeys() {
		return Map.copyOf(this.keys);
	}
}