package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;

/**
 * A damage predicate, as used by the {@code minecraft:player_killed_entity} advancement
 * criterion's "killing_blow" and similar "how was I hurt" checks. Wraps a
 * {@link DamageSourcePredicate} plus how much damage was dealt/taken.
 */
public class DamagePredicate extends PredicateBuilder<DamagePredicate> {
	public static final Codec<DamagePredicate> CODEC = codecOf(DamagePredicate::new, null, "Damage predicate");

	public static DamagePredicate of() {
		return new DamagePredicate();
	}

	/**
	 * requires the damage source to match, e.g. {@code type(DamageSourcePredicate.of().tag("minecraft:is_fire", true))}
	 */
	public DamagePredicate type(DamageSourcePredicate source) {
		return parameter("type", source);
	}

	public DamagePredicate dealt(Range range) {
		return parameter("dealt", range);
	}

	public DamagePredicate taken(Range range) {
		return parameter("taken", range);
	}

	public DamagePredicate blocked(boolean blocked) {
		return parameter("blocked", blocked);
	}

	public DamagePredicate sourceEntity(EntityPredicate entity) {
		return parameter("source_entity", entity);
	}
}
