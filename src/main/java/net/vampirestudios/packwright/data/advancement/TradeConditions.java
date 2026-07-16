package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:villager_trade} */
public final class TradeConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("villager_trade");

	public static final MapCodec<TradeConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			EntityPredicate.CODEC.optionalFieldOf("villager").forGetter(x -> Optional.ofNullable(x.villager)),
			ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item))
	).apply(i, (player, villager, item) -> {
		TradeConditions out = new TradeConditions();
		out.player = player.orElse(null);
		out.villager = villager.orElse(null);
		out.item = item.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private EntityPredicate villager;
	private ItemPredicate item;

	public TradeConditions() {
		super(TYPE.toString());
	}

	public static TradeConditions tradedWithVillager() {
		return new TradeConditions();
	}

	public TradeConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public EntityPredicate getVillager() { return villager; }
	public ItemPredicate getItem() { return item; }
}
