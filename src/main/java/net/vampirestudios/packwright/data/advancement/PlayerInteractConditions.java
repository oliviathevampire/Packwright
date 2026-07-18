package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:player_interacted_with_entity} and
 * {@code minecraft:player_sheared_equipment} — both backed by {@code PlayerInteractTrigger},
 * sharing the exact same {@code (player, item, entity)} shape
 */
public final class PlayerInteractConditions extends CriterionConditions {
	public static final Identifier PLAYER_INTERACTED_WITH_ENTITY = Identifier.withDefaultNamespace("player_interacted_with_entity");
	public static final Identifier PLAYER_SHEARED_EQUIPMENT = Identifier.withDefaultNamespace("player_sheared_equipment");

	static {
		CriterionConditions.register(PLAYER_INTERACTED_WITH_ENTITY.toString(), mapCodec(PLAYER_INTERACTED_WITH_ENTITY).codec());
		CriterionConditions.register(PLAYER_SHEARED_EQUIPMENT.toString(), mapCodec(PLAYER_SHEARED_EQUIPMENT).codec());
	}

	private static MapCodec<PlayerInteractConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item)),
				AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity))
		).apply(i, (player, item, entity) -> {
			PlayerInteractConditions out = new PlayerInteractConditions(trigger);
			out.player = player.orElse(null);
			out.item = item.orElse(null);
			out.entity = entity.orElse(null);
			return out;
		}));
	}

	private Condition player;
	private ItemPredicate item;
	private EntityPredicate entity;

	private PlayerInteractConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static PlayerInteractConditions itemUsedOnEntity(ItemPredicate item, EntityPredicate entity) {
		PlayerInteractConditions out = new PlayerInteractConditions(PLAYER_INTERACTED_WITH_ENTITY);
		out.item = item;
		out.entity = entity;
		return out;
	}

	public static PlayerInteractConditions equipmentSheared(ItemPredicate item, EntityPredicate entity) {
		PlayerInteractConditions out = new PlayerInteractConditions(PLAYER_SHEARED_EQUIPMENT);
		out.item = item;
		out.entity = entity;
		return out;
	}

	public PlayerInteractConditions player(Condition player) { this.player = player; return this; }

	public PlayerInteractConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public ItemPredicate getItem() { return item; }
	public EntityPredicate getEntity() { return entity; }
}
