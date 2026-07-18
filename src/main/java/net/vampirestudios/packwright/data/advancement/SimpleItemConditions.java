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
 * conditions for the vanilla triggers whose conditions object is just
 * {@code (player, item)}: {@code minecraft:filled_bucket} ({@code FilledBucketTrigger}),
 * {@code minecraft:consume_item} ({@code ConsumeItemTrigger}), {@code minecraft:used_totem}
 * ({@code UsedTotemTrigger}), {@code minecraft:shot_crossbow} ({@code ShotCrossbowTrigger})
 * and {@code minecraft:using_item} ({@code UsingItemTrigger})
 */
public final class SimpleItemConditions extends CriterionConditions {
	public static final Identifier FILLED_BUCKET = Identifier.withDefaultNamespace("filled_bucket");
	public static final Identifier CONSUME_ITEM = Identifier.withDefaultNamespace("consume_item");
	public static final Identifier USED_TOTEM = Identifier.withDefaultNamespace("used_totem");
	public static final Identifier SHOT_CROSSBOW = Identifier.withDefaultNamespace("shot_crossbow");
	public static final Identifier USING_ITEM = Identifier.withDefaultNamespace("using_item");

	static {
		CriterionConditions.register(FILLED_BUCKET.toString(), mapCodec(FILLED_BUCKET).codec());
		CriterionConditions.register(CONSUME_ITEM.toString(), mapCodec(CONSUME_ITEM).codec());
		CriterionConditions.register(USED_TOTEM.toString(), mapCodec(USED_TOTEM).codec());
		CriterionConditions.register(SHOT_CROSSBOW.toString(), mapCodec(SHOT_CROSSBOW).codec());
		CriterionConditions.register(USING_ITEM.toString(), mapCodec(USING_ITEM).codec());
	}

	private static MapCodec<SimpleItemConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(x -> Optional.ofNullable(x.item))
		).apply(i, (player, item) -> {
			SimpleItemConditions out = new SimpleItemConditions(trigger);
			out.player = player.orElse(null);
			out.item = item.orElse(null);
			return out;
		}));
	}

	private Condition player;
	private ItemPredicate item;

	private SimpleItemConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static SimpleItemConditions filledBucket(ItemPredicate item) {
		SimpleItemConditions out = new SimpleItemConditions(FILLED_BUCKET);
		out.item = item;
		return out;
	}

	public static SimpleItemConditions usedItem(ItemPredicate item) {
		SimpleItemConditions out = new SimpleItemConditions(CONSUME_ITEM);
		out.item = item;
		return out;
	}

	public static SimpleItemConditions usedTotem(ItemPredicate item) {
		SimpleItemConditions out = new SimpleItemConditions(USED_TOTEM);
		out.item = item;
		return out;
	}

	public static SimpleItemConditions shotCrossbow(ItemPredicate item) {
		SimpleItemConditions out = new SimpleItemConditions(SHOT_CROSSBOW);
		out.item = item;
		return out;
	}

	public static SimpleItemConditions usingItem(ItemPredicate item) {
		SimpleItemConditions out = new SimpleItemConditions(USING_ITEM);
		out.item = item;
		return out;
	}

	public SimpleItemConditions player(Condition player) { this.player = player; return this; }

	public SimpleItemConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public ItemPredicate getItem() { return item; }
}
