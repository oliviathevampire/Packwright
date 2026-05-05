package net.vampirestudios.arrp.json.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.JAttributeValue;
import net.minecraft.util.ExtraCodecs;
import java.util.*;

/**
 * Biome definition that matches modern JSON:
 * - Classic fields: has_precipitation, temperature, downfall, etc.
 * - "effects" for the remaining legacy visuals (water/grass/foliage colors).
 * - "attributes" for Environment Attributes (visual/*, gameplay/*, audio/*, etc.).
 *
 * No Gson anywhere; everything is driven by Mojang Codecs.
 */
public class JBiome {

	// =====================================================================
	// Root biome codec
	// =====================================================================

	public static final Codec<JBiome> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.BOOL.fieldOf("has_precipitation")
							.forGetter(JBiome::hasPrecipitation),
					Codec.FLOAT.fieldOf("temperature")
							.forGetter(JBiome::temperature),
					Codec.STRING.fieldOf("temperature_modifier")
							.forGetter(JBiome::temperatureModifier),
					Codec.FLOAT.fieldOf("downfall")
							.forGetter(JBiome::downfall),
					Effects.CODEC.fieldOf("effects")
							.forGetter(JBiome::effects),
					Codec.unboundedMap(Codec.STRING, JAttributeValue.CODEC)
							.optionalFieldOf("attributes", Collections.emptyMap())
							.forGetter(b -> b.attributes == null ? Collections.emptyMap() : b.attributes),
					Codec.FLOAT.optionalFieldOf("creature_spawn_probability")
							.forGetter(b -> {
								if (b.spawnSettings == null) return Optional.empty();
								Float prob = b.spawnSettings.getCreatureSpawnProbability();
								return prob == null ? Optional.empty() : Optional.of(prob);
							}),
					Codec.unboundedMap(Codec.STRING, SpawnSettings.SpawnCost.CODEC)
							.optionalFieldOf("spawn_costs")
							.forGetter(b -> Optional.of(
									b.spawnSettings == null
											? Collections.emptyMap()
											: b.spawnSettings.getSpawnCosts()
							)),
					Codec.unboundedMap(Codec.STRING, SpawnSettings.SpawnerData.CODEC.listOf())
							.optionalFieldOf("spawners")
							.forGetter(b -> Optional.of(
									b.spawnSettings == null
											? Collections.emptyMap()
											: b.spawnSettings.getSpawners()
							)),
					Codec.STRING.listOf()
							.optionalFieldOf("carvers", Collections.emptyList())
							.forGetter(b -> {
								if (b.generation == null) return Collections.emptyList();
								return b.generation.getCarvers();
							}),

