package net.vampirestudios.packwright.data.loot.functions;

import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

public class FilteredFunction extends LootFunction {
	public FilteredFunction(ItemPredicate itemFilter) {
		super("minecraft:filtered");
		parameter("item_filter", itemFilter);
	}
}
