package net.vampirestudios.arrp.data.predicate;

import net.minecraft.resources.Identifier;

import java.util.Map;

/**
 * An entity predicate, as used by {@code minecraft:entity_properties} and
 * {@code minecraft:damage_source_properties}.
 */
public class EntityPredicate extends PredicateBuilder<EntityPredicate> {

	public static EntityPredicate of() {
		return new EntityPredicate();
	}

	/**
	 * matches the entity type by id or {@code #tag}
	 */
	public EntityPredicate type(String typeOrTag) {
		return parameter("type", typeOrTag);
	}

	public EntityPredicate type(Identifier type) {
		return parameter("type", type);
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

	public EntityPredicate vehicle(EntityPredicate vehicle) {
		return parameter("vehicle", vehicle);
	}

	public EntityPredicate passenger(EntityPredicate passenger) {
		return parameter("passenger", passenger);
	}

	public EntityPredicate targetedEntity(EntityPredicate target) {
		return parameter("targeted_entity", target);
	}
}
