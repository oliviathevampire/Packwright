package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A damage source predicate, as used by {@code minecraft:damage_source_properties} and
 * nested inside {@link DamagePredicate}.
 */
public class DamageSourcePredicate extends PredicateBuilder<DamageSourcePredicate> {
	public static final Codec<DamageSourcePredicate> CODEC = codecOf(DamageSourcePredicate::new, null, "Damage source predicate");

	public static DamageSourcePredicate of() {
		return new DamageSourcePredicate();
	}

	/**
	 * requires the damage type tag to match (or not match), e.g.
	 * {@code tag("minecraft:is_explosion", true)}
	 */
	public DamageSourcePredicate tag(String tag, boolean expected) {
		Map<String, Object> entry = new LinkedHashMap<>();
		entry.put("id", tag);
		entry.put("expected", expected);
		subList("tags").add(entry);
		return this;
	}

	public DamageSourcePredicate directEntity(EntityPredicate entity) {
		return parameter("direct_entity", entity);
	}

	public DamageSourcePredicate sourceEntity(EntityPredicate entity) {
		return parameter("source_entity", entity);
	}

	public DamageSourcePredicate isDirect(boolean direct) {
		return parameter("is_direct", direct);
	}
}
