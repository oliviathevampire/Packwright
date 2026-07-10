package net.vampirestudios.packwright.data.worldgen.structure;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.Identifier;

public class StructureSet {

	public static final Codec<StructureSet> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(StructureSet set, DynamicOps<T> ops, T prefix) {
			JsonObject json = set.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<StructureSet, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Structure set must be an object");
			}
			return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
		}
	};

	private JsonArray structures;   // array of { "structure": id, "weight": n }
	private JsonObject placement;   // placement object
	private final JsonObject extra = new JsonObject();

	public static StructureSet set() {
		return new StructureSet();
	}

	public static StructureSet fromJson(JsonObject json) {
		return new StructureSet().json(json);
	}

	public StructureSet json(JsonObject json) {
		this.structures = null;
		this.placement = null;
		this.extra.entrySet().clear();

		if (json == null) return this;

		if (json.has("structures") && json.get("structures").isJsonArray()) {
			this.structures = json.getAsJsonArray("structures").deepCopy();
		}
		if (json.has("placement") && json.get("placement").isJsonObject()) {
			this.placement = json.getAsJsonObject("placement").deepCopy();
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

	public StructureSet structures(JsonArray structures) {
		this.structures = structures == null ? null : structures.deepCopy();
		return this;
	}

	public StructureSet placement(JsonObject placement) {
		this.placement = placement == null ? null : placement.deepCopy();
		return this;
	}

	public StructureSet randomSpreadPlacement(int salt, int spacing, int separation) {
		JsonObject placement = new JsonObject();
		placement.addProperty("type", "minecraft:random_spread");
		placement.addProperty("salt", salt);
		placement.addProperty("spacing", spacing);
		placement.addProperty("separation", separation);
		return placement(placement);
	}

	public StructureSet dimensionOriginPlacement() {
		JsonObject placement = new JsonObject();
		placement.addProperty("type", "minecraft:dimension_origin");
		return placement(placement);
	}

	public StructureSet extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers

	private JsonArray ensureStructures() {
		if (this.structures == null) this.structures = new JsonArray();
		return this.structures;
	}

	public StructureSet addStructure(Identifier id, int weight) {
		JsonObject obj = new JsonObject();
		obj.addProperty("structure", id == null ? null : id.toString());
		obj.addProperty("weight", weight);
		ensureStructures().add(obj);
		return this;
	}

	public static StructureSet randomSpread(Identifier structureId, int weight, int salt, int spacing, int separation) {
		return StructureSet.set()
				.addStructure(structureId, weight)
				.randomSpreadPlacement(salt, spacing, separation);
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.structures != null) out.add("structures", this.structures.deepCopy());
		if (this.placement != null) out.add("placement", this.placement.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement val = this.extra.get(key);
			if (val != null) out.add(key, val.deepCopy());
		}
		return out;
	}

	private static boolean isKnown(String key) {
		return "structures".equals(key) || "placement".equals(key);
	}
}
