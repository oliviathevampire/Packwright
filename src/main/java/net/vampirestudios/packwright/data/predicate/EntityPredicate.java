package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

/**
 * An entity predicate, as used by {@code minecraft:entity_properties},
 * {@code minecraft:damage_source_properties}, and advancement criteria.
 */
public class EntityPredicate extends PredicateBuilder<EntityPredicate> {
	public static final Codec<EntityPredicate> CODEC = codecOf(EntityPredicate::new, null, "Entity predicate");

	public static EntityPredicate of() {
		return new EntityPredicate();
	}

	/**
	 * matches the entity type by id or {@code #tag}
	 */
	public EntityPredicate type(String typeOrTag) {
		return parameter("entity_type", typeOrTag);
	}

	public EntityPredicate type(Identifier type) {
		return parameter("entity_type", type);
	}

	public EntityPredicate nbt(String nbt) {
		return parameter("nbt", nbt);
	}

	public EntityPredicate team(String team) {
		return parameter("team", team);
	}

	public EntityPredicate onFire(boolean onFire) {
		subMap("flags").put("is_on_fire", onFire);
		return this;
	}

	public EntityPredicate sneaking(boolean sneaking) {
		subMap("flags").put("is_sneaking", sneaking);
		return this;
	}

	public EntityPredicate sprinting(boolean sprinting) {
		subMap("flags").put("is_sprinting", sprinting);
		return this;
	}

	public EntityPredicate swimming(boolean swimming) {
		subMap("flags").put("is_swimming", swimming);
		return this;
	}

	public EntityPredicate baby(boolean baby) {
		subMap("flags").put("is_baby", baby);
		return this;
	}

	/**
	 * requires an item in the given equipment slot: {@code mainhand}, {@code offhand},
	 * {@code head}, {@code chest}, {@code legs}, {@code feet} or {@code body}
	 */
	public EntityPredicate equipment(String slot, ItemPredicate item) {
		subMap("equipment").put(slot, item.asMap());
		return this;
	}

	/**
	 * requires the given status effect to be active
	 */
	public EntityPredicate effect(String effect) {
		subMap("effects").put(effect, Map.of());
		return this;
	}

	/**
	 * requires the given status effect with extra checks, e.g.
	 * {@code Map.of("amplifier", Map.of("min", 1))}
	 */
	public EntityPredicate effect(String effect, Map<String, ?> checks) {
		subMap("effects").put(effect, checks);
		return this;
	}

	public EntityPredicate location(LocationPredicate location) {
		return parameter("location", location);
	}

	public EntityPredicate steppingOn(LocationPredicate location) {
		return parameter("stepping_on", location);
	}

	/**
	 * matches the block the entity's movement is currently being affected by (e.g. honey, soul sand)
	 */
	public EntityPredicate movementAffectedBy(LocationPredicate location) {
		return parameter("movement_affected_by", location);
	}

	public EntityPredicate vehicle(EntityPredicate vehicle) {
		return parameter("vehicle", vehicle);
	}

	public EntityPredicate passenger(EntityPredicate passenger) {
		return parameter("passenger", passenger);
	}

	public EntityPredicate targetedEntity(EntityPredicate target) {
		return parameter("targeted_entity", target);
	}

	public EntityPredicate distanceX(Range range) {
		subMap("distance").put("x", range.value());
		return this;
	}

	public EntityPredicate distanceY(Range range) {
		subMap("distance").put("y", range.value());
		return this;
	}

	public EntityPredicate distanceZ(Range range) {
		subMap("distance").put("z", range.value());
		return this;
	}

	public EntityPredicate distanceHorizontal(Range range) {
		subMap("distance").put("horizontal", range.value());
		return this;
	}

	public EntityPredicate distanceAbsolute(Range range) {
		subMap("distance").put("absolute", range.value());
		return this;
	}

	public EntityPredicate movementX(Range range) {
		subMap("movement").put("x", range.value());
		return this;
	}

	public EntityPredicate movementY(Range range) {
		subMap("movement").put("y", range.value());
		return this;
	}

