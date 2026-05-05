package net.vampirestudios.arrp.json.worldgen.dimension;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.JAttributeValue;
import net.vampirestudios.arrp.json.worldgen.JEnvironmentAttributes;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import java.util.*;

public class JDimensionType {
	public static final Codec<JDimensionType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
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
			Codec.STRING.fieldOf("infiniburn").orElse("#minecraft:infiniburn_overworld")
					.forGetter(dt -> dt.infiniburn),
			Codec.FLOAT.fieldOf("ambient_light").orElse(0.0F)
					.forGetter(dt -> dt.ambientLight),
			Codec.unboundedMap(Codec.STRING, JAttributeValue.CODEC)
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

		JDimensionType t = new JDimensionType();
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

	private String infiniburn = "#minecraft:infiniburn_overworld";
	private float ambientLight = 0.0F;

	private Map<String, JAttributeValue> attributes = new LinkedHashMap<>();
	private TimelinesRef timelines;
	private Skybox skybox;
	private CardinalLightType cardinalLight;
	private Boolean hasFixedTime;

	// ----------------- factory -----------------

	public static JDimensionType dimensionType() {
		return new JDimensionType();
	}

	// ----------------- fluent builder API -----------------

	public JDimensionType hasSkylight(boolean value) {
		this.hasSkylight = value;
		return this;
	}

	public JDimensionType hasCeiling(boolean value) {
		this.hasCeiling = value;
		return this;
	}

	public JDimensionType coordinateScale(double scale) {
		this.coordinateScale = scale;
		return this;
	}

	public JDimensionType monsterSpawnLightConstant(int value) {
		this.monsterSpawnLightLevel = IntProviderLike.constant(value);
		return this;
	}

	public JDimensionType monsterSpawnLightUniform(int minInclusive, int maxInclusive) {
		this.monsterSpawnLightLevel = IntProviderLike.uniform(minInclusive, maxInclusive);
		return this;
	}

	public JDimensionType monsterSpawnBlockLightLimit(int limit) {
		this.monsterSpawnBlockLightLimit = limit;
		return this;
	}

	public JDimensionType minY(int minY) {
		this.minY = minY;
		return this;
	}

	public JDimensionType height(int height) {
		this.height = height;
		return this;
	}

	public JDimensionType logicalHeight(int logicalHeight) {
		this.logicalHeight = logicalHeight;
		return this;
	}

	public JDimensionType infiniburn(TagKey<Block> infiniburnTagOrBlock) {
		this.infiniburn = "#" + infiniburnTagOrBlock.location();
		return this;
	}

	public JDimensionType ambientLight(float value) {
		this.ambientLight = value;
		return this;
	}

	// ---- attributes ----

	public JDimensionType attribute(String key, boolean value) {
		if (key != null) {
			this.attributes.put(key, JAttributeValue.ofBoolean(value));
		}
		return this;
	}

	public JDimensionType attribute(String key, double value) {
		if (key != null) {
			this.attributes.put(key, JAttributeValue.ofDouble(value));
		}
		return this;
	}

	public JDimensionType attribute(String key, String value) {
		if (key != null && value != null) {
			this.attributes.put(key, JAttributeValue.ofString(value));
		}
		return this;
	}

	public <T> JDimensionType attribute(String key, Codec<T> codec, T value) {
		if (key != null && value != null) {
			this.attributes.put(key, JAttributeValue.ofEncoded(codec, value));
		}
		return this;
	}

