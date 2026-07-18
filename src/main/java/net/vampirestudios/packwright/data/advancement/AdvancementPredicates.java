package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.List;

final class AdvancementPredicates {
	static final Codec<Condition> CONDITION_CODEC = Condition.TYPE_CODEC;
	static final Codec<EntityPredicate> ENTITY_CODEC = Condition.TYPE_CODEC.xmap(
			condition -> EntityPredicate.of(),
			predicate -> Condition.entityProperties(EntityTarget.THIS, predicate)
	);
	static final Codec<List<Condition>> LOCATION_CODEC = Condition.TYPE_CODEC.xmap(
			List::of,
			conditions -> conditions.size() == 1
					? conditions.getFirst()
					: Condition.allOf(conditions.toArray(Condition[]::new))
	);

	private AdvancementPredicates() {
	}
}
