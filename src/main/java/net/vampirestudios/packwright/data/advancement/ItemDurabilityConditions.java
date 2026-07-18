package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:item_durability_changed} */
public final class ItemDurabilityConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("item_durability_changed");

	public static final MapCodec<ItemDurabilityConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item)),
			IntBound.CODEC.optionalFieldOf("durability").forGetter(x -> Optional.ofNullable(x.durability)),
			IntBound.CODEC.optionalFieldOf("delta").forGetter(x -> Optional.ofNullable(x.delta))
	).apply(i, (player, item, durability, delta) -> {
		ItemDurabilityConditions out = new ItemDurabilityConditions();
		out.player = player.orElse(null);
		out.item = item.orElse(null);
		out.durability = durability.orElse(null);
		out.delta = delta.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private ItemPredicate item;
	private IntBound durability;
	private IntBound delta;

	public ItemDurabilityConditions() {
		super(TYPE.toString());
	}

	public static ItemDurabilityConditions changedDurability(ItemPredicate item, IntBound durability) {
		ItemDurabilityConditions out = new ItemDurabilityConditions();
		out.item = item;
		out.durability = durability;
		return out;
	}

	public ItemDurabilityConditions player(Condition player) { this.player = player; return this; }

	public ItemDurabilityConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public ItemPredicate getItem() { return item; }
	public IntBound getDurability() { return durability; }
	public IntBound getDelta() { return delta; }
}
