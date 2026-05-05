package net.vampirestudios.arrp.json.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JKeys implements Cloneable {
	protected final Map<String, JIngredient> keys;
	protected final Map<String, List<JIngredient>> acceptableKeys;

	JKeys() {
		this.keys = new HashMap<>(9, 1);
		this.acceptableKeys = new HashMap<>();
	}

	public static JKeys keys() {
		return new JKeys();
	}

	public JKeys key(final String key, final JIngredient value) {
		this.keys.put(key, value);

		return this;
	}

	/** Optional sugar: supply multiple acceptable ingredients for a single key. */
	public JKeys keyAny(final String key, final List<JIngredient> values) {
		this.acceptableKeys.put(key, values);
		return this;
	}

	@Override
	protected JKeys clone() {
		try {
			return (JKeys) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	// ---------- Codec ----------
	/**
	 * JSON shape:
	 * {
	 *   "A": { ...JIngredient... },                // single
	 *   "B": [ { ... }, { ... } ]                  // list/any-of
	 * }
	 */
	public static final Codec<JKeys> CODEC =
			Codec.unboundedMap(Codec.STRING, Codecs.oneOrList(JIngredient.CODEC))
					.xmap(JKeys::fromMap, JKeys::toMap)
					.validate(JKeys::validateUnique);

	private static JKeys fromMap(Map<String, List<JIngredient>> in) {
		JKeys out = new JKeys();
		for (var e : in.entrySet()) {
			List<JIngredient> list = e.getValue();
			if (list == null || list.isEmpty()) continue;
			if (list.size() == 1) out.keys.put(e.getKey(), list.getFirst());
			else out.acceptableKeys.put(e.getKey(), List.copyOf(list));
		}
		return out;
	}

	private Map<String, List<JIngredient>> toMap() {
		Map<String, List<JIngredient>> out = new LinkedHashMap<>();
		for (var e : this.keys.entrySet()) out.put(e.getKey(), List.of(e.getValue()));
		for (var e : this.acceptableKeys.entrySet()) out.put(e.getKey(), List.copyOf(e.getValue()));
		return out;
	}

	private static DataResult<JKeys> validateUnique(JKeys k) {
		for (String s : k.keys.keySet()) {
			if (k.acceptableKeys.containsKey(s)) {
				return DataResult.error(() -> "JKeys: key '" + s + "' present as both single and list");
			}
		}
		return DataResult.success(k);
	}

	public static class Serializer implements JsonSerializer<JKeys> {
		@Override
		public JsonElement serialize(final JKeys src, final Type typeOfSrc, final JsonSerializationContext context) {
			final JsonObject object = new JsonObject();

			src.keys.forEach((final String key, final JIngredient ingredient) -> object.add(key,
					context.serialize(ingredient)));
			src.acceptableKeys.forEach((final String key, final List<JIngredient> acceptableIngredients) -> object.add(
					key,
					context.serialize(acceptableIngredients)));

			return object;
		}
	}
}
