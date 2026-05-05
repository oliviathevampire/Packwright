package net.vampirestudios.arrp.json.worldgen.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

public class JConfiguredFeature implements Cloneable {

	public static final Codec<JConfiguredFeature> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(JConfiguredFeature feature, DynamicOps<T> ops, T prefix) {
			JsonObject json = feature.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<JConfiguredFeature, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Configured feature must be an object");
			}
			return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
		}
	};

	private String type;
	protected JsonObject config;
	private final JsonObject extra = new JsonObject();

	public static JConfiguredFeature feature() {
		return new JConfiguredFeature();
	}

	public static JConfiguredFeature fromJson(JsonObject json) {
		return new JConfiguredFeature().json(json);
	}

	public JConfiguredFeature json(JsonObject json) {
		this.type = null;
		this.config = null;
		this.extra.entrySet().clear();

		if (json == null) return this;

		if (json.has("type") && json.get("type").isJsonPrimitive()) {
			this.type = json.get("type").getAsString();
		}
		if (json.has("config") && json.get("config").isJsonObject()) {
			this.config = json.getAsJsonObject("config").deepCopy();
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

	public JConfiguredFeature type(String type) {
		this.type = type;
		return this;
	}

	public JConfiguredFeature config(JsonObject config) {
		this.config = config == null ? null : config.deepCopy();
		return this;
	}

	public JConfiguredFeature extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers

	/**
	 * Very simple "tree" config builder.
	 * You can replace this with a full vanilla-equivalent config later.
	 */
	public static JConfiguredFeature tree(String trunkId, String leavesId) {
		JsonObject cfg = new JsonObject();
		cfg.addProperty("trunk_block", trunkId);
		cfg.addProperty("foliage_block", leavesId);
		return JConfiguredFeature.feature()
				.type("minecraft:tree")
				.config(cfg);
	}

	public JConfiguredFeature ignoreVines(boolean value) {
		if (this.config == null) this.config = new JsonObject();
		this.config.addProperty("ignore_vines", value);
		return this;
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.type != null) out.addProperty("type", this.type);
		if (this.config != null) out.add("config", this.config.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement val = this.extra.get(key);
			if (val != null) out.add(key, val.deepCopy());
		}
		return out;
	}

	@Override
	public JConfiguredFeature clone() {
		try {
			JConfiguredFeature clone = (JConfiguredFeature) super.clone();
			clone.config = this.config == null ? null : this.config.deepCopy();
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
		return "type".equals(key) || "config".equals(key);
	}
}