	public JDimensionType attributes(JEnvironmentAttributes attributes) {
		if (attributes != null) {
			for (Map.Entry<String, net.vampirestudios.arrp.json.worldgen.EnvironmentAttributeValue> entry : attributes.getValues().entrySet()) {
				EnvironmentAttributeValueBridge.put(this.attributes, entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	public JDimensionType skyColor(String color) { return attribute(JEnvironmentAttributes.SKY_COLOR, color); }
	public JDimensionType fogColor(String color) { return attribute(JEnvironmentAttributes.FOG_COLOR, color); }
	public JDimensionType waterFogColor(String color) { return attribute(JEnvironmentAttributes.WATER_FOG_COLOR, color); }
	public JDimensionType cloudColor(String color) { return attribute(JEnvironmentAttributes.CLOUD_COLOR, color); }
	public JDimensionType cloudHeight(float value) { return attribute(JEnvironmentAttributes.CLOUD_HEIGHT, value); }
	public JDimensionType sunAngle(float value) { return attribute(JEnvironmentAttributes.SUN_ANGLE, value); }
	public JDimensionType moonAngle(float value) { return attribute(JEnvironmentAttributes.MOON_ANGLE, value); }
	public JDimensionType starAngle(float value) { return attribute(JEnvironmentAttributes.STAR_ANGLE, value); }
	public JDimensionType starBrightness(float value) { return attribute(JEnvironmentAttributes.STAR_BRIGHTNESS, value); }
	public JDimensionType skyLightColor(String color) { return attribute(JEnvironmentAttributes.SKY_LIGHT_COLOR, color); }
	public JDimensionType skyLightFactor(float value) { return attribute(JEnvironmentAttributes.SKY_LIGHT_FACTOR, value); }
	public JDimensionType skyLightLevel(float value) { return attribute(JEnvironmentAttributes.SKY_LIGHT_LEVEL, value); }
	public JDimensionType waterEvaporates(boolean value) { return attribute(JEnvironmentAttributes.WATER_EVAPORATES, value); }
	public JDimensionType bedRule(String value) { return attribute(JEnvironmentAttributes.BED_RULE, value); }
	public JDimensionType respawnAnchorWorks(boolean value) { return attribute(JEnvironmentAttributes.RESPAWN_ANCHOR_WORKS, value); }
	public JDimensionType netherPortalSpawnsPiglins(boolean value) { return attribute(JEnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, value); }
	public JDimensionType fastLava(boolean value) { return attribute(JEnvironmentAttributes.FAST_LAVA, value); }
	public JDimensionType piglinsZombify(boolean value) { return attribute(JEnvironmentAttributes.PIGLINS_ZOMBIFY, value); }
	public JDimensionType snowGolemMelts(boolean value) { return attribute(JEnvironmentAttributes.SNOW_GOLEM_MELTS, value); }
	public JDimensionType creakingActive(boolean value) { return attribute(JEnvironmentAttributes.CREAKING_ACTIVE, value); }

	public Map<String, JAttributeValue> attributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	private static final class EnvironmentAttributeValueBridge {
		private static void put(Map<String, JAttributeValue> attributes, String key, net.vampirestudios.arrp.json.worldgen.EnvironmentAttributeValue value) {
			if (value instanceof net.vampirestudios.arrp.json.worldgen.EnvironmentAttributeValue.BoolValue boolValue) {
				attributes.put(key, JAttributeValue.ofBoolean(boolValue.value));
			} else if (value instanceof net.vampirestudios.arrp.json.worldgen.EnvironmentAttributeValue.NumberValue numberValue) {
				attributes.put(key, JAttributeValue.ofDouble(numberValue.value));
			} else if (value instanceof net.vampirestudios.arrp.json.worldgen.EnvironmentAttributeValue.StringValue stringValue) {
				attributes.put(key, JAttributeValue.ofString(stringValue.value));
			}
		}
	}

	// ---- timelines / skybox / cardinal light ----

	/**
	 * Single timeline ID.
	 */
	public JDimensionType timeline(String id) {
		this.timelines = TimelinesRef.single(id);
		return this;
	}

	/**
	 * Multiple timeline IDs.
	 */
	public JDimensionType timelines(List<String> ids) {
		this.timelines = TimelinesRef.list(ids);
		return this;
	}

	/**
	 * Timeline tag, e.g. "#minecraft:day_cycle".
	 */
	public JDimensionType timelinesTag(String tag) {
		this.timelines = TimelinesRef.tag(tag);
		return this;
	}

	public JDimensionType skybox(Skybox skybox) {
		this.skybox = skybox;
		return this;
	}

	public JDimensionType cardinalLight(CardinalLightType cardinalLightType) {
		this.cardinalLight = cardinalLightType;
		return this;
	}

	public JDimensionType hasFixedTime(boolean value) {
		this.hasFixedTime = value;
		return this;
	}

	// ----------------- helper types -----------------

	public enum CardinalLightType implements StringRepresentable {
		DEFAULT("default"),
		NETHER("nether");

		public static final Codec<JDimensionType.CardinalLightType> CODEC = StringRepresentable.fromEnum(JDimensionType.CardinalLightType::values);
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

		public static final Codec<JDimensionType.Skybox> CODEC = StringRepresentable.fromEnum(JDimensionType.Skybox::values);
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
	 * Same AttributeValue union as in JBiome, duplicated here
	 * so the class stays self-contained.
	 */
	public record AttributeValue(Kind kind, boolean boolValue, double numberValue, String stringValue) {
		public static final Codec<AttributeValue> CODEC =
				Codec.either(Codec.BOOL, Codec.either(Codec.DOUBLE, Codec.STRING))
						.xmap(
								either -> {
									if (either.left().isPresent()) {
										return AttributeValue.of(either.left().get());
									}
									Either<Double, String> right = either.right().get();
									if (right.left().isPresent()) {
										return AttributeValue.of(right.left().get());
									}
									return AttributeValue.of(right.right().get());
								},
								value -> switch (value.kind) {
									case BOOLEAN -> Either.left(value.boolValue);
									case NUMBER -> Either.right(Either.left(value.numberValue));
									case STRING -> Either.right(Either.right(value.stringValue));
								}
						);

		public static AttributeValue of(boolean value) {
			return new AttributeValue(Kind.BOOLEAN, value, 0.0D, null);
		}

		public static AttributeValue of(double value) {
			return new AttributeValue(Kind.NUMBER, false, value, null);
		}

		public static AttributeValue of(String value) {
			if (value == null) throw new NullPointerException("value");
			return new AttributeValue(Kind.STRING, false, 0.0D, value);
		}


		public boolean asBoolean() {
			if (kind != Kind.BOOLEAN) throw new IllegalStateException("Not a boolean");
			return boolValue;
		}

		public double asNumber() {
			if (kind != Kind.NUMBER) throw new IllegalStateException("Not a number");
			return numberValue;
		}

		public String asString() {
			if (kind != Kind.STRING) throw new IllegalStateException("Not a string");
			return stringValue;
		}

		public enum Kind {BOOLEAN, NUMBER, STRING}
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

	public record IdWrapper(String id) {
		public static final Codec<IdWrapper> CODEC =
				Codec.STRING.xmap(IdWrapper::new, IdWrapper::id);
	}
}
