package net.vampirestudios.arrp.data.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LootFunction {
	public static final Codec<LootFunction> CODEC = new Codec<>() {
		@Override public <T> DataResult<T> encode(LootFunction v, DynamicOps<T> ops, T prefix) {
			var builder = ops.mapBuilder();

			// dump properties object
			T props = new Dynamic<>(JsonOps.INSTANCE, v.properties).convert(ops).getValue();
			var mapRes = ops.getMap(props);
			if (mapRes.result().isEmpty()) return DataResult.error(() -> "function properties not an object");
			for (Pair<T, T> e : mapRes.result().get().entries().toList()) {
				builder.add(e.getFirst(), e.getSecond());
			}

			if (!v.conditions.isEmpty()) {
				var conds = Condition.CODEC.listOf().encodeStart(ops, v.conditions);
				if (conds.result().isEmpty()) return conds;
				builder.add(ops.createString("conditions"), conds.result().get());
			}
			return builder.build(prefix);
		}

		@Override public <T> DataResult<Pair<LootFunction, T>> decode(DynamicOps<T> ops, T input) {
			// convert full object to JSON, then peel off "conditions"
			var el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) return DataResult.error(() -> "loot function must be an object");
			JsonObject obj = el.getAsJsonObject();

			var fnEl = obj.get("function");
			if (fnEl == null || !fnEl.isJsonPrimitive()) return DataResult.error(() -> "missing 'function'");
			String fn = fnEl.getAsString();

			// extract conditions if present
			List<Condition> conds = java.util.List.of();
			var condEl = obj.remove("conditions");
			if (condEl != null) {
				var parsed = Condition.CODEC.listOf().parse(JsonOps.INSTANCE, condEl);
				if (parsed.result().isEmpty()) return DataResult.error(() -> "bad function.conditions");
				conds = parsed.result().get();
			}

			LootFunction f = new LootFunction(fn);
			f.properties = obj;               // store remaining properties intact
			f.conditions.addAll(conds);       // fill list
			return DataResult.success(Pair.of(f, input));
		}
	};
	private final List<Condition> conditions = new ArrayList<>();
	private JsonObject properties = new JsonObject();

	/**
	 * @see LootTable#function(String)
	 */
	public LootFunction(String function) {
		function(function);
	}

	public LootFunction function(String function) {
		this.properties.addProperty("function", function);
		return this;
	}

	public LootFunction set(JsonObject properties) {
		properties.addProperty("function",this.properties.get("function").getAsString());
		this.properties = properties;
		return this;
	}

	public LootFunction parameter(String key, JsonElement value) {
		this.properties.add(key, value);
		return this;
	}

	public LootFunction parameter(String key, String value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public LootFunction parameter(String key, Number value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public LootFunction parameter(String key, Boolean value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public LootFunction parameter(String key, Identifier value) {
		return parameter(key, value.toString());
	}

	public LootFunction parameter(String key, Character value) {
		return parameter(key, new JsonPrimitive(value));
	}

	public LootFunction condition(Condition condition) {
		this.conditions.add(condition);
		return this;
	}
}
