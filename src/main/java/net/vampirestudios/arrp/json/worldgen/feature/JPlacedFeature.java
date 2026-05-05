package net.vampirestudios.arrp.json.worldgen.feature;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

public class JPlacedFeature implements Cloneable {

	public static final Codec<JPlacedFeature> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(JPlacedFeature feature, DynamicOps<T> ops, T prefix) {
			JsonObject json = feature.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<JPlacedFeature, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Placed feature must be an object");
			}
			return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
		}
	};

	private JsonElement feature;     // string or object
	private JsonArray placement;     // array of modifiers
	private final JsonObject extra = new JsonObject();

	public static JPlacedFeature placed() {
		return new JPlacedFeature();
	}

	public static JPlacedFeature placed(String featureId) {
		return new JPlacedFeature().featureId(featureId);
	}

	public static JPlacedFeature fromJson(JsonObject json) {
		return new JPlacedFeature().json(json);
	}

	public JPlacedFeature json(JsonObject json) {
		this.feature = null;
		this.placement = null;
		this.extra.entrySet().clear();

		if (json == null) return this;

		if (json.has("feature")) {
			JsonElement f = json.get("feature");
			this.feature = f == null ? null : f.deepCopy();
		}
		if (json.has("placement") && json.get("placement").isJsonArray()) {
			this.placement = json.getAsJsonArray("placement").deepCopy();
		}

		for (String key : json.keySet()) {
			if (!isKnown(key)) {
				JsonElement val = json.get(key);
				if (val != null) this.extra.add(key, val.deepCopy());
			}
		}

		return this;
	}

	// Core setters

	public JPlacedFeature feature(JsonElement feature) {
		this.feature = feature == null ? null : feature.deepCopy();
		return this;
	}

	public JPlacedFeature featureId(String id) {
		if (id == null) {
			this.feature = null;
		} else {
			this.feature = new JsonPrimitive(id);
		}
		return this;
	}

	public JPlacedFeature placement(JsonArray placement) {
		this.placement = placement == null ? null : placement.deepCopy();
		return this;
	}

	public JPlacedFeature extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers (placement modifiers)

	private JsonArray ensurePlacement() {
		if (this.placement == null) this.placement = new JsonArray();
		return this.placement;
	}

	public JPlacedFeature count(int count) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:count");
		obj.addProperty("count", count);
		ensurePlacement().add(obj);
		return this;
	}

	public JPlacedFeature inSquare() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:in_square");
		ensurePlacement().add(obj);
		return this;
	}

	public JPlacedFeature heightmap(String heightmap) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:heightmap");
		obj.addProperty("heightmap", heightmap);
		ensurePlacement().add(obj);
		return this;
	}

	public JPlacedFeature biomeFilter() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:biome");
		ensurePlacement().add(obj);
		return this;
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.feature != null) out.add("feature", this.feature.deepCopy());
		if (this.placement != null) out.add("placement", this.placement.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement val = this.extra.get(key);
			if (val != null) out.add(key, val.deepCopy());
		}
		return out;
	}

	@Override
	public JPlacedFeature clone() {
		try {
			JPlacedFeature clone = (JPlacedFeature) super.clone();
			clone.feature = this.feature == null ? null : this.feature.deepCopy();
			clone.placement = this.placement == null ? null : this.placement.deepCopy();
			clone.extra.entrySet().clear();
			for (String key : this.extra.keySet()) {
				JsonElement val = this.extra.get(key);
				if (val != null) clone.extra.add(key, val.deepCopy());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	private static boolean isKnown(String key) {
		return "feature".equals(key) || "placement".equals(key);
	}
}
