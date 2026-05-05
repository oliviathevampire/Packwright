package net.vampirestudios.arrp.json.worldgen;

import com.mojang.serialization.Codec;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map of Environment Attributes for a holder (biome, dimension, etc.):
 *
 * "attributes": {
 *   "minecraft:visual/fog_color": "#ffaaff",
 *   "minecraft:visual/cloud_opacity": 0.5,
 *   "minecraft:gameplay/water_evaporates": true
 * }
 *
 * Independent from JBiome so it can be reused elsewhere.
 */
public class JEnvironmentAttributes implements Cloneable {
    public static final String FOG_COLOR = "minecraft:visual/fog_color";
    public static final String FOG_START_DISTANCE = "minecraft:visual/fog_start_distance";
    public static final String FOG_END_DISTANCE = "minecraft:visual/fog_end_distance";
    public static final String SKY_FOG_END_DISTANCE = "minecraft:visual/sky_fog_end_distance";
    public static final String CLOUD_FOG_END_DISTANCE = "minecraft:visual/cloud_fog_end_distance";
    public static final String WATER_FOG_COLOR = "minecraft:visual/water_fog_color";
    public static final String WATER_FOG_START_DISTANCE = "minecraft:visual/water_fog_start_distance";
    public static final String WATER_FOG_END_DISTANCE = "minecraft:visual/water_fog_end_distance";
    public static final String SKY_COLOR = "minecraft:visual/sky_color";
    public static final String SUNRISE_SUNSET_COLOR = "minecraft:visual/sunrise_sunset_color";
    public static final String CLOUD_COLOR = "minecraft:visual/cloud_color";
    public static final String CLOUD_HEIGHT = "minecraft:visual/cloud_height";
    public static final String SUN_ANGLE = "minecraft:visual/sun_angle";
    public static final String MOON_ANGLE = "minecraft:visual/moon_angle";
    public static final String STAR_ANGLE = "minecraft:visual/star_angle";
    public static final String MOON_PHASE = "minecraft:visual/moon_phase";
    public static final String STAR_BRIGHTNESS = "minecraft:visual/star_brightness";
    public static final String BLOCK_LIGHT_TINT = "minecraft:visual/block_light_tint";
    public static final String SKY_LIGHT_COLOR = "minecraft:visual/sky_light_color";
    public static final String SKY_LIGHT_FACTOR = "minecraft:visual/sky_light_factor";
    public static final String NIGHT_VISION_COLOR = "minecraft:visual/night_vision_color";
    public static final String AMBIENT_LIGHT_COLOR = "minecraft:visual/ambient_light_color";
    public static final String DEFAULT_DRIPSTONE_PARTICLE = "minecraft:visual/default_dripstone_particle";
    public static final String AMBIENT_PARTICLES = "minecraft:visual/ambient_particles";
    public static final String BACKGROUND_MUSIC = "minecraft:audio/background_music";
    public static final String MUSIC_VOLUME = "minecraft:audio/music_volume";
    public static final String AMBIENT_SOUNDS = "minecraft:audio/ambient_sounds";
    public static final String FIREFLY_BUSH_SOUNDS = "minecraft:audio/firefly_bush_sounds";
    public static final String SKY_LIGHT_LEVEL = "minecraft:gameplay/sky_light_level";
    public static final String CAN_START_RAID = "minecraft:gameplay/can_start_raid";
    public static final String WATER_EVAPORATES = "minecraft:gameplay/water_evaporates";
    public static final String BED_RULE = "minecraft:gameplay/bed_rule";
    public static final String RESPAWN_ANCHOR_WORKS = "minecraft:gameplay/respawn_anchor_works";
    public static final String NETHER_PORTAL_SPAWNS_PIGLINS = "minecraft:gameplay/nether_portal_spawns_piglin";
    public static final String FAST_LAVA = "minecraft:gameplay/fast_lava";
    public static final String INCREASED_FIRE_BURNOUT = "minecraft:gameplay/increased_fire_burnout";
    public static final String EYEBLOSSOM_OPEN = "minecraft:gameplay/eyeblossom_open";
    public static final String TURTLE_EGG_HATCH_CHANCE = "minecraft:gameplay/turtle_egg_hatch_chance";
    public static final String PIGLINS_ZOMBIFY = "minecraft:gameplay/piglins_zombify";
    public static final String SNOW_GOLEM_MELTS = "minecraft:gameplay/snow_golem_melts";
    public static final String CREAKING_ACTIVE = "minecraft:gameplay/creaking_active";
    public static final String SURFACE_SLIME_SPAWN_CHANCE = "minecraft:gameplay/surface_slime_spawn_chance";
    public static final String CAT_WAKING_UP_GIFT_CHANCE = "minecraft:gameplay/cat_waking_up_gift_chance";
    public static final String BEES_STAY_IN_HIVE = "minecraft:gameplay/bees_stay_in_hive";
    public static final String MONSTERS_BURN = "minecraft:gameplay/monsters_burn";
    public static final String CAN_PILLAGER_PATROL_SPAWN = "minecraft:gameplay/can_pillager_patrol_spawn";
    public static final String VILLAGER_ACTIVITY = "minecraft:gameplay/villager_activity";
    public static final String BABY_VILLAGER_ACTIVITY = "minecraft:gameplay/baby_villager_activity";