					Codec.STRING.listOf().listOf()
							.optionalFieldOf("features", Collections.emptyList())
							.forGetter(b -> {
								if (b.generation == null) return Collections.emptyList();
								return b.generation.getFeaturesByStep();
							})
			).apply(instance, (precip, temp, tempMod, downfall, effects, attrs,
							   spawnProbOpt, spawnCostsOpt, spawnersOpt, carvers, featuresSteps) -> {
				JBiome biome = new JBiome();
				biome.hasPrecipitation(precip);
				biome.temperature(temp);
				biome.temperatureModifier(tempMod);
				biome.downfall(downfall);
				biome.effects(effects);
				biome.attributes(attrs);

				// ---- spawn ----
				boolean hasAnySpawn =
						spawnProbOpt.isPresent()
								|| spawnCostsOpt.isPresent()
								|| spawnersOpt.isPresent();

				if (hasAnySpawn) {
					SpawnSettings s = new SpawnSettings();
					spawnProbOpt.ifPresent(s::setCreatureSpawnProbability);

					Map<String, SpawnSettings.SpawnCost> spawnCosts =
							spawnCostsOpt.orElseGet(Collections::emptyMap);
					if (!spawnCosts.isEmpty()) {
						s.spawnCosts.putAll(spawnCosts);
					}

					Map<String, List<SpawnSettings.SpawnerData>> spawners =
							spawnersOpt.orElseGet(Collections::emptyMap);
					if (!spawners.isEmpty()) {
						s.spawners.putAll(spawners);
					}

					biome.spawnSettings(s);
				}

				// ---- generation ----
				if (!carvers.isEmpty() || !featuresSteps.isEmpty()) {
					Generation g = new Generation();
					for (String c : carvers) {
						g.addCarver(c);
					}
					for (int step = 0; step < featuresSteps.size(); step++) {
						for (String feat : featuresSteps.get(step)) {
							g.addFeature(step, feat);
						}
					}
					biome.generation(g);
				}
				return biome;
			})
	);

	// =====================================================================
	// Fields
	// =====================================================================

	private Boolean hasPrecipitation;
	private Float temperature;
	private String temperatureModifier = "none";
	private Float downfall;

	private Effects effects;           // remaining legacy effect fields
	private Map<String, JAttributeValue> attributes;     // Environment Attributes
	private SpawnSettings spawnSettings;
	private Generation generation;

	// =====================================================================
	// Constructors / factories
	// =====================================================================

	public JBiome() {
		this.spawnSettings = new SpawnSettings();
		this.attributes = new HashMap<>();
		this.effects = null;        // fine to leave null
		this.generation = new Generation();
	}

	public static JBiome biome() {
		return new JBiome();
	}

	/**
	 * Basic empty-biome template with everything null.
	 */
	public static JBiome empty() {
		return new JBiome();
	}

	// =====================================================================
	// Optional accessors for Codec
	// =====================================================================

	private Boolean hasPrecipitation() {
		return this.hasPrecipitation;
	}

	private float temperature() {
		return this.temperature;
	}

	private String temperatureModifier() {
		return this.temperatureModifier;
	}

	private float downfall() {
		return this.downfall;
	}

	private Effects effects() {
		return this.effects;
	}

	private Optional<Map<String, JAttributeValue>> attributesOptional() {
		return Optional.ofNullable(this.attributes);
	}

	private Optional<SpawnSettings> spawnSettingsOptional() {
		return Optional.ofNullable(this.spawnSettings);
	}

	private Optional<Generation> generationOptional() {
		return Optional.ofNullable(this.generation);
	}

	// =====================================================================
	// Builder-style setters
	// =====================================================================

	public JBiome hasPrecipitation(boolean has) {
		this.hasPrecipitation = has;
		return this;
	}

	public JBiome temperature(float temperature) {
		this.temperature = temperature;
		return this;
	}

	public JBiome temperatureModifier(String modifier) {
		this.temperatureModifier = modifier;
		return this;
	}

	public JBiome downfall(float downfall) {
		this.downfall = downfall;
		return this;
	}

	public JBiome effects(Effects effects) {
		if (this.effects == null) {
			this.effects = new Effects();
		} else {
			this.effects = effects;
		}
		return this;
	}

	public JBiome attributes(Map<String, JAttributeValue> attributes) {
		this.attributes = attributes;
		return this;
	}

	public JBiome spawnSettings(SpawnSettings spawnSettings) {
		if (this.spawnSettings == null) {
			this.spawnSettings = new SpawnSettings();
		} else {
			this.spawnSettings = spawnSettings;
		}
		return this;
	}

	public JBiome generation(Generation generation) {
		if (this.generation == null) {
			this.generation = new Generation();
		} else {
			this.generation = generation;
		}
		return this;
	}

	// =====================================================================
	// Convenience helpers
	// =====================================================================

	// ---- Effects helpers (remaining classic visual fields) ----

	private Effects ensureEffects() {
		if (this.effects == null) {
			this.effects = new Effects();
		}
		return this.effects;
	}

	// ---- Attributes helpers: raw map access ----

	private Map<String, JAttributeValue> ensureAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return this.attributes;
	}

	/** Generic attribute setter for string values. */
	public JBiome attribute(String id, String value) {
		ensureAttributes().put(id, JAttributeValue.ofString(value));
		return this;
	}

	/** Generic attribute setter for numeric values. */
	public JBiome attribute(String id, Number value) {
		ensureAttributes().put(id, JAttributeValue.ofFloat(value.floatValue()));
		return this;
	}

	/** Generic attribute setter for boolean values. */
	public JBiome attribute(String id, boolean value) {
		ensureAttributes().put(id, JAttributeValue.ofBoolean(value));
		return this;
	}

	// ---- Spawn helpers ----

	private SpawnSettings ensureSpawnSettings() {
		if (this.spawnSettings == null) {
			this.spawnSettings = new SpawnSettings();
		}
		return this.spawnSettings;
	}

	public JBiome spawnProbability(float probability) {
		ensureSpawnSettings().setCreatureSpawnProbability(probability);
		return this;
	}

	/** Convenience for "spawn_costs" entry. */
	public JBiome spawnCost(String entityId, double energyBudget, double charge) {
		ensureSpawnSettings().addSpawnCost(entityId, energyBudget, charge);
		return this;
	}

	/**
	 * Convenience for "spawners" entry.
	 * category examples: "monster", "creature", "ambient", "water_creature", "water_ambient", "misc".
	 */
	public JBiome spawner(String category, String entityId, int weight, int minCount, int maxCount) {
		ensureSpawnSettings().addSpawner(category, entityId, weight, minCount, maxCount);
		return this;
	}

	// =====================================================================
	// Nested types
	// =====================================================================

	/**
	 * Remaining biome "effects" fields after the Environment Attribute migration:
	 * - water_color
	 * - grass_color
	 * - foliage_color
	 * - grass_color_modifier
	 */
	public static class Effects {
		public static final Codec<Effects> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						ExtraCodecs.STRING_RGB_COLOR.fieldOf("water_color").forGetter(e -> e.waterColor),
						ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("grass_color").forGetter(e -> e.grassColor),
						ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("foliage_color").forGetter(e -> e.foliageColor),
						ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("dry_foliage_color").forGetter(e -> e.dryFoliageColor),
						Codec.STRING.optionalFieldOf("grass_color_modifier").forGetter(e -> Optional.ofNullable(e.grassColorModifier))
				).apply(instance, (water, grass, foliage, dryFoliage, modifier) -> {
					Effects e = new Effects();
					e.waterColor(water);
					grass.ifPresent(e::grassColor);
					foliage.ifPresent(e::foliageColor);
					dryFoliage.ifPresent(e::dryFoliageColor);
					modifier.ifPresent(e::grassColorModifier);
					return e;
				})
		);
		private int waterColor;
		private Optional<Integer> grassColor = Optional.empty();
		private Optional<Integer> foliageColor = Optional.empty();
		private Optional<Integer> dryFoliageColor = Optional.empty();
		private String grassColorModifier;

		public Effects waterColor(int color) {
			this.waterColor = color;
			return this;
		}

		public Effects grassColor(int color) {
			this.grassColor = Optional.of(color);
			return this;
		}

		public Effects foliageColor(int color) {
			this.foliageColor = Optional.of(color);
			return this;
		}

		public Effects dryFoliageColor(int color) {
			this.dryFoliageColor = Optional.of(color);
			return this;
		}

		public Effects grassColorModifier(String modifier) {
			this.grassColorModifier = modifier;
			return this;
		}
	}

	public static class SpawnSettings {
		public static final Codec<SpawnSettings> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Codec.FLOAT.optionalFieldOf("creature_spawn_probability")
								.forGetter(s -> Optional.ofNullable(s.creatureSpawnProbability)),
						Codec.unboundedMap(Codec.STRING, SpawnCost.CODEC)
								.optionalFieldOf("spawn_costs", Collections.emptyMap())
								.forGetter(s -> s.spawnCosts == null ? Collections.emptyMap() : s.spawnCosts),
						Codec.unboundedMap(Codec.STRING, SpawnerData.CODEC.listOf())
								.optionalFieldOf("spawners", Collections.emptyMap())
								.forGetter(s -> s.spawners == null ? Collections.emptyMap() : s.spawners)
				).apply(instance, (probOpt, spawnCosts, spawners) -> {
					SpawnSettings s = new SpawnSettings();
					probOpt.ifPresent(s::setCreatureSpawnProbability);
					if (!spawnCosts.isEmpty()) {
						s.spawnCosts.putAll(spawnCosts);
					}
					if (!spawners.isEmpty()) {
						s.spawners.putAll(spawners);
					}
					return s;
				})
		);
		private Float creatureSpawnProbability;
		private Map<String, SpawnCost> spawnCosts = new LinkedHashMap<>();
		private Map<String, List<SpawnerData>> spawners = new LinkedHashMap<>();

		// ---------------- core setters / getters ----------------

		public Float getCreatureSpawnProbability() {
			return creatureSpawnProbability;
		}

		public SpawnSettings setCreatureSpawnProbability(float probability) {
			this.creatureSpawnProbability = probability;
			return this;
		}

		public Map<String, SpawnCost> getSpawnCosts() {
			return Collections.unmodifiableMap(spawnCosts);
		}

		public Map<String, List<SpawnerData>> getSpawners() {
			return Collections.unmodifiableMap(spawners);
		}

		// ---------------- builder-style helpers ----------------

		/** Add a spawn_cost entry: energy_budget + charge. */
		public SpawnSettings addSpawnCost(String entityId, double energyBudget, double charge) {
			if (entityId != null) {
				this.spawnCosts.put(entityId, new SpawnCost(energyBudget, charge));
			}
			return this;
		}

		/**
		 * Add a spawner entry:
		 * category examples: "monster", "creature", "ambient", "water_creature", "water_ambient", "misc".
		 */
		public SpawnSettings addSpawner(String category, String entityId, int weight, int minCount, int maxCount) {
			if (category == null || entityId == null) return this;
			this.spawners
					.computeIfAbsent(category, k -> new ArrayList<>())
					.add(new SpawnerData(entityId, weight, minCount, maxCount));
			return this;
		}

		// ------------------------------------------------------
		// Nested value objects for spawn_costs and spawners
		// ------------------------------------------------------

		/**
		 * Matches JSON:
		 * "spawn_costs": {
		 *   "minecraft:creeper": {
		 *     "energy_budget": 0.12,
		 *     "charge": 0.15
		 *   }
		 * }
		 */
		public record SpawnCost(double energyBudget, double charge) {
			public static final Codec<SpawnCost> CODEC = RecordCodecBuilder.create(instance ->
					instance.group(
							Codec.DOUBLE.fieldOf("energy_budget").forGetter(c -> c.energyBudget),
							Codec.DOUBLE.fieldOf("charge").forGetter(c -> c.charge)
					).apply(instance, SpawnCost::new)
			);
		}

		/**
		 * Matches entries inside "spawners" category lists:
		 * <p>
		 * "spawners": {
		 *   "monster": [
		 *     { "type": "minecraft:zombie", "weight": 95, "minCount": 4, "maxCount": 4 }
		 *   ]
		 * }
		 */
		public record SpawnerData(String type, int weight, int minCount, int maxCount) {
			public static final Codec<SpawnerData> CODEC = RecordCodecBuilder.create(instance ->
					instance.group(
							Codec.STRING.fieldOf("type").forGetter(d -> d.type),
							Codec.INT.fieldOf("weight").forGetter(d -> d.weight),
							Codec.INT.fieldOf("minCount").forGetter(d -> d.minCount),
							Codec.INT.fieldOf("maxCount").forGetter(d -> d.maxCount)
					).apply(instance, SpawnerData::new)
			);

		}
	}

	public static class Generation {
		public static final Codec<Generation> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Codec.STRING.listOf().optionalFieldOf("carvers", Collections.emptyList())
								.forGetter(g -> g.carvers == null ? Collections.emptyList() : g.carvers),
						Codec.STRING.listOf().listOf().optionalFieldOf("features", Collections.emptyList())
								.forGetter(g -> g.features == null ? Collections.emptyList() : g.features)
				).apply(instance, (carvers, features) -> {
					Generation g = new Generation();
					g.carvers = new ArrayList<>(carvers);
					g.features = new ArrayList<>();
					for (List<String> step : features) {
						g.features.add(new ArrayList<>(step));
					}
					return g;
				})
		);
		private List<String> carvers = new ArrayList<>();
		private List<List<String>> features = new ArrayList<>();

		public Generation addCarver(String id) {
			if (id != null) this.carvers.add(id);
			return this;
		}

		/** Ensure the features list has at least (step+1) entries. */
		private void ensureStep(int step) {
			while (features.size() <= step) {
				features.add(new ArrayList<>());
			}
		}

		/** Add a feature at a specific generation step. */
		public Generation addFeature(int step, String id) {
			if (id == null) return this;
			ensureStep(step);
			features.get(step).add(id);
			return this;
		}

		public List<String> getCarvers() {
			return Collections.unmodifiableList(carvers);
		}

		/** Get features per step, as required by JSON. */
		public List<List<String>> getFeaturesByStep() {
			// Return a read-only deep copy view
			if (features != null) {
				List<List<String>> copy = new ArrayList<>(features.size());
				for (List<String> step : features) {
					copy.add(Collections.unmodifiableList(step));
				}
				return Collections.unmodifiableList(copy);
			}
			return Collections.emptyList();
		}
	}
}
