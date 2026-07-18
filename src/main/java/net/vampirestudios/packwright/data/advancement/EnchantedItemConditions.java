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

/** conditions for {@code minecraft:enchanted_item} */
public final class EnchantedItemConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("enchanted_item");

	public static final MapCodec<EnchantedItemConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item)),
			IntBound.CODEC.optionalFieldOf("levels").forGetter(x -> Optional.ofNullable(x.levels))
	).apply(i, (player, item, levels) -> {
		EnchantedItemConditions out = new EnchantedItemConditions();
		out.player = player.orElse(null);
		out.item = item.orElse(null);
		out.levels = levels.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private ItemPredicate item;
	private IntBound levels;

	public EnchantedItemConditions() {
		super(TYPE.toString());
	}

	public static EnchantedItemConditions enchantedItem() {
		return new EnchantedItemConditions();
	}

	public static EnchantedItemConditions enchantedItem(ItemPredicate item, IntBound levels) {
		EnchantedItemConditions out = new EnchantedItemConditions();
		out.item = item;
		out.levels = levels;
		return out;
	}

	public EnchantedItemConditions player(Condition player) { this.player = player; return this; }

	public EnchantedItemConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public ItemPredicate getItem() { return item; }
	public IntBound getLevels() { return levels; }
}