    private Map<String, EnvironmentAttributeValue> values = new LinkedHashMap<>();

    public static final Codec<JEnvironmentAttributes> CODEC =
            Codec.unboundedMap(Codec.STRING, EnvironmentAttributeValue.CODEC)
                    .xmap(JEnvironmentAttributes::fromMap, JEnvironmentAttributes::toMap);

    private static JEnvironmentAttributes fromMap(Map<String, EnvironmentAttributeValue> map) {
        JEnvironmentAttributes attrs = new JEnvironmentAttributes();
        attrs.values.putAll(map);
        return attrs;
    }

    private Map<String, EnvironmentAttributeValue> toMap() {
        return new LinkedHashMap<>(this.values);
    }

    // ----------------- Builder-style setters -----------------

    public JEnvironmentAttributes putString(String key, String value) {
        if (key != null && value != null) {
            this.values.put(key, EnvironmentAttributeValue.ofString(value));
        }
        return this;
    }

    public JEnvironmentAttributes putNumber(String key, double value) {
        if (key != null) {
            this.values.put(key, EnvironmentAttributeValue.ofNumber(value));
        }
        return this;
    }

    public JEnvironmentAttributes putBoolean(String key, boolean value) {
        if (key != null) {
            this.values.put(key, EnvironmentAttributeValue.ofBoolean(value));
        }
        return this;
    }

