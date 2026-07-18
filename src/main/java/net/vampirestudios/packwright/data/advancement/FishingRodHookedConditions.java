package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:fishing_rod_hooked} */
public final class FishingRodHookedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("fishing_rod_hooked");

	public static final MapCodec<FishingRodHookedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			ItemPredicate.CODEC.optionalFieldOf("rod").forGetter(x -> Optional.ofNullable(x.rod)),
			AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity)),
			ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item))
	).apply(i, (player, rod, entity, item) -> {
		FishingRodHookedConditions out = new FishingRodHookedConditions();
		out.player = player.orElse(null);
		out.rod = rod.orElse(null);
		out.entity = entity.orElse(null);
		out.item = item.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private ItemPredicate rod;
	private EntityPredicate entity;
	private ItemPredicate item;

	public FishingRodHookedConditions() {
		super(TYPE.toString());
	}

	public static FishingRodHookedConditions fishedItem(ItemPredicate rod, EntityPredicate entity, ItemPredicate item) {
		FishingRodHookedConditions out = new FishingRodHookedConditions();
		out.rod = rod;
		out.entity = entity;
		out.item = item;
		return out;
	}

	public FishingRodHookedConditions player(Condition player) { this.player = player; return this; }

	public FishingRodHookedConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public ItemPredicate getRod() { return rod; }
	public EntityPredicate getEntity() { return entity; }
	public ItemPredicate getItem() { return item; }
}
