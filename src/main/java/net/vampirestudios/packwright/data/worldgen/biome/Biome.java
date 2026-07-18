package net.vampirestudios.packwright.data.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.AttributeValue;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import java.util.*;

import static net.vampirestudios.packwright.util.ResourceHelpers.vanillaId;

/**
 * Biome definition that matches modern JSON:
 * - Classic fields: has_precipitation, temperature, downfall, etc.
 * - "effects" for the remaining legacy visuals (water/grass/foliage colors).
 * - "attributes" for Environment Attributes (visual/*, gameplay/*, audio/*, etc.).
 *
 * No Gson anywhere; everything is driven by Mojang Codecs.
 */
public class Biome {

	/**
	 * Since 26.3, mob spawning is no longer a set of flat top-level biome fields;
	 * it's an environment attribute value (an embedded {@link SpawnSettings}).
	 */
	private static final Identifier NATURAL_MOB_SPAWNS_ID = vanillaId("gameplay/natural_mob_spawns");

	// =====================================================================
	// Root biome codec
	// =====================================================================

	public static final Codec<Biome> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.BOOL.fieldOf("has_precipitation")
							.forGetter(Biome::hasPrecipitation),
					Codec.FLOAT.fieldOf("temperature")
							.forGetter(Biome::temperature),
					Codec.STRING.fieldOf("temperature_modifier").orElse("none")
							.forGetter(Biome::temperatureModifier),
					Codec.FLOAT.fieldOf("downfall")
							.forGetter(Biome::downfall),
					Effects.CODEC.fieldOf("effects")
							.forGetter(Biome::effects),
					Codec.unboundedMap(Identifier.CODEC, AttributeValue.CODEC)
							.fieldOf("attributes").orElse(Collections.emptyMap())
							.forGetter(Biome::attributesForEncode),
					// carvers and features are required by the game, even when empty
					Identifier.CODEC.listOf()
							.fieldOf("carvers").orElse(Collections.emptyList())
							.forGetter(b -> {
								if (b.generation == null) return Collections.emptyList();
								return b.generation.getCarvers();
							}),