    public JEnvironmentAttributes fogColor(String color) { return putString(FOG_COLOR, color); }
    public JEnvironmentAttributes fogStartDistance(float value) { return putNumber(FOG_START_DISTANCE, value); }
    public JEnvironmentAttributes fogEndDistance(float value) { return putNumber(FOG_END_DISTANCE, value); }
    public JEnvironmentAttributes skyFogEndDistance(float value) { return putNumber(SKY_FOG_END_DISTANCE, value); }
    public JEnvironmentAttributes cloudFogEndDistance(float value) { return putNumber(CLOUD_FOG_END_DISTANCE, value); }
    public JEnvironmentAttributes waterFogColor(String color) { return putString(WATER_FOG_COLOR, color); }
    public JEnvironmentAttributes waterFogStartDistance(float value) { return putNumber(WATER_FOG_START_DISTANCE, value); }
    public JEnvironmentAttributes waterFogEndDistance(float value) { return putNumber(WATER_FOG_END_DISTANCE, value); }
    public JEnvironmentAttributes skyColor(String color) { return putString(SKY_COLOR, color); }
    public JEnvironmentAttributes sunriseSunsetColor(String color) { return putString(SUNRISE_SUNSET_COLOR, color); }
    public JEnvironmentAttributes cloudColor(String color) { return putString(CLOUD_COLOR, color); }
    public JEnvironmentAttributes cloudHeight(float value) { return putNumber(CLOUD_HEIGHT, value); }
    public JEnvironmentAttributes sunAngle(float value) { return putNumber(SUN_ANGLE, value); }
    public JEnvironmentAttributes moonAngle(float value) { return putNumber(MOON_ANGLE, value); }
    public JEnvironmentAttributes starAngle(float value) { return putNumber(STAR_ANGLE, value); }
    public JEnvironmentAttributes moonPhase(String value) { return putString(MOON_PHASE, value); }
    public JEnvironmentAttributes starBrightness(float value) { return putNumber(STAR_BRIGHTNESS, value); }
    public JEnvironmentAttributes blockLightTint(String color) { return putString(BLOCK_LIGHT_TINT, color); }
    public JEnvironmentAttributes skyLightColor(String color) { return putString(SKY_LIGHT_COLOR, color); }
    public JEnvironmentAttributes skyLightFactor(float value) { return putNumber(SKY_LIGHT_FACTOR, value); }
    public JEnvironmentAttributes nightVisionColor(String color) { return putString(NIGHT_VISION_COLOR, color); }
    public JEnvironmentAttributes ambientLightColor(String color) { return putString(AMBIENT_LIGHT_COLOR, color); }
    public JEnvironmentAttributes musicVolume(float value) { return putNumber(MUSIC_VOLUME, value); }
    public JEnvironmentAttributes fireflyBushSounds(boolean value) { return putBoolean(FIREFLY_BUSH_SOUNDS, value); }
    public JEnvironmentAttributes skyLightLevel(float value) { return putNumber(SKY_LIGHT_LEVEL, value); }
    public JEnvironmentAttributes canStartRaid(boolean value) { return putBoolean(CAN_START_RAID, value); }
    public JEnvironmentAttributes waterEvaporates(boolean value) { return putBoolean(WATER_EVAPORATES, value); }
    public JEnvironmentAttributes bedRule(String value) { return putString(BED_RULE, value); }
    public JEnvironmentAttributes respawnAnchorWorks(boolean value) { return putBoolean(RESPAWN_ANCHOR_WORKS, value); }
    public JEnvironmentAttributes netherPortalSpawnsPiglins(boolean value) { return putBoolean(NETHER_PORTAL_SPAWNS_PIGLINS, value); }
    public JEnvironmentAttributes fastLava(boolean value) { return putBoolean(FAST_LAVA, value); }
    public JEnvironmentAttributes increasedFireBurnout(boolean value) { return putBoolean(INCREASED_FIRE_BURNOUT, value); }
    public JEnvironmentAttributes eyeblossomOpen(String value) { return putString(EYEBLOSSOM_OPEN, value); }
    public JEnvironmentAttributes turtleEggHatchChance(float value) { return putNumber(TURTLE_EGG_HATCH_CHANCE, value); }
    public JEnvironmentAttributes piglinsZombify(boolean value) { return putBoolean(PIGLINS_ZOMBIFY, value); }
    public JEnvironmentAttributes snowGolemMelts(boolean value) { return putBoolean(SNOW_GOLEM_MELTS, value); }
    public JEnvironmentAttributes creakingActive(boolean value) { return putBoolean(CREAKING_ACTIVE, value); }
    public JEnvironmentAttributes surfaceSlimeSpawnChance(float value) { return putNumber(SURFACE_SLIME_SPAWN_CHANCE, value); }
    public JEnvironmentAttributes catWakingUpGiftChance(float value) { return putNumber(CAT_WAKING_UP_GIFT_CHANCE, value); }
    public JEnvironmentAttributes beesStayInHive(boolean value) { return putBoolean(BEES_STAY_IN_HIVE, value); }
    public JEnvironmentAttributes monstersBurn(boolean value) { return putBoolean(MONSTERS_BURN, value); }
    public JEnvironmentAttributes canPillagerPatrolSpawn(boolean value) { return putBoolean(CAN_PILLAGER_PATROL_SPAWN, value); }
    public JEnvironmentAttributes villagerActivity(String value) { return putString(VILLAGER_ACTIVITY, value); }
    public JEnvironmentAttributes babyVillagerActivity(String value) { return putString(BABY_VILLAGER_ACTIVITY, value); }

    // ----------------- Accessors -----------------

    public Map<String, EnvironmentAttributeValue> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public EnvironmentAttributeValue get(String key) {
        return this.values.get(key);
    }

    @Override
    public JEnvironmentAttributes clone() {
        try {
            JEnvironmentAttributes clone = (JEnvironmentAttributes) super.clone();
            clone.values = new LinkedHashMap<>();
            clone.values.putAll(this.values);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
