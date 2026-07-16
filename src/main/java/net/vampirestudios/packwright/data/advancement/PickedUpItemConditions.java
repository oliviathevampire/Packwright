package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:thrown_item_picked_up_by_entity} and
 * {@code minecraft:thrown_item_picked_up_by_player} — both backed by {@code PickedUpItemTrigger},
 * sharing the exact same {@code (player, item, entity)} shape
 */
public final class PickedUpItemConditions extends CriterionConditions {
	public static final Identifier THROWN_ITEM_PICKED_UP_BY_ENTITY = Identifier.withDefaultNamespace("thrown_item_picked_up_by_entity");
	public static final Identifier THROWN_ITEM_PICKED_UP_BY_PLAYER = Identifier.withDefaultNamespace("thrown_item_picked_up_by_player");

	static {
		CriterionConditions.register(THROWN_ITEM_PICKED_UP_BY_ENTITY.toString(), mapCodec(THROWN_ITEM_PICKED_UP_BY_ENTITY).codec());
		CriterionConditions.register(THROWN_ITEM_PICKED_UP_BY_PLAYER.toString(), mapCodec(THROWN_ITEM_PICKED_UP_BY_PLAYER).codec());
	}

	private static MapCodec<PickedUpItemConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item)),
				EntityPredicate.CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity))
		).apply(i, (player, item, entity) -> {
			PickedUpItemConditions out = new PickedUpItemConditions(trigger);
			out.player = player.orElse(null);
			out.item = item.orElse(null);
			out.entity = entity.orElse(null);
			return out;
		}));
	}

	private EntityPredicate player;
	private ItemPredicate item;
	private EntityPredicate entity;

	private PickedUpItemConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static PickedUpItemConditions thrownItemPickedUpByEntity(ItemPredicate item, EntityPredicate entity) {
		PickedUpItemConditions out = new PickedUpItemConditions(THROWN_ITEM_PICKED_UP_BY_ENTITY);
		out.item = item;
		out.entity = entity;
		return out;
	}

	public static PickedUpItemConditions thrownItemPickedUpByPlayer(ItemPredicate item, EntityPredicate entity) {
		PickedUpItemConditions out = new PickedUpItemConditions(THROWN_ITEM_PICKED_UP_BY_PLAYER);
		out.item = item;
		out.entity = entity;
		return out;
	}

	public PickedUpItemConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public ItemPredicate getItem() { return item; }
	public EntityPredicate getEntity() { return entity; }
}
