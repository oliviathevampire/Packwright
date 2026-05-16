package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JKeys {
	private final Map<String, List<JIngredient>> keys = new LinkedHashMap<>();

	public static final Codec<JKeys> CODEC =
			Codec.unboundedMap(Codec.STRING, Codecs.oneOrList(JIngredient.CODEC))
					.xmap(JKeys::new, JKeys::toMap);

	public JKeys() {
	}

	private JKeys(Map<String, List<JIngredient>> keys) {
		keys.forEach(this::keyAny);
	}

	public static JKeys keys() {
		return new JKeys();
	}

	public JKeys item(String key, Identifier itemId) {
		return this.key(key, JIngredient.ingredient().item(itemId));
	}

	public JKeys item(String key, Item itemId) {
		return this.key(key, JIngredient.ingredient().item(itemId));
	}

	public JKeys tag(String key, Identifier tagId) {
		return this.key(key, JIngredient.ingredient().tag(tagId));
	}

	public JKeys key(String key, JIngredient value) {
		return this.keyAny(key, List.of(value));
	}

	public JKeys keyAny(String key, JIngredient... values) {
		return this.keyAny(key, List.of(values));
	}

	public JKeys keyAny(String key, List<JIngredient> values) {
		if (values == null || values.isEmpty()) {
			this.keys.remove(key);
			return this;
		}

		this.keys.put(key, List.copyOf(values));
		return this;
	}

	private Map<String, List<JIngredient>> toMap() {
		return new LinkedHashMap<>(this.keys);
	}

	public Map<String, List<JIngredient>> getKeys() {
		return Map.copyOf(this.keys);
	}
}