package net.vampirestudios.arrp.data.worldgen.dimension;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;

/**
 * Simple builder for dimension datapack JSON that round-trips through codecs.
 *
 * {
 *   "type": "minecraft:overworld",
 *   "generator": { ... }
 * }
 */
public class Dimension implements Cloneable {
	public static final Codec<Dimension> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(Dimension dimension, DynamicOps<T> ops, T prefix) {
			JsonObject json = dimension.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<Dimension, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Dimension definition must be an object");
			}

			return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
		}
	};

	private String type;
	private JsonObject generator;
	private final JsonObject extra = new JsonObject();

	public static Dimension dimension() {
		return new Dimension();
	}

	public static Dimension fromJson(JsonObject json) {
		return new Dimension().json(json);
	}

	public Dimension json(JsonObject json) {
		this.type = null;
		this.generator = null;
		this.extra.entrySet().clear();

		if (json == null) {
			return this;
		}

		if (json.has("type") && json.get("type").isJsonPrimitive()) {
			this.type = json.get("type").getAsString();
		}
		if (json.has("generator") && json.get("generator").isJsonObject()) {
			this.generator = json.getAsJsonObject("generator").deepCopy();
		}

		for (String key : json.keySet()) {
			if (!isKnown(key)) {
				JsonElement value = json.get(key);
				if (value != null) {
					this.extra.add(key, value.deepCopy());
				}
			}
		}

		return this;
	}

	// Core setters

	public Dimension type(Identifier type) {
		this.type = type == null ? null : type.toString();
		return this;
	}

	public Dimension generator(JsonObject generator) {
		this.generator = generator == null ? null : generator.deepCopy();
		return this;
	}

	public Dimension extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers (keep test code out of JsonObject)

	/**
	 * Sets a noise generator with the given settings id.
	 */
	public Dimension noiseGenerator(Identifier settingsId) {
		JsonObject gen = this.generator == null ? new JsonObject() : this.generator.deepCopy();
		gen.addProperty("type", "minecraft:noise");
		gen.addProperty("settings", settingsId == null ? null : settingsId.toString());
		this.generator = gen;
		return this;
	}

	/**
	 * Sets biome_source to a fixed biome.
	 */
	public Dimension fixedBiome(Identifier biomeId) {
		JsonObject biomeSource = new JsonObject();
		biomeSource.addProperty("type", "minecraft:fixed");
		biomeSource.addProperty("biome", biomeId == null ? null : biomeId.toString());

		JsonObject gen = this.generator == null ? new JsonObject() : this.generator.deepCopy();
		gen.add("biome_source", biomeSource);
		this.generator = gen;
		return this;
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.type != null) out.addProperty("type", this.type);
		if (this.generator != null) out.add("generator", this.generator.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement value = this.extra.get(key);
			if (value != null) out.add(key, value.deepCopy());
		}
		return out;
	}

	@Override
	public Dimension clone() {
		try {
			Dimension clone = (Dimension) super.clone();
			clone.generator = this.generator == null ? null : this.generator.deepCopy();
			clone.extra.entrySet().clear();
			for (String key : this.extra.keySet()) {
				JsonElement value = this.extra.get(key);
				if (value != null) clone.extra.add(key, value.deepCopy());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	private static boolean isKnown(String key) {
		return "type".equals(key) || "generator".equals(key);
	}
}