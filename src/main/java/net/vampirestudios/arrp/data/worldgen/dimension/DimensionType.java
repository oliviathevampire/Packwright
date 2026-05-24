package net.vampirestudios.arrp.data.worldgen.dimension;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.vampirestudios.arrp.data.worldgen.EnvironmentAttributeValue;
import net.vampirestudios.arrp.data.worldgen.EnvironmentAttributes;

import java.util.*;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaTagId;

public class DimensionType {
	public static final Codec<DimensionType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("has_skylight").orElse(true)
					.forGetter(dt -> dt.hasSkylight),
			Codec.BOOL.fieldOf("has_ceiling").orElse(false)
					.forGetter(dt -> dt.hasCeiling),
			Codec.DOUBLE.fieldOf("coordinate_scale").orElse(1.0D)
					.forGetter(dt -> dt.coordinateScale),
			IntProviderLike.CODEC.optionalFieldOf("monster_spawn_light_level")
					.forGetter(dt -> Optional.ofNullable(dt.monsterSpawnLightLevel)),
			Codec.INT.optionalFieldOf("monster_spawn_block_light_limit")
					.forGetter(dt -> Optional.ofNullable(dt.monsterSpawnBlockLightLimit)),
			Codec.INT.fieldOf("min_y").orElse(-64)
					.forGetter(dt -> dt.minY),
			Codec.INT.fieldOf("height").orElse(384)
					.forGetter(dt -> dt.height),
			Codec.INT.fieldOf("logical_height").orElse(384)
					.forGetter(dt -> dt.logicalHeight),
			Codec.STRING.fieldOf("infiniburn").orElse(vanillaTagId("infiniburn_overworld"))
					.forGetter(dt -> dt.infiniburn),
			Codec.FLOAT.fieldOf("ambient_light").orElse(0.0F)
					.forGetter(dt -> dt.ambientLight),
			Codec.unboundedMap(Identifier.CODEC, EnvironmentAttributeValue.CODEC)
					.optionalFieldOf("attributes", Collections.emptyMap())
					.forGetter(dt -> dt.attributes == null ? Collections.emptyMap() : dt.attributes),
			TimelinesRef.CODEC.optionalFieldOf("timelines")
					.forGetter(dt -> Optional.ofNullable(dt.timelines)),
			Skybox.CODEC.optionalFieldOf("skybox")
					.forGetter(dt -> Optional.ofNullable(dt.skybox)),
			CardinalLightType.CODEC.optionalFieldOf("cardinal_light")
					.forGetter(dt -> Optional.ofNullable(dt.cardinalLight)),
			Codec.BOOL.optionalFieldOf("has_fixed_time")
					.forGetter(dt -> Optional.ofNullable(dt.hasFixedTime))
	).apply(instance, (hasSkylight, hasCeiling, coordinateScale,
					   monsterLightOpt, blockLimitOpt,
					   minY, height, logicalHeight,
					   infiniburn, ambientLight,
					   attributesMap, timelinesOpt,
					   skyboxOpt, cardinalOpt, hasFixedTimeOpt) -> {

		DimensionType t = new DimensionType();
		t.hasSkylight = hasSkylight;
		t.hasCeiling = hasCeiling;
		t.coordinateScale = coordinateScale;
		t.monsterSpawnLightLevel = monsterLightOpt.orElse(null);
		t.monsterSpawnBlockLightLimit = blockLimitOpt.orElse(null);
		t.minY = minY;
		t.height = height;
		t.logicalHeight = logicalHeight;
		t.infiniburn = infiniburn;
		t.ambientLight = ambientLight;
		t.attributes = new LinkedHashMap<>(attributesMap);
		t.timelines = timelinesOpt.orElse(null);
		t.skybox = skyboxOpt.orElse(null);
		t.cardinalLight = cardinalOpt.orElse(null);
		t.hasFixedTime = hasFixedTimeOpt.orElse(null);
		return t;
	}));

	// ----------------- fields -----------------

	private boolean hasSkylight = true;
	private boolean hasCeiling = false;
	private double coordinateScale = 1.0D;

	private IntProviderLike monsterSpawnLightLevel;
	private Integer monsterSpawnBlockLightLimit;

	private int minY = -64;
	private int height = 384;
	private int logicalHeight = 384;

	private String infiniburn = vanillaTagId("infiniburn_overworld");
	private float ambientLight = 0.0F;

	private Map<Identifier, EnvironmentAttributeValue> attributes = new LinkedHashMap<>();
	private TimelinesRef timelines;
	private Skybox skybox;
	private CardinalLightType cardinalLight;
	private Boolean hasFixedTime;

	// ----------------- factory -----------------

	public static DimensionType dimensionType() {
		return new DimensionType();
	}

	// ----------------- fluent builder API -----------------

	public DimensionType hasSkylight(boolean value) {
		this.hasSkylight = value;
		return this;
	}

	public DimensionType hasCeiling(boolean value) {
		this.hasCeiling = value;
		return this;
	}

	public DimensionType coordinateScale(double scale) {
		this.coordinateScale = scale;
		return this;
	}

	public DimensionType monsterSpawnLightConstant(int value) {
		this.monsterSpawnLightLevel = IntProviderLike.constant(value);
		return this;
	}

	public DimensionType monsterSpawnLightUniform(int minInclusive, int maxInclusive) {
		this.monsterSpawnLightLevel = IntProviderLike.uniform(minInclusive, maxInclusive);
		return this;
	}

	public DimensionType monsterSpawnBlockLightLimit(int limit) {
		this.monsterSpawnBlockLightLimit = limit;
		return this;
	}

	public DimensionType minY(int minY) {
		this.minY = minY;
		return this;
	}

	public DimensionType height(int height) {
		this.height = height;
		return this;
	}

	public DimensionType logicalHeight(int logicalHeight) {
		this.logicalHeight = logicalHeight;
		return this;
	}

	public DimensionType infiniburn(TagKey<Block> infiniburnTagOrBlock) {
		this.infiniburn = "#" + infiniburnTagOrBlock.location();
		return this;
	}

	public DimensionType ambientLight(float value) {
		this.ambientLight = value;
		return this;
	}

	// ---- attributes ----

	public DimensionType attribute(Identifier key, boolean value) {
		if (key != null) {
			this.attributes.put(key, EnvironmentAttributeValue.ofBoolean(value));
		}
		return this;
	}

	public DimensionType attribute(Identifier key, double value) {
		if (key != null) {
			this.attributes.put(key, EnvironmentAttributeValue.ofNumber(value));
		}
		return this;
	}

	public DimensionType attribute(Identifier key, String value) {
		if (key != null && value != null) {
			this.attributes.put(key, EnvironmentAttributeValue.ofString(value));
		}
		return this;
	}

	public DimensionType attributes(EnvironmentAttributes attributes) {
		if (attributes != null) {
			for (Map.Entry<Identifier, EnvironmentAttributeValue> entry : attributes.getValues().entrySet()) {
				this.attributes.put(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	public DimensionType skyColor(String color) { return attribute(EnvironmentAttributes.SKY_COLOR, color); }
	public DimensionType fogColor(String color) { return attribute(EnvironmentAttributes.FOG_COLOR, color); }
	public DimensionType waterFogColor(String color) { return attribute(EnvironmentAttributes.WATER_FOG_COLOR, color); }
	public DimensionType cloudColor(String color) { return attribute(EnvironmentAttributes.CLOUD_COLOR, color); }
	public DimensionType cloudHeight(float value) { return attribute(EnvironmentAttributes.CLOUD_HEIGHT, value); }
	public DimensionType sunAngle(float value) { return attribute(EnvironmentAttributes.SUN_ANGLE, value); }
	public DimensionType moonAngle(float value) { return attribute(EnvironmentAttributes.MOON_ANGLE, value); }
	public DimensionType starAngle(float value) { return attribute(EnvironmentAttributes.STAR_ANGLE, value); }
	public DimensionType starBrightness(float value) { return attribute(EnvironmentAttributes.STAR_BRIGHTNESS, value); }
	public DimensionType skyLightColor(String color) { return attribute(EnvironmentAttributes.SKY_LIGHT_COLOR, color); }
	public DimensionType skyLightFactor(float value) { return attribute(EnvironmentAttributes.SKY_LIGHT_FACTOR, value); }
	public DimensionType skyLightLevel(float value) { return attribute(EnvironmentAttributes.SKY_LIGHT_LEVEL, value); }
	public DimensionType waterEvaporates(boolean value) { return attribute(EnvironmentAttributes.WATER_EVAPORATES, value); }
	public DimensionType bedRule(String value) { return attribute(EnvironmentAttributes.BED_RULE, value); }
	public DimensionType respawnAnchorWorks(boolean value) { return attribute(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, value); }
	public DimensionType netherPortalSpawnsPiglins(boolean value) { return attribute(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, value); }
	public DimensionType fastLava(boolean value) { return attribute(EnvironmentAttributes.FAST_LAVA, value); }
	public DimensionType piglinsZombify(boolean value) { return attribute(EnvironmentAttributes.PIGLINS_ZOMBIFY, value); }
	public DimensionType snowGolemMelts(boolean value) { return attribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, value); }
	public DimensionType creakingActive(boolean value) { return attribute(EnvironmentAttributes.CREAKING_ACTIVE, value); }

	public Map<Identifier, EnvironmentAttributeValue> attributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	/**
	 * Single timeline ID.
	 */
	public DimensionType timeline(Identifier id) {
		this.timelines = TimelinesRef.single(id == null ? null : id.toString());
		return this;
	}

	/**
	 * Multiple timeline IDs.
	 */
	public DimensionType timelines(List<Identifier> ids) {
		List<String> strIds = new ArrayList<>();
		if (ids != null) for (Identifier id : ids) strIds.add(id == null ? null : id.toString());
		this.timelines = TimelinesRef.list(strIds);
		return this;
	}

	/**
	 * Timeline tag, e.g. "#minecraft:day_cycle".
	 */
	public DimensionType timelinesTag(Identifier tag) {
		this.timelines = TimelinesRef.tag(tag == null ? null : "#" + tag);
		return this;
	}

	public DimensionType skybox(Skybox skybox) {
		this.skybox = skybox;
		return this;
	}

	public DimensionType cardinalLight(CardinalLightType cardinalLightType) {
		this.cardinalLight = cardinalLightType;
		return this;
	}

	public DimensionType hasFixedTime(boolean value) {
		this.hasFixedTime = value;
		return this;
	}

	// ----------------- helper types -----------------

	public enum CardinalLightType implements StringRepresentable {
		DEFAULT("default"),
		NETHER("nether");

		public static final Codec<DimensionType.CardinalLightType> CODEC = StringRepresentable.fromEnum(DimensionType.CardinalLightType::values);
		private final String name;

		private CardinalLightType(final String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}

	public enum Skybox implements StringRepresentable {
		NONE("none"),
		OVERWORLD("overworld"),
		END("end");

		public static final Codec<DimensionType.Skybox> CODEC = StringRepresentable.fromEnum(DimensionType.Skybox::values);
		private final String name;

		private Skybox(final String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}

	/**
	 * IntProvider-ish helper for monster_spawn_light_level.
	 * Supports constant or minecraft:uniform.
	 * @param value  for CONSTANT
	 * @param minInclusive  for UNIFORM
	 * @param maxInclusive  for UNIFORM
	 */
	public record IntProviderLike(Kind kind, int value, int minInclusive, int maxInclusive) {
		private static final Codec<IntProviderLike> UNIFORM_WRAPPED_CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						Codec.STRING.fieldOf("type").forGetter(p -> "minecraft:uniform"),
						Codec.INT.fieldOf("min_inclusive").forGetter(IntProviderLike::minInclusive),
						Codec.INT.fieldOf("max_inclusive").forGetter(IntProviderLike::maxInclusive)
				).apply(instance, (type, minInclusive, maxInclusive) ->
						// You *could* validate `type` here if you want, but we'll just trust it
						IntProviderLike.uniform(minInclusive, maxInclusive)
				));
		public static final Codec<IntProviderLike> CODEC =
				Codec.either(Codec.INT, UNIFORM_WRAPPED_CODEC)
						.xmap(
								either -> {
									if (either.left().isPresent()) {
										return IntProviderLike.constant(either.left().get());
									}
									return either.right().get();
								},
								provider -> {
									if (provider.kind == Kind.CONSTANT) {
										return Either.left(provider.value);
									}
									return Either.right(provider);
								}
						);

		public static IntProviderLike constant(int value) {
			return new IntProviderLike(Kind.CONSTANT, value, 0, 0);
		}

		public static IntProviderLike uniform(int minInclusive, int maxInclusive) {
			return new IntProviderLike(Kind.UNIFORM, 0, minInclusive, maxInclusive);
		}


		public int constantValue() {
			if (kind != Kind.CONSTANT) {
				throw new IllegalStateException("Not a constant IntProvider");
			}
			return value;
		}

		@Override
		public int minInclusive() {
			if (kind != Kind.UNIFORM) {
				throw new IllegalStateException("Not a uniform IntProvider");
			}
			return minInclusive;
		}

		@Override
		public int maxInclusive() {
			if (kind != Kind.UNIFORM) {
				throw new IllegalStateException("Not a uniform IntProvider");
			}
			return maxInclusive;
		}

		public enum Kind {CONSTANT, UNIFORM}
	}

	/**
	 * timelines: either "id", ["id1","id2"], or "#tag".
	 * @param tag  if non-null, this is a tag reference
	 */
	public record TimelinesRef(String tag, List<String> timelineIds) {

		public static final Codec<TimelinesRef> CODEC =
				Codec.either(Codec.STRING, Codec.list(Codec.STRING))
						.xmap(
								either -> {
									if (either.left().isPresent()) {
										String value = either.left().get();
										if (value.startsWith("#")) {
											return TimelinesRef.tag(value);
										} else {
											return TimelinesRef.single(value);
										}
									}
									return TimelinesRef.list(either.right().get());
								},
								ref -> {
									if (ref.tag != null) {
										return Either.left(ref.tag);
									}
									if (ref.timelineIds.size() == 1) {
										return Either.left(ref.timelineIds.getFirst());
									}
									return Either.right(ref.timelineIds);
								}
						);

		public static TimelinesRef single(String id) {
			List<String> list = new ArrayList<>(1);
			list.add(id);
			return new TimelinesRef(null, list);
		}

		public static TimelinesRef list(List<String> ids) {
			return new TimelinesRef(null, new ArrayList<>(ids));
		}

		public static TimelinesRef tag(String tag) {
			return new TimelinesRef(tag, Collections.emptyList());
		}

		@Override
		public List<String> timelineIds() {
			return Collections.unmodifiableList(timelineIds);
		}
	}
}
