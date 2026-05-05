package net.vampirestudios.arrp.json.worldgen.noise;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

public class JNoiseSettings implements Cloneable {

	public static final Codec<JNoiseSettings> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(JNoiseSettings settings, DynamicOps<T> ops, T prefix) {
			JsonObject json = settings.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<JNoiseSettings, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) {
				return DataResult.error(() -> "Noise settings must be an object");
			}
			return DataResult.success(Pair.of(fromJson(el.getAsJsonObject()), input));
		}
	};

	private Integer seaLevel;
	private Boolean legacyRandomSource;
	private JsonObject defaultBlock;
	private JsonObject defaultFluid;
	private JsonObject noise;
	private JsonArray spawnTarget;
	private JsonObject spawnDensity;
	private final JsonObject extra = new JsonObject();

	public static JNoiseSettings settings() {
		return new JNoiseSettings();
	}

	public static JNoiseSettings fromJson(JsonObject json) {
		return new JNoiseSettings().json(json);
	}

	public JNoiseSettings json(JsonObject json) {
		this.seaLevel = null;
		this.legacyRandomSource = null;
		this.defaultBlock = null;
		this.defaultFluid = null;
		this.noise = null;
		this.spawnTarget = null;
		this.spawnDensity = null;
		this.extra.entrySet().clear();

		if (json == null) return this;

		if (json.has("sea_level") && json.get("sea_level").isJsonPrimitive()) {
			this.seaLevel = json.get("sea_level").getAsInt();
		}
		if (json.has("legacy_random_source") && json.get("legacy_random_source").isJsonPrimitive()) {
			this.legacyRandomSource = json.get("legacy_random_source").getAsBoolean();
		}
		if (json.has("default_block") && json.get("default_block").isJsonObject()) {
			this.defaultBlock = json.getAsJsonObject("default_block").deepCopy();
		}
		if (json.has("default_fluid") && json.get("default_fluid").isJsonObject()) {
			this.defaultFluid = json.getAsJsonObject("default_fluid").deepCopy();
		}
		if (json.has("noise") && json.get("noise").isJsonObject()) {
			this.noise = json.getAsJsonObject("noise").deepCopy();
		}
		if (json.has("spawn_target") && json.get("spawn_target").isJsonArray()) {
			this.spawnTarget = json.getAsJsonArray("spawn_target").deepCopy();
		}
		if (json.has("spawn_density") && json.get("spawn_density").isJsonObject()) {
			this.spawnDensity = json.getAsJsonObject("spawn_density").deepCopy();
		}

		for (String key : json.keySet()) {
			if (!isKnown(key)) {
				JsonElement value = json.get(key);
				if (value != null) this.extra.add(key, value.deepCopy());
			}
		}

		return this;
	}

	// Core setters

	public JNoiseSettings seaLevel(int v) { this.seaLevel = v; return this; }
	public JNoiseSettings legacyRandomSource(boolean v) { this.legacyRandomSource = v; return this; }
	public JNoiseSettings defaultBlock(JsonObject v) { this.defaultBlock = v == null ? null : v.deepCopy(); return this; }
	public JNoiseSettings defaultFluid(JsonObject v) { this.defaultFluid = v == null ? null : v.deepCopy(); return this; }
	public JNoiseSettings noise(JsonObject v) { this.noise = v == null ? null : v.deepCopy(); return this; }
	public JNoiseSettings spawnTarget(JsonArray v) { this.spawnTarget = v == null ? null : v.deepCopy(); return this; }
	public JNoiseSettings spawnDensity(JsonObject v) { this.spawnDensity = v == null ? null : v.deepCopy(); return this; }

	public JNoiseSettings extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers

	public JNoiseSettings defaultBlockId(String id) {
		if (id == null) {
			this.defaultBlock = null;
			return this;
		}
		JsonObject obj = new JsonObject();
		obj.addProperty("Name", id);
		return defaultBlock(obj);
	}

	public JNoiseSettings defaultFluidId(String id) {
		if (id == null) {
			this.defaultFluid = null;
			return this;
		}
		JsonObject obj = new JsonObject();
		obj.addProperty("Name", id);
		return defaultFluid(obj);
	}

	public JNoiseSettings noiseSimple(int minY, int height, int sizeHorizontal, int sizeVertical) {
		JsonObject n = new JsonObject();
		n.addProperty("min_y", minY);
		n.addProperty("height", height);
		n.addProperty("size_horizontal", sizeHorizontal);
		n.addProperty("size_vertical", sizeVertical);
		return noise(n);
	}

	public JsonObject toJson() {
		JsonObject out = new JsonObject();
		if (this.seaLevel != null) out.addProperty("sea_level", this.seaLevel);
		if (this.legacyRandomSource != null) out.addProperty("legacy_random_source", this.legacyRandomSource);
		if (this.defaultBlock != null) out.add("default_block", this.defaultBlock.deepCopy());
		if (this.defaultFluid != null) out.add("default_fluid", this.defaultFluid.deepCopy());
		if (this.noise != null) out.add("noise", this.noise.deepCopy());
		if (this.spawnTarget != null) out.add("spawn_target", this.spawnTarget.deepCopy());
		if (this.spawnDensity != null) out.add("spawn_density", this.spawnDensity.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement value = this.extra.get(key);
			if (value != null) out.add(key, value.deepCopy());
		}
		return out;
	}

	@Override
	public JNoiseSettings clone() {
		try {
			JNoiseSettings clone = (JNoiseSettings) super.clone();
			clone.defaultBlock = this.defaultBlock == null ? null : this.defaultBlock.deepCopy();
			clone.defaultFluid = this.defaultFluid == null ? null : this.defaultFluid.deepCopy();
			clone.noise = this.noise == null ? null : this.noise.deepCopy();
			clone.spawnTarget = this.spawnTarget == null ? null : this.spawnTarget.deepCopy();
			clone.spawnDensity = this.spawnDensity == null ? null : this.spawnDensity.deepCopy();
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
		return "sea_level".equals(key)
				|| "legacy_random_source".equals(key)
				|| "default_block".equals(key)
				|| "default_fluid".equals(key)
				|| "noise".equals(key)
				|| "spawn_target".equals(key)
				|| "spawn_density".equals(key);
	}
}
