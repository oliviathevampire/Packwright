package net.vampirestudios.packwright.data.worldgen.structure;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.Identifier;

import static net.vampirestudios.packwright.util.ResourceHelpers.vanillaId;

public class Structure {

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
		for (String key : json.keySet()) {
			if (!isKnown(key)) {
				JsonElement val = json.get(key);
				if (val != null) this.extra.add(key, val.deepCopy());
			}
		}

		return this;
	}

	// Core setters

	public Structure type(Identifier type) { this.type = type == null ? null : type.toString(); return this; }

	public Structure biomes(JsonElement biomes) {
		this.biomes = biomes == null ? null : biomes.deepCopy();
		return this;
	}

	public Structure biomesId(Identifier id) {
		this.biomes = id == null ? null : new JsonPrimitive(id.toString());
		return this;
	}

	public Structure biomesTag(Identifier tag) {
		if (tag == null) {
			this.biomes = null;
		} else {
			this.biomes = new JsonPrimitive("#" + tag);
		}
		return this;
	}

	public Structure step(String step) { this.step = step; return this; }

	public Structure spawnOverrides(JsonObject overrides) {
		this.spawnOverrides = overrides == null ? null : overrides.deepCopy();
		return this;
	}

	public Structure property(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	public Structure property(String key, String value) {
		return property(key, new JsonPrimitive(value));
	}

	public Structure property(String key, Identifier value) {
		return property(key, value == null ? null : new JsonPrimitive(value.toString()));
	}

	public Structure property(String key, int value) {
		return property(key, new JsonPrimitive(value));
	}

	public Structure property(String key, boolean value) {
		return property(key, new JsonPrimitive(value));
	}

	public Structure extra(String key, JsonElement value) {
		return property(key, value);
	}

	// Convenience helpers for jigsaw-like structures

	public static Structure jigsaw(String startPool) {
		return Structure.structure()
				.type(vanillaId("jigsaw"))
				.property("start_pool", startPool);
	}

	public Structure size(int size) {
		return property("size", size);
	}

	public Structure maxDistanceFromCenter(int v) {
		return property("max_distance_from_center", v);
	}

	/** absolute start height; the game requires a vertical anchor object, not a bare int */
	public Structure startHeightInt(int v) {
		JsonObject anchor = new JsonObject();
		anchor.addProperty("absolute", v);
		return startHeight(anchor);
	}

	/** a height provider or vertical anchor object, e.g. {@code {"absolute": 0}} */
	public Structure startHeight(JsonObject heightProvider) {
		return property("start_height", heightProvider);
	}

	public Structure useExpansionHack(boolean v) {
		return property("use_expansion_hack", v);
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.type != null) out.addProperty("type", this.type);
		if (this.biomes != null) out.add("biomes", this.biomes.deepCopy());
		if (this.step != null) out.addProperty("step", this.step);
		// spawn_overrides is required by the game; default to no overrides
		out.add("spawn_overrides", this.spawnOverrides != null ? this.spawnOverrides.deepCopy() : new JsonObject());
		for (String key : this.extra.keySet()) {
			JsonElement val = this.extra.get(key);
			if (val != null) out.add(key, val.deepCopy());
		}
		return out;
	}

	private static boolean isKnown(String key) {
		return "type".equals(key)
				|| "biomes".equals(key)
				|| "step".equals(key)
				|| "spawn_overrides".equals(key);
	}
}
