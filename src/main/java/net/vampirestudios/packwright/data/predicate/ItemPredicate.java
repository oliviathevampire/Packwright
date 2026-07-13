package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * An item predicate, as used by {@code minecraft:match_tool}, entity equipment checks,
 * and the {@code minecraft:inventory_changed} advancement criterion.
 */
public class ItemPredicate extends PredicateBuilder<ItemPredicate> {
	public static final Codec<ItemPredicate> CODEC = codecOf(ItemPredicate::new, null, "Item predicate");

	public static ItemPredicate of() {
		return new ItemPredicate();
	}

	/**
	 * matches any of the given item ids or {@code #tag}s
	 */
	public ItemPredicate items(String... itemsOrTag) {
		return parameter("items", List.of(itemsOrTag));
	}

	public ItemPredicate items(Identifier... items) {
		List<String> list = new ArrayList<>(items.length);
		for (Identifier item : items) {
			list.add(item.toString());
		}
		return parameter("items", list);
	}

	public ItemPredicate count(int count) {
		return parameter("count", count);
	}

	public ItemPredicate count(Range range) {
		return parameter("count", range);
	}

	/**
	 * requires an exact data component value, e.g.
	 * {@code component("minecraft:damage", 3)}
	 */
	public ItemPredicate component(String component, Object value) {
		subMap("components").put(component, value);
		return this;
	}

	/**
	 * adds an item sub-predicate, e.g.
	 * {@code predicate("minecraft:damage", Map.of("damage", Map.of("min", 1)))}
	 */
	public ItemPredicate predicate(String id, Object value) {
		subMap("predicates").put(id, value);
		return this;
	}
}