					Identifier.CODEC.listOf().listOf()
							.fieldOf("features").orElse(Collections.emptyList())
							.forGetter(b -> {
								if (b.generation == null) return Collections.emptyList();
								return b.generation.getFeaturesByStep();
							})
			).apply(instance, (precip, temp, tempMod, downfall, effects, attrs, carvers, featuresSteps) -> {
				Biome biome = new Biome();
				biome.hasPrecipitation(precip);
				biome.temperature(temp);
				biome.temperatureModifier(tempMod);
				biome.downfall(downfall);
				biome.effects(effects);

				// ---- spawn: pulled out of the generic attributes map into the typed field ----
				Map<Identifier, AttributeValue> rest = new LinkedHashMap<>(attrs);
				AttributeValue mobSpawns = rest.remove(NATURAL_MOB_SPAWNS_ID);
				if (mobSpawns != null) {
					biome.spawnSettings(mobSpawns.decode(SpawnSettings.CODEC));
				}
				biome.attributes(rest);

				// ---- generation ----
				if (!carvers.isEmpty() || !featuresSteps.isEmpty()) {
					Generation g = new Generation();
					for (Identifier c : carvers) {
						g.addCarver(c);
					}
					for (int step = 0; step < featuresSteps.size(); step++) {
						for (Identifier feat : featuresSteps.get(step)) {
							g.addFeature(step, feat);
						}
					}
					biome.generation(g);
				}
				return biome;
			})
	);

	/** the raw attributes map plus the mob spawn settings, if any, keyed under {@code minecraft:gameplay/natural_mob_spawns} */
	private Map<Identifier, AttributeValue> attributesForEncode() {
		Map<Identifier, AttributeValue> base = this.attributes == null ? Collections.emptyMap() : this.attributes;
		if (this.spawnSettings == null || this.spawnSettings.isEmpty()) return base;

		Map<Identifier, AttributeValue> merged = new LinkedHashMap<>(base);
		merged.put(NATURAL_MOB_SPAWNS_ID, AttributeValue.ofEncoded(SpawnSettings.CODEC, this.spawnSettings));
		return merged;
	}

	// =====================================================================
	// Fields
	// =====================================================================

	private Boolean hasPrecipitation;
	private Float temperature;
	private String temperatureModifier = "none";
	private Float downfall;

	private Effects effects;           // remaining legacy effect fields
	private Map<Identifier, AttributeValue> attributes;     // Environment Attributes
	private SpawnSettings spawnSettings;
	private Generation generation;

	// =====================================================================
	// Constructors / factories
	// =====================================================================

	public Biome() {
		this.spawnSettings = new SpawnSettings();
		this.attributes = new HashMap<>();
		this.effects = null;        // fine to leave null
		this.generation = new Generation();
	}

	public static Biome biome() {
		return new Biome();
	}

	/**
	 * Basic empty-biome template with everything null.
	 */
	public static Biome empty() {
		return new Biome();
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

	private Optional<Map<Identifier, AttributeValue>> attributesOptional() {
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

	public Biome hasPrecipitation(boolean has) {
		this.hasPrecipitation = has;
		return this;
	}

	public Biome temperature(float temperature) {
		this.temperature = temperature;
		return this;
	}

	public Biome temperatureModifier(String modifier) {
		this.temperatureModifier = modifier;
		return this;
	}

	public Biome downfall(float downfall) {
		this.downfall = downfall;
		return this;
	}

	public Biome effects(Effects effects) {
		if (this.effects == null) {
			this.effects = new Effects();
		} else {
			this.effects = effects;
		}
		return this;
	}

	public Biome attributes(Map<Identifier, AttributeValue> attributes) {
		this.attributes = attributes;
		return this;
	}

	public Biome spawnSettings(SpawnSettings spawnSettings) {
		if (this.spawnSettings == null) {
			this.spawnSettings = new SpawnSettings();
		} else {
			this.spawnSettings = spawnSettings;
		}
		return this;
	}

	public Biome generation(Generation generation) {
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

	private Map<Identifier, AttributeValue> ensureAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return this.attributes;
	}

	/** Generic attribute setter for string values. */
	public Biome attribute(Identifier id, String value) {
		ensureAttributes().put(id, AttributeValue.ofString(value));
		return this;
	}

	/** Generic attribute setter for numeric values. */
	public Biome attribute(Identifier id, Number value) {
		ensureAttributes().put(id, AttributeValue.ofFloat(value.floatValue()));
		return this;
	}

	/** Generic attribute setter for boolean values. */
	public Biome attribute(Identifier id, boolean value) {
		ensureAttributes().put(id, AttributeValue.ofBoolean(value));
		return this;
	}

	// ---- Spawn helpers ----

	private SpawnSettings ensureSpawnSettings() {
		if (this.spawnSettings == null) {
			this.spawnSettings = new SpawnSettings();
		}
		return this.spawnSettings;
	}

	/** Convenience for "spawn_costs" entry. */
	public Biome spawnCost(Identifier entityId, double energyBudget, double charge) {
		ensureSpawnSettings().addSpawnCost(entityId, energyBudget, charge);
		return this;
	}

	/**
	 * Convenience for "spawns_by_category" entry.
	 * category examples: "monster", "creature", "ambient", "water_creature", "water_ambient", "misc".
	 */
	public Biome spawner(String category, Identifier entityId, int weight, int minCount, int maxCount) {
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

	/**
	 * The {@code minecraft:gameplay/natural_mob_spawns} attribute value (since 26.3;
	 * previously a set of flat top-level biome fields).
	 */
	public static class SpawnSettings {
		public static final Codec<SpawnSettings> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Codec.unboundedMap(Codec.STRING, SpawnerData.CODEC.listOf())
								.optionalFieldOf("spawns_by_category", Collections.emptyMap())
								.forGetter(s -> s.spawnsByCategory == null ? Collections.emptyMap() : s.spawnsByCategory),
						Codec.unboundedMap(Identifier.CODEC, SpawnCost.CODEC)
								.optionalFieldOf("spawn_costs", Collections.emptyMap())
								.forGetter(s -> s.spawnCosts == null ? Collections.emptyMap() : s.spawnCosts)
				).apply(instance, (spawnsByCategory, spawnCosts) -> {
					SpawnSettings s = new SpawnSettings();
					if (!spawnsByCategory.isEmpty()) {
						s.spawnsByCategory.putAll(spawnsByCategory);
					}
					if (!spawnCosts.isEmpty()) {
						s.spawnCosts.putAll(spawnCosts);
					}
					return s;
				})
		);
		private Map<String, List<SpawnerData>> spawnsByCategory = new LinkedHashMap<>();
		private Map<Identifier, SpawnCost> spawnCosts = new LinkedHashMap<>();

		// ---------------- core setters / getters ----------------

		public Map<Identifier, SpawnCost> getSpawnCosts() {
			return Collections.unmodifiableMap(spawnCosts);
		}

		public Map<String, List<SpawnerData>> getSpawnsByCategory() {
			return Collections.unmodifiableMap(spawnsByCategory);
		}

		public boolean isEmpty() {
			return spawnsByCategory.isEmpty() && spawnCosts.isEmpty();
		}

		// ---------------- builder-style helpers ----------------

		/** Add a spawn_cost entry: energy_budget + charge. */
		public SpawnSettings addSpawnCost(Identifier entityId, double energyBudget, double charge) {
			if (entityId != null) {
				this.spawnCosts.put(entityId, new SpawnCost(energyBudget, charge));
			}
			return this;
		}

		/**
		 * Add a spawns_by_category entry:
		 * category examples: "monster", "creature", "ambient", "water_creature", "water_ambient", "misc".
		 */
		public SpawnSettings addSpawner(String category, Identifier entityId, int weight, IntProvider count) {
			if (category == null || entityId == null) return this;
			this.spawnsByCategory
					.computeIfAbsent(category, k -> new ArrayList<>())
					.add(new SpawnerData(entityId, weight, count));
			return this;
		}

		/** a fixed spawn group size */
		public SpawnSettings addSpawner(String category, Identifier entityId, int weight, int count) {
			return addSpawner(category, entityId, weight, IntProvider.constant(count));
		}

		/** a random spawn group size, uniformly distributed between {@code minCount} and {@code maxCount} */
		public SpawnSettings addSpawner(String category, Identifier entityId, int weight, int minCount, int maxCount) {
			return addSpawner(category, entityId, weight, IntProvider.uniform(minCount, maxCount));
		}

		// ------------------------------------------------------
		// Nested value objects for spawn_costs and spawns_by_category
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
		 * Matches entries inside "spawns_by_category" category lists:
		 * <p>
		 * "spawns_by_category": {
		 *   "monster": [
		 *     { "type": "minecraft:zombie", "weight": 95, "count": {"type": "minecraft:uniform", "min_inclusive": 4, "max_inclusive": 4} }
		 *   ]
		 * }
		 */
		public record SpawnerData(Identifier type, int weight, IntProvider count) {
			public static final Codec<SpawnerData> CODEC = RecordCodecBuilder.create(instance ->
					instance.group(
							Identifier.CODEC.fieldOf("type").forGetter(d -> d.type),
							Codec.INT.fieldOf("weight").forGetter(d -> d.weight),
							IntProvider.CODEC.fieldOf("count").forGetter(d -> d.count)
					).apply(instance, SpawnerData::new)
			);
		}
	}

	public static class Generation {
		public static final Codec<Generation> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						// carvers and features are required by the game, even when empty
						Identifier.CODEC.listOf().fieldOf("carvers").orElse(Collections.emptyList())
								.forGetter(g -> g.carvers == null ? Collections.emptyList() : g.carvers),
						Identifier.CODEC.listOf().listOf().fieldOf("features").orElse(Collections.emptyList())
								.forGetter(g -> g.features == null ? Collections.emptyList() : g.features)
				).apply(instance, (carvers, features) -> {
					Generation g = new Generation();
					g.carvers = new ArrayList<>(carvers);
					g.features = new ArrayList<>();
					for (List<Identifier> step : features) {
						g.features.add(new ArrayList<>(step));
					}
					return g;
				})
		);
		private List<Identifier> carvers = new ArrayList<>();
		private List<List<Identifier>> features = new ArrayList<>();

		public Generation addCarver(Identifier id) {
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
		public Generation addFeature(int step, Identifier id) {
			if (id == null) return this;
			ensureStep(step);
			features.get(step).add(id);
			return this;
		}

		public List<Identifier> getCarvers() {
			return Collections.unmodifiableList(carvers);
		}

		/** Get features per step, as required by JSON. */
		public List<List<Identifier>> getFeaturesByStep() {
			// Return a read-only deep copy view
			if (features != null) {
				List<List<Identifier>> copy = new ArrayList<>(features.size());
				for (List<Identifier> step : features) {
					copy.add(Collections.unmodifiableList(step));
				}
				return Collections.unmodifiableList(copy);
			}
			return Collections.emptyList();
		}
	}
}
