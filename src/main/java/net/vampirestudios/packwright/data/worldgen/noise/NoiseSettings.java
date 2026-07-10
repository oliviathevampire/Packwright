package net.vampirestudios.packwright.data.worldgen.noise;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;

public class NoiseSettings {

	public static final Codec<NoiseSettings> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(NoiseSettings settings, DynamicOps<T> ops, T prefix) {
			JsonObject json = settings.toJson();
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json).convert(ops).getValue());
		}

		@Override
		public <T> DataResult<Pair<NoiseSettings, T>> decode(DynamicOps<T> ops, T input) {
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
	private JsonElement materialRule;
	// required by the game; defaulted so a bare settings object still parses
	private boolean aquifersEnabled = false;
	private boolean oreVeinsEnabled = false;
	private boolean disableMobGeneration = false;
	private JsonObject noiseRouter;
	private final JsonObject extra = new JsonObject();

	public static NoiseSettings settings() {
		return new NoiseSettings();
	}

	public static NoiseSettings fromJson(JsonObject json) {
		return new NoiseSettings().json(json);
	}

	public NoiseSettings json(JsonObject json) {
		this.seaLevel = null;
		this.legacyRandomSource = null;
		this.defaultBlock = null;
		this.defaultFluid = null;
		this.noise = null;
		this.spawnTarget = null;
		this.spawnDensity = null;
		this.materialRule = null;
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
		if (json.has("material_rule")) {
			this.materialRule = json.get("material_rule").deepCopy();
		}
		if (json.has("aquifers_enabled") && json.get("aquifers_enabled").isJsonPrimitive()) {
			this.aquifersEnabled = json.get("aquifers_enabled").getAsBoolean();
		}
		if (json.has("ore_veins_enabled") && json.get("ore_veins_enabled").isJsonPrimitive()) {
			this.oreVeinsEnabled = json.get("ore_veins_enabled").getAsBoolean();
		}
		if (json.has("disable_mob_generation") && json.get("disable_mob_generation").isJsonPrimitive()) {
			this.disableMobGeneration = json.get("disable_mob_generation").getAsBoolean();
		}
		if (json.has("noise_router") && json.get("noise_router").isJsonObject()) {
			this.noiseRouter = json.getAsJsonObject("noise_router").deepCopy();
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

	public NoiseSettings seaLevel(int v) { this.seaLevel = v; return this; }
	public NoiseSettings legacyRandomSource(boolean v) { this.legacyRandomSource = v; return this; }
	public NoiseSettings defaultBlock(JsonObject v) { this.defaultBlock = v == null ? null : v.deepCopy(); return this; }
	public NoiseSettings defaultFluid(JsonObject v) { this.defaultFluid = v == null ? null : v.deepCopy(); return this; }
	public NoiseSettings noise(JsonObject v) { this.noise = v == null ? null : v.deepCopy(); return this; }
	public NoiseSettings spawnTarget(JsonArray v) { this.spawnTarget = v == null ? null : v.deepCopy(); return this; }
	public NoiseSettings spawnDensity(JsonObject v) { this.spawnDensity = v == null ? null : v.deepCopy(); return this; }
	public NoiseSettings materialRule(JsonElement v) { this.materialRule = v == null ? null : v.deepCopy(); return this; }
	public NoiseSettings materialRule(Identifier id) {
		this.materialRule = id == null ? null : new JsonPrimitive(id.toString());
		return this;
	}
	public NoiseSettings materialRule(MaterialRule rule) {
		this.materialRule = rule == null ? null : MaterialRule.CODEC.encodeStart(JsonOps.INSTANCE, rule).getOrThrow();
		return this;
	}

	public NoiseSettings aquifersEnabled(boolean v) { this.aquifersEnabled = v; return this; }
	public NoiseSettings oreVeinsEnabled(boolean v) { this.oreVeinsEnabled = v; return this; }
	public NoiseSettings disableMobGeneration(boolean v) { this.disableMobGeneration = v; return this; }
	public NoiseSettings noiseRouter(JsonObject v) { this.noiseRouter = v == null ? null : v.deepCopy(); return this; }

	/**
	 * a minimal noise router: every routing density function is {@code 0} except
	 * {@code final_density}, which shapes the terrain (positive = solid, negative = air)
	 */
	public NoiseSettings simpleNoiseRouter(JsonElement finalDensity) {
		JsonObject router = new JsonObject();
		for (String key : new String[]{
				"barrier", "fluid_level_floodedness", "fluid_level_spread", "lava",
				"temperature", "vegetation", "continents", "erosion", "depth", "ridges",
				"preliminary_surface_level", "initial_density_without_jaggedness",
				"vein_toggle", "vein_ridged", "vein_gap"
		}) {
			router.addProperty(key, 0);
		}
		router.add("final_density", finalDensity.deepCopy());
		return noiseRouter(router);
	}

	/**
	 * a router whose {@code final_density} is a simple y gradient: solid below
	 * {@code fromY}, air above {@code toY}, producing flat terrain in between
	 */
	public NoiseSettings simpleNoiseRouterGradient(int fromY, int toY) {
		JsonObject gradient = new JsonObject();
		gradient.addProperty("type", "minecraft:y_clamped_gradient");
		gradient.addProperty("from_y", fromY);
		gradient.addProperty("to_y", toY);
		gradient.addProperty("from_value", 1.0);
		gradient.addProperty("to_value", -1.0);
		return simpleNoiseRouter(gradient);
	}

	public NoiseSettings extra(String key, JsonElement value) {
		if (key != null && value != null && !isKnown(key)) {
			this.extra.add(key, value.deepCopy());
		}
		return this;
	}

	// Convenience helpers

	public NoiseSettings defaultBlockId(Identifier id) {
		if (id == null) {
			this.defaultBlock = null;
			return this;
		}
		JsonObject obj = new JsonObject();
		obj.addProperty("Name", id.toString());
		return defaultBlock(obj);
	}

	public NoiseSettings defaultFluidId(Identifier id) {
		if (id == null) {
			this.defaultFluid = null;
			return this;
		}
		JsonObject obj = new JsonObject();
		obj.addProperty("Name", id.toString());
		return defaultFluid(obj);
	}

	public NoiseSettings noiseSimple(int minY, int height, int sizeHorizontal, int sizeVertical) {
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
		// spawn_target and the three booleans are required by the game
		out.add("spawn_target", this.spawnTarget != null ? this.spawnTarget.deepCopy() : new JsonArray());
		if (this.spawnDensity != null) out.add("spawn_density", this.spawnDensity.deepCopy());
		if (this.materialRule != null) out.add("material_rule", this.materialRule.deepCopy());
		out.addProperty("aquifers_enabled", this.aquifersEnabled);
		out.addProperty("ore_veins_enabled", this.oreVeinsEnabled);
		out.addProperty("disable_mob_generation", this.disableMobGeneration);
		if (this.noiseRouter != null) out.add("noise_router", this.noiseRouter.deepCopy());
		for (String key : this.extra.keySet()) {
			JsonElement value = this.extra.get(key);
			if (value != null) out.add(key, value.deepCopy());
		}
		return out;
	}

	private static boolean isKnown(String key) {
		return "sea_level".equals(key)
				|| "legacy_random_source".equals(key)
				|| "default_block".equals(key)
				|| "default_fluid".equals(key)
				|| "noise".equals(key)
				|| "spawn_target".equals(key)
				|| "spawn_density".equals(key)
				|| "material_rule".equals(key)
				|| "aquifers_enabled".equals(key)
				|| "ore_veins_enabled".equals(key)
				|| "disable_mob_generation".equals(key)
				|| "noise_router".equals(key);
	}
}
