package net.vampirestudios.arrp.json.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.vampirestudios.arrp.impl.RuntimeResourcePackImpl;
import net.vampirestudios.arrp.json.models.JModel;
import net.minecraft.resources.Identifier;

public class JCondition {
	public static final Codec<JCondition> CODEC = new Codec<>() {
		@Override public <T> DataResult<T> encode(JCondition v, DynamicOps<T> ops, T prefix) {
			// write the parameters object as-is
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, v.parameters).convert(ops).getValue());
		}
		@Override public <T> DataResult<Pair<JCondition, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) return DataResult.error(() -> "Loot condition must be an object");
			JsonObject obj = el.getAsJsonObject();
			var tag = obj.get("condition");
			if (tag == null || !tag.isJsonPrimitive() || !tag.getAsJsonPrimitive().isString()) {
				return DataResult.error(() -> "Loot condition missing 'condition' string");
			}
			JCondition c = new JCondition();
			c.parameters = obj.deepCopy(); // store as-is
			return DataResult.success(Pair.of(c, input));
		}
	};

	private JsonObject parameters = new JsonObject();

	/**
	 * @see JLootTable#predicate(String)
	 * @see JModel#condition()
	 */
	public JCondition(String condition) {
		if (condition != null) {
			this.condition(condition);
		}
	}
	
	public JCondition() {
	}

	public JCondition condition(String condition) {
		this.parameters.addProperty("condition", condition);
		return this;
	}

	public JCondition set(JsonObject parameters) {
		parameters.addProperty("condition", this.parameters.get("condition").getAsString());
		this.parameters = parameters;
		return this;
	}

	public JCondition parameter(String key, Number value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public JCondition parameter(String key, JsonElement value) {
		this.parameters.add(key, value);
		return this;
	}

	public JCondition parameter(String key, Boolean value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public JCondition parameter(String key, Character value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public JCondition parameter(String key, Identifier value) {
		return parameter(key, value.toString());
	}

	public JCondition parameter(String key, String value) {
		return parameter(key, new JsonPrimitive(value));
	}

	/**
	 * "or"'s the conditions together
	 */
	public JCondition alternative(JCondition... conditions) {
		JsonArray array = new JsonArray();
		for (JCondition condition : conditions) {
			array.add(RuntimeResourcePackImpl.GSON.toJsonTree(condition));
		}
		this.parameters.add("terms", array);
		return this;
	}
}
