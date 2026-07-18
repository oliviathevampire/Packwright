package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** conditions for {@code minecraft:inventory_changed} */
public final class InventoryChangedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("inventory_changed");

	/**
	 * the "slots" sub-object: occupied/full/empty slot counts, each an {@link IntBound}
	 * that defaults to unbounded when absent
	 */
	private record Slots(Optional<IntBound> occupied, Optional<IntBound> full, Optional<IntBound> empty) {
		static final Codec<Slots> CODEC = RecordCodecBuilder.create(i -> i.group(
				IntBound.CODEC.optionalFieldOf("occupied").forGetter(Slots::occupied),
				IntBound.CODEC.optionalFieldOf("full").forGetter(Slots::full),
				IntBound.CODEC.optionalFieldOf("empty").forGetter(Slots::empty)
		).apply(i, Slots::new));

		boolean isAny() { return occupied.isEmpty() && full.isEmpty() && empty.isEmpty(); }
	}

	public static final MapCodec<InventoryChangedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Slots.CODEC.optionalFieldOf("slots").forGetter(x -> {
				Slots slots = new Slots(Optional.ofNullable(x.slotsOccupied), Optional.ofNullable(x.slotsFull), Optional.ofNullable(x.slotsEmpty));
				return slots.isAny() ? Optional.empty() : Optional.of(slots);
			}),
			ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(x -> x.items)
	).apply(i, (player, slots, items) -> {
		InventoryChangedConditions out = new InventoryChangedConditions();
		out.player = player.orElse(null);
		slots.ifPresent(s -> {
			out.slotsOccupied = s.occupied().orElse(null);
			out.slotsFull = s.full().orElse(null);
			out.slotsEmpty = s.empty().orElse(null);
		});
		out.items.addAll(items);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private IntBound slotsOccupied;
	private IntBound slotsFull;
	private IntBound slotsEmpty;
	private final List<ItemPredicate> items = new ArrayList<>();

	public InventoryChangedConditions() {
		super(TYPE.toString());
	}

	public static InventoryChangedConditions inventoryChanged(ItemPredicate... anyOf) {
		InventoryChangedConditions out = new InventoryChangedConditions();
		if (anyOf != null) out.items.addAll(List.of(anyOf));
		return out;
	}

	public InventoryChangedConditions player(Condition player) { this.player = player; return this; }

	public InventoryChangedConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	/**
	 * requires the number of occupied/full/empty inventory slots to be within these
	 * bounds; pass {@code null} for a bound to leave it unrestricted
	 */
	public InventoryChangedConditions slots(IntBound occupied, IntBound full, IntBound empty) {
		this.slotsOccupied = occupied;
		this.slotsFull = full;
		this.slotsEmpty = empty;
		return this;
	}

	public Condition getPlayer() { return player; }
	public IntBound getSlotsOccupied() { return slotsOccupied; }
	public IntBound getSlotsFull() { return slotsFull; }
	public IntBound getSlotsEmpty() { return slotsEmpty; }
	public List<ItemPredicate> getItems() {
		return List.copyOf(items);
	}
}