	public EntityPredicate movementZ(Range range) {
		subMap("movement").put("z", range.value());
		return this;
	}

	public EntityPredicate speed(Range range) {
		subMap("movement").put("speed", range.value());
		return this;
	}

	public EntityPredicate horizontalSpeed(Range range) {
		subMap("movement").put("horizontal_speed", range.value());
		return this;
	}

	public EntityPredicate verticalSpeed(Range range) {
		subMap("movement").put("vertical_speed", range.value());
		return this;
	}

	public EntityPredicate fallDistance(Range range) {
		subMap("movement").put("fall_distance", range.value());
		return this;
	}

	/**
	 * requires {@code entity.tickCount % period == 0}
	 */
	public EntityPredicate periodicTick(int period) {
		return parameter("periodic_tick", period);
	}

	/**
	 * requires an item matching the predicate in the given slot, e.g. {@code hotbar.0} or {@code armor}
	 */
	public EntityPredicate slot(String slot, ItemPredicate item) {
		subMap("slots").put(slot, item.asMap());
		return this;
	}

	/**
	 * requires an exact data component value, e.g.
	 * {@code components("minecraft:custom_name", "...")}
	 */
	public EntityPredicate components(String component, Object value) {
		subMap("components").put(component, value);
		return this;
	}

	/**
	 * adds a data-component sub-predicate, e.g.
	 * {@code predicate("minecraft:damage", Map.of("damage", Map.of("min", 1)))}
	 */
	public EntityPredicate predicate(String id, Object value) {
		subMap("predicates").put(id, wrapPredicateValue(value));
		return this;
	}

	public EntityPredicate entityTagsAnyOf(String... tags) {
		subMap("entity_tags").put("any_of", List.of(tags));
		return this;
	}

	public EntityPredicate entityTagsAllOf(String... tags) {
		subMap("entity_tags").put("all_of", List.of(tags));
		return this;
	}

	public EntityPredicate entityTagsNoneOf(String... tags) {
		subMap("entity_tags").put("none_of", List.of(tags));
		return this;
	}

	/**
	 * matches a lightning bolt by the number of blocks it set on fire
	 */
	public EntityPredicate lightningBlocksSetOnFire(Range range) {
		subMap("type_specific/lightning").put("blocks_set_on_fire", range.value());
		return this;
	}

	/**
	 * matches a lightning bolt by an entity it struck
	 */
	public EntityPredicate lightningEntityStruck(EntityPredicate entityStruck) {
		subMap("type_specific/lightning").put("entity_struck", entityStruck.asMap());
		return this;
	}

	/**
	 * matches a fishing hook's open-water state
	 */
	public EntityPredicate fishingHookInOpenWater(boolean inOpenWater) {
		subMap("type_specific/fishing_hook").put("in_open_water", inOpenWater);
		return this;
	}

	/**
	 * matches a player's experience level; simplified to {@code level} only —
	 * food, stats, recipes, advancements, looking_at and input are not exposed here
	 */
	public EntityPredicate playerLevel(Range range) {
		subMap("type_specific/player").put("level", range.value());
		return this;
	}

	/**
	 * matches a player's game mode(s), e.g. {@code "survival"}, {@code "creative"}
	 */
	public EntityPredicate playerGameMode(String... gameModes) {
		subMap("type_specific/player").put("gamemode", List.of(gameModes));
		return this;
	}

	/**
	 * matches an {@code AbstractCubeMob} (slime, magma cube) by size
	 */
	public EntityPredicate cubeMobSize(Range range) {
		subMap("type_specific/cube_mob").put("size", range.value());
		return this;
	}

	/**
	 * matches a raider's raid participation and captain status
	 */
	public EntityPredicate raider(boolean hasRaid, boolean isCaptain) {
		Map<String, Object> raider = subMap("type_specific/raider");
		raider.put("has_raid", hasRaid);
		raider.put("is_captain", isCaptain);
		return this;
	}

	/**
	 * matches a sheep's sheared state
	 */
	public EntityPredicate sheepSheared(boolean sheared) {
		subMap("type_specific/sheep").put("sheared", sheared);
		return this;
	}
}
