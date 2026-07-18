package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:summoned_entity} ({@code SummonedEntityTrigger}) and
 * {@code minecraft:tame_animal} ({@code TameAnimalTrigger}) — both share the exact same
 * {@code (player, entity)} shape
 */
public final class EntityTriggerConditions extends CriterionConditions {
	public static final Identifier SUMMONED_ENTITY = Identifier.withDefaultNamespace("summoned_entity");
	public static final Identifier TAME_ANIMAL = Identifier.withDefaultNamespace("tame_animal");

	static {
		CriterionConditions.register(SUMMONED_ENTITY.toString(), mapCodec(SUMMONED_ENTITY).codec());
		CriterionConditions.register(TAME_ANIMAL.toString(), mapCodec(TAME_ANIMAL).codec());
	}

	private static MapCodec<EntityTriggerConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity))
		).apply(i, (player, entity) -> {
			EntityTriggerConditions out = new EntityTriggerConditions(trigger);
			out.player = player.orElse(null);
			out.entity = entity.orElse(null);
			return out;
		}));
	}

	private Condition player;
	private EntityPredicate entity;

	private EntityTriggerConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static EntityTriggerConditions summonedEntity(EntityPredicate entity) {
		EntityTriggerConditions out = new EntityTriggerConditions(SUMMONED_ENTITY);
		out.entity = entity;
		return out;
	}

	public static EntityTriggerConditions tamedAnimal() {
		return new EntityTriggerConditions(TAME_ANIMAL);
	}

	public static EntityTriggerConditions tamedAnimal(EntityPredicate entity) {
		EntityTriggerConditions out = new EntityTriggerConditions(TAME_ANIMAL);
		out.entity = entity;
		return out;
	}

	public EntityTriggerConditions player(Condition player) { this.player = player; return this; }

	public EntityTriggerConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public EntityPredicate getEntity() { return entity; }
}
