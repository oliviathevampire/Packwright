package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:bee_nest_destroyed} */
public final class BeeNestDestroyedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("bee_nest_destroyed");

	public static final MapCodec<BeeNestDestroyedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Identifier.CODEC.optionalFieldOf("block").forGetter(x -> Optional.ofNullable(x.block)),
			ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item)),
			IntBound.CODEC.optionalFieldOf("num_bees_inside").forGetter(x -> Optional.ofNullable(x.beesInside))
	).apply(i, (player, block, item, beesInside) -> {
		BeeNestDestroyedConditions out = new BeeNestDestroyedConditions();
		out.player = player.orElse(null);
		out.block = block.orElse(null);
		out.item = item.orElse(null);
		out.beesInside = beesInside.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private Identifier block;
	private ItemPredicate item;
	private IntBound beesInside;

	public BeeNestDestroyedConditions() {
		super(TYPE.toString());
	}

	public static BeeNestDestroyedConditions destroyedBeeNest(Identifier block, ItemPredicate item, IntBound numBeesInside) {
		BeeNestDestroyedConditions out = new BeeNestDestroyedConditions();
		out.block = block;
		out.item = item;
		out.beesInside = numBeesInside;
		return out;
	}

	public BeeNestDestroyedConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public Identifier getBlock() { return block; }
	public ItemPredicate getItem() { return item; }
	public IntBound getBeesInside() { return beesInside; }
}
