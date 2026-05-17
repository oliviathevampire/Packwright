package net.vampirestudios.arrp.data.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.assets.models.Model;
import net.minecraft.resources.Identifier;

public class Condition {
	public static final Codec<Condition> CODEC = new Codec<>() {
		@Override public <T> DataResult<T> encode(Condition v, DynamicOps<T> ops, T prefix) {
			// write the parameters object as-is
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, v.parameters).convert(ops).getValue());
		}
		@Override public <T> DataResult<Pair<Condition, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) return DataResult.error(() -> "Loot condition must be an object");
			JsonObject obj = el.getAsJsonObject();
			var tag = obj.get("condition");
			if (tag == null || !tag.isJsonPrimitive() || !tag.getAsJsonPrimitive().isString()) {
				return DataResult.error(() -> "Loot condition missing 'condition' string");
			}
			Condition c = new Condition();
			c.parameters = obj.deepCopy(); // store as-is
			return DataResult.success(Pair.of(c, input));
		}
	};

	private JsonObject parameters = new JsonObject();

	/**
	 * @see LootTable#predicate(String)
	 * @see Model#condition()
	 */
	public Condition(String condition) {
		if (condition != null) {
			this.condition(condition);
		}
	}
	
	public Condition() {
	}

	public Condition condition(String condition) {
		this.parameters.addProperty("condition", condition);
		return this;
	}

	public Condition set(JsonObject parameters) {
		parameters.addProperty("condition", this.parameters.get("condition").getAsString());
		this.parameters = parameters;
		return this;
	}

	public Condition parameter(String key, Number value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public Condition parameter(String key, JsonElement value) {
		this.parameters.add(key, value);
		return this;
	}

	public Condition parameter(String key, Boolean value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public Condition parameter(String key, Character value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public Condition parameter(String key, Identifier value) {
		return parameter(key, value.toString());
	}

	public Condition parameter(String key, String value) {
		return parameter(key, new JsonPrimitive(value));
	}

	/**
	 * "or"'s the conditions together
	 */
	public Condition alternative(Condition... conditions) {
		JsonArray array = new JsonArray();
		for (Condition condition : conditions) {
			array.add(RuntimeResourcePackImpl.GSON.toJsonTree(condition));
		}
		this.parameters.add("terms", array);
		return this;
	}
}
