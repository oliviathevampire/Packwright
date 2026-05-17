package net.vampirestudios.arrp.data.worldgen.structure;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

public class Structure implements Cloneable {

	public static final Codec<Structure> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(Structure structure, DynamicOps<T> ops, T prefix) {
			JsonObject json = structure.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<Structure, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Structure must be an object");
			}
			return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
		}
	};

	private String type;
	private JsonElement biomes;          // string or tag
	private String step;
	private JsonObject spawnOverrides;
	private JsonObject settings;         // structure-specific settings
	private final JsonObject extra = new JsonObject();

	public static Structure structure() {
		return new Structure();
	}

	public static Structure fromJson(JsonObject json) {
		return new Structure().json(json);
	}

	public Structure json(JsonObject json) {
		this.type = null;
		this.biomes = null;
		this.step = null;
		this.spawnOverrides = null;
		this.settings = null;
		this.extra.entrySet().clear();

		if (json == null) return this;

		if (json.has("type") && json.get("type").isJsonPrimitive()) {
			this.type = json.get("type").getAsString();
		}
		if (json.has("biomes")) {
			this.biomes = json.get("biomes").deepCopy();
		}
		if (json.has("step") && json.get("step").isJsonPrimitive()) {
			this.step = json.get("step").getAsString();
		}
		if (json.has("spawn_overrides") && json.get("spawn_overrides").isJsonObject()) {
			this.spawnOverrides = json.getAsJsonObject("spawn_overrides").deepCopy();
		}
		if (json.has("settings") && json.get("settings").isJsonObject()) {
			this.settings = json.getAsJsonObject("settings").deepCopy();
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

	public Structure type(String type) { this.type = type; return this; }

	public Structure biomes(JsonElement biomes) {
		this.biomes = biomes == null ? null : biomes.deepCopy();
		return this;
	}

	public Structure biomesId(String id) {
		this.biomes = id == null ? null : new JsonPrimitive(id);
		return this;
	}

	public Structure biomesTag(String tag) {
		if (tag == null) {
			this.biomes = null;
		} else {
			if (!tag.startsWith("#")) tag = "#" + tag;
			this.biomes = new JsonPrimitive(tag);
		}
		return this;
	}

	public Structure step(String step) { this.step = step; return this; }

	public Structure spawnOverrides(JsonObject overrides) {
		this.spawnOverrides = overrides == null ? null : overrides.deepCopy();
		return this;
	}

	public Structure settings(JsonObject settings) {
		this.settings = settings == null ? null : settings.deepCopy();
		return this;
	}

	public Structure extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers for jigsaw-like structures

	public static Structure jigsaw(String startPool) {
		Structure s = Structure.structure().type("minecraft:jigsaw");
		JsonObject settings = new JsonObject();
		settings.addProperty("start_pool", startPool);
		s.settings(settings);
		return s;
	}

	private JsonObject ensureSettings() {
		if (this.settings == null) this.settings = new JsonObject();
		return this.settings;
	}

	public Structure size(int size) {
		ensureSettings().addProperty("size", size);
		return this;
	}

	public Structure maxDistanceFromCenter(int v) {
		ensureSettings().addProperty("max_distance_from_center", v);
		return this;
	}

	public Structure startHeightInt(int v) {
		ensureSettings().addProperty("start_height", v);
		return this;
	}

	public Structure useExpansionHack(boolean v) {
		ensureSettings().addProperty("use_expansion_hack", v);
		return this;
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.type != null) out.addProperty("type", this.type);
		if (this.biomes != null) out.add("biomes", this.biomes.deepCopy());
		if (this.step != null) out.addProperty("step", this.step);
		if (this.spawnOverrides != null) out.add("spawn_overrides", this.spawnOverrides.deepCopy());
		if (this.settings != null) out.add("settings", this.settings.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement val = this.extra.get(key);
			if (val != null) out.add(key, val.deepCopy());
		}
		return out;
	}

	@Override
	public Structure clone() {
		try {
			Structure clone = (Structure) super.clone();
			clone.biomes = this.biomes == null ? null : this.biomes.deepCopy();
			clone.spawnOverrides = this.spawnOverrides == null ? null : this.spawnOverrides.deepCopy();
			clone.settings = this.settings == null ? null : this.settings.deepCopy();
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
		return "type".equals(key)
				|| "biomes".equals(key)
				|| "step".equals(key)
				|| "spawn_overrides".equals(key)
				|| "settings".equals(key);
	}
}
