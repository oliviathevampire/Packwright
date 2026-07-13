package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.ArrayList;
import java.util.List;

/** conditions for {@code minecraft:inventory_changed} */
public final class InventoryChangedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("inventory_changed");

	public static final MapCodec<InventoryChangedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(x -> x.items)
	).apply(i, items -> {
		InventoryChangedConditions out = new InventoryChangedConditions();
		out.items.addAll(items);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private final List<ItemPredicate> items = new ArrayList<>();

	public InventoryChangedConditions() {
		super(TYPE.toString());
	}

	public static InventoryChangedConditions inventoryChanged(ItemPredicate... anyOf) {
		InventoryChangedConditions out = new InventoryChangedConditions();
		if (anyOf != null) out.items.addAll(List.of(anyOf));
		return out;
	}

	public List<ItemPredicate> getItems() {
		return List.copyOf(items);
	}
}
