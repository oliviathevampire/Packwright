package net.vampirestudios.arrp.data.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class EnvironmentAttributes {
    public static final Identifier FOG_COLOR = vanillaId("visual/fog_color");
    public static final Identifier FOG_START_DISTANCE = vanillaId("visual/fog_start_distance");
    public static final Identifier FOG_END_DISTANCE = vanillaId("visual/fog_end_distance");
    public static final Identifier SKY_FOG_END_DISTANCE = vanillaId("visual/sky_fog_end_distance");
    public static final Identifier CLOUD_FOG_END_DISTANCE = vanillaId("visual/cloud_fog_end_distance");
    public static final Identifier WATER_FOG_COLOR = vanillaId("visual/water_fog_color");
    public static final Identifier WATER_FOG_START_DISTANCE = vanillaId("visual/water_fog_start_distance");
    public static final Identifier WATER_FOG_END_DISTANCE = vanillaId("visual/water_fog_end_distance");
    public static final Identifier SKY_COLOR = vanillaId("visual/sky_color");
    public static final Identifier SUNRISE_SUNSET_COLOR = vanillaId("visual/sunrise_sunset_color");
    public static final Identifier CLOUD_COLOR = vanillaId("visual/cloud_color");
    public static final Identifier CLOUD_HEIGHT = vanillaId("visual/cloud_height");
    public static final Identifier SUN_ANGLE = vanillaId("visual/sun_angle");
    public static final Identifier MOON_ANGLE = vanillaId("visual/moon_angle");
    public static final Identifier STAR_ANGLE = vanillaId("visual/star_angle");
    public static final Identifier MOON_PHASE = vanillaId("visual/moon_phase");
    public static final Identifier STAR_BRIGHTNESS = vanillaId("visual/star_brightness");
    public static final Identifier BLOCK_LIGHT_TINT = vanillaId("visual/block_light_tint");
    public static final Identifier SKY_LIGHT_COLOR = vanillaId("visual/sky_light_color");
    public static final Identifier SKY_LIGHT_FACTOR = vanillaId("visual/sky_light_factor");
    public static final Identifier NIGHT_VISION_COLOR = vanillaId("visual/night_vision_color");
    public static final Identifier AMBIENT_LIGHT_COLOR = vanillaId("visual/ambient_light_color");
    public static final Identifier DEFAULT_DRIPSTONE_PARTICLE = vanillaId("visual/default_dripstone_particle");
    public static final Identifier AMBIENT_PARTICLES = vanillaId("visual/ambient_particles");
    public static final Identifier BACKGROUND_MUSIC = vanillaId("audio/background_music");
    public static final Identifier MUSIC_VOLUME = vanillaId("audio/music_volume");
    public static final Identifier AMBIENT_SOUNDS = vanillaId("audio/ambient_sounds");
    public static final Identifier FIREFLY_BUSH_SOUNDS = vanillaId("audio/firefly_bush_sounds");
    public static final Identifier SKY_LIGHT_LEVEL = vanillaId("gameplay/sky_light_level");
    public static final Identifier CAN_START_RAID = vanillaId("gameplay/can_start_raid");
    public static final Identifier WATER_EVAPORATES = vanillaId("gameplay/water_evaporates");
    public static final Identifier BED_RULE = vanillaId("gameplay/bed_rule");
    public static final Identifier RESPAWN_ANCHOR_WORKS = vanillaId("gameplay/respawn_anchor_works");
    public static final Identifier NETHER_PORTAL_SPAWNS_PIGLINS = vanillaId("gameplay/nether_portal_spawns_piglin");
    public static final Identifier FAST_LAVA = vanillaId("gameplay/fast_lava");
    public static final Identifier INCREASED_FIRE_BURNOUT = vanillaId("gameplay/increased_fire_burnout");
    public static final Identifier EYEBLOSSOM_OPEN = vanillaId("gameplay/eyeblossom_open");
    public static final Identifier TURTLE_EGG_HATCH_CHANCE = vanillaId("gameplay/turtle_egg_hatch_chance");
    public static final Identifier PIGLINS_ZOMBIFY = vanillaId("gameplay/piglins_zombify");
    public static final Identifier SNOW_GOLEM_MELTS = vanillaId("gameplay/snow_golem_melts");
    public static final Identifier CREAKING_ACTIVE = vanillaId("gameplay/creaking_active");
    public static final Identifier SURFACE_SLIME_SPAWN_CHANCE = vanillaId("gameplay/surface_slime_spawn_chance");
    public static final Identifier CAT_WAKING_UP_GIFT_CHANCE = vanillaId("gameplay/cat_waking_up_gift_chance");
    public static final Identifier BEES_STAY_IN_HIVE = vanillaId("gameplay/bees_stay_in_hive");
    public static final Identifier MONSTERS_BURN = vanillaId("gameplay/monsters_burn");
    public static final Identifier CAN_PILLAGER_PATROL_SPAWN = vanillaId("gameplay/can_pillager_patrol_spawn");
    public static final Identifier VILLAGER_ACTIVITY = vanillaId("gameplay/villager_activity");
    public static final Identifier BABY_VILLAGER_ACTIVITY = vanillaId("gameplay/baby_villager_activity");

    private Map<Identifier, EnvironmentAttributeValue> values = new LinkedHashMap<>();

    public static final Codec<EnvironmentAttributes> CODEC =
            Codec.unboundedMap(Identifier.CODEC, EnvironmentAttributeValue.CODEC)
                    .xmap(EnvironmentAttributes::fromMap, EnvironmentAttributes::toMap);

    private static EnvironmentAttributes fromMap(Map<Identifier, EnvironmentAttributeValue> map) {
        EnvironmentAttributes attrs = new EnvironmentAttributes();
        attrs.values.putAll(map);
        return attrs;
    }

    private Map<Identifier, EnvironmentAttributeValue> toMap() {
        return new LinkedHashMap<>(this.values);
    }

    // ----------------- Builder-style setters -----------------

    public EnvironmentAttributes set(Identifier key, String value) {
        if (key != null && value != null) {
            this.values.put(key, EnvironmentAttributeValue.ofString(value.toString()));
        }
        return this;
    }

    public EnvironmentAttributes set(Identifier key, Identifier value) {
        if (key != null && value != null) {
            this.values.put(key, EnvironmentAttributeValue.ofString(value.toString()));
        }
        return this;
    }

    public EnvironmentAttributes set(Identifier key, double value) {
        if (key != null) {
            this.values.put(key, EnvironmentAttributeValue.ofNumber(value));
        }
        return this;
    }

    public EnvironmentAttributes set(Identifier key, boolean value) {
        if (key != null) {
            this.values.put(key, EnvironmentAttributeValue.ofBoolean(value));
        }
        return this;
    }

    public EnvironmentAttributes fogColor(String color) { return set(FOG_COLOR, color); }
    public EnvironmentAttributes fogStartDistance(float value) { return set(FOG_START_DISTANCE, value); }
    public EnvironmentAttributes fogEndDistance(float value) { return set(FOG_END_DISTANCE, value); }
    public EnvironmentAttributes skyFogEndDistance(float value) { return set(SKY_FOG_END_DISTANCE, value); }
    public EnvironmentAttributes cloudFogEndDistance(float value) { return set(CLOUD_FOG_END_DISTANCE, value); }
    public EnvironmentAttributes waterFogColor(String color) { return set(WATER_FOG_COLOR, color); }
    public EnvironmentAttributes waterFogStartDistance(float value) { return set(WATER_FOG_START_DISTANCE, value); }
    public EnvironmentAttributes waterFogEndDistance(float value) { return set(WATER_FOG_END_DISTANCE, value); }
    public EnvironmentAttributes skyColor(String color) { return set(SKY_COLOR, color); }
    public EnvironmentAttributes sunriseSunsetColor(String color) { return set(SUNRISE_SUNSET_COLOR, color); }
    public EnvironmentAttributes cloudColor(String color) { return set(CLOUD_COLOR, color); }
    public EnvironmentAttributes cloudHeight(float value) { return set(CLOUD_HEIGHT, value); }
    public EnvironmentAttributes sunAngle(float value) { return set(SUN_ANGLE, value); }
    public EnvironmentAttributes moonAngle(float value) { return set(MOON_ANGLE, value); }
    public EnvironmentAttributes starAngle(float value) { return set(STAR_ANGLE, value); }
    public EnvironmentAttributes moonPhase(String value) { return set(MOON_PHASE, value); }
    public EnvironmentAttributes starBrightness(float value) { return set(STAR_BRIGHTNESS, value); }
    public EnvironmentAttributes blockLightTint(String color) { return set(BLOCK_LIGHT_TINT, color); }
    public EnvironmentAttributes skyLightColor(String color) { return set(SKY_LIGHT_COLOR, color); }
    public EnvironmentAttributes skyLightFactor(float value) { return set(SKY_LIGHT_FACTOR, value); }
    public EnvironmentAttributes nightVisionColor(String color) { return set(NIGHT_VISION_COLOR, color); }
    public EnvironmentAttributes ambientLightColor(String color) { return set(AMBIENT_LIGHT_COLOR, color); }
    public EnvironmentAttributes musicVolume(float value) { return set(MUSIC_VOLUME, value); }
    public EnvironmentAttributes fireflyBushSounds(boolean value) { return set(FIREFLY_BUSH_SOUNDS, value); }
    public EnvironmentAttributes skyLightLevel(float value) { return set(SKY_LIGHT_LEVEL, value); }
    public EnvironmentAttributes canStartRaid(boolean value) { return set(CAN_START_RAID, value); }
    public EnvironmentAttributes waterEvaporates(boolean value) { return set(WATER_EVAPORATES, value); }
    public EnvironmentAttributes bedRule(String value) { return set(BED_RULE, value); }
    public EnvironmentAttributes respawnAnchorWorks(boolean value) { return set(RESPAWN_ANCHOR_WORKS, value); }
    public EnvironmentAttributes netherPortalSpawnsPiglins(boolean value) { return set(NETHER_PORTAL_SPAWNS_PIGLINS, value); }
    public EnvironmentAttributes fastLava(boolean value) { return set(FAST_LAVA, value); }
    public EnvironmentAttributes increasedFireBurnout(boolean value) { return set(INCREASED_FIRE_BURNOUT, value); }
    public EnvironmentAttributes eyeblossomOpen(String value) { return set(EYEBLOSSOM_OPEN, value); }
    public EnvironmentAttributes turtleEggHatchChance(float value) { return set(TURTLE_EGG_HATCH_CHANCE, value); }
    public EnvironmentAttributes piglinsZombify(boolean value) { return set(PIGLINS_ZOMBIFY, value); }
    public EnvironmentAttributes snowGolemMelts(boolean value) { return set(SNOW_GOLEM_MELTS, value); }
    public EnvironmentAttributes creakingActive(boolean value) { return set(CREAKING_ACTIVE, value); }
    public EnvironmentAttributes surfaceSlimeSpawnChance(float value) { return set(SURFACE_SLIME_SPAWN_CHANCE, value); }
    public EnvironmentAttributes catWakingUpGiftChance(float value) { return set(CAT_WAKING_UP_GIFT_CHANCE, value); }
    public EnvironmentAttributes beesStayInHive(boolean value) { return set(BEES_STAY_IN_HIVE, value); }
    public EnvironmentAttributes monstersBurn(boolean value) { return set(MONSTERS_BURN, value); }
    public EnvironmentAttributes canPillagerPatrolSpawn(boolean value) { return set(CAN_PILLAGER_PATROL_SPAWN, value); }
    public EnvironmentAttributes villagerActivity(String value) { return set(VILLAGER_ACTIVITY, value); }
    public EnvironmentAttributes babyVillagerActivity(String value) { return set(BABY_VILLAGER_ACTIVITY, value); }

    // ----------------- Accessors -----------------

    public Map<Identifier, EnvironmentAttributeValue> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public EnvironmentAttributeValue get(Identifier key) {
        return this.values.get(key);
    }
}
