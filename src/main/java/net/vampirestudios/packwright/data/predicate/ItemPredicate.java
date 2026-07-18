package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		if (itemsOrTag.length == 1 && itemsOrTag[0].startsWith("#")) {
			return parameter("items", itemsOrTag[0]);
		}
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
		subMap("predicates").put(id, wrapPredicateValue(value));
		return this;
	}

	/**
	 * matches item durability (max damage minus damage) and/or raw damage value
	 */
	public ItemPredicate damage(Range durability, Range damage) {
		Map<String, Object> value = new LinkedHashMap<>();
		if (durability != null) value.put("durability", durability.value());
		if (damage != null) value.put("damage", damage.value());
		return predicate("damage", value);
	}

	/**
	 * matches enchantments applied to the item; each entry is built with
	 * {@link #enchantmentEntry(List, Range)}
	 */
	@SafeVarargs
	public final ItemPredicate enchantments(Map<String, ?>... entries) {
		return predicate("enchantments", List.of(entries));
	}

	/**
	 * matches enchantments stored on the item (e.g. an enchanted book); each entry is built with
	 * {@link #enchantmentEntry(List, Range)}
	 */
	@SafeVarargs
	public final ItemPredicate storedEnchantments(Map<String, ?>... entries) {
		return predicate("stored_enchantments", List.of(entries));
	}

	/**
	 * builds a single enchantment-match entry for {@link #enchantments} / {@link #storedEnchantments};
	 * either or both of {@code enchantmentsOrTag} and {@code levels} may be {@code null} to match "any"
	 */
	public static Map<String, Object> enchantmentEntry(List<String> enchantmentsOrTag, Range levels) {
		Map<String, Object> entry = new LinkedHashMap<>();
		if (enchantmentsOrTag != null) entry.put("enchantments", enchantmentsOrTag);
		if (levels != null) entry.put("levels", levels.value());
		return entry;
	}

	/**
	 * matches potion contents by potion id/tag; the vanilla {@code effects} collection
	 * filter (contains/count/size) is simplified out here
	 */
	public ItemPredicate potionContents(String... potionsOrTag) {
		return predicate("potion_contents", Map.of("potions", List.of(potionsOrTag)));
	}

	/**
	 * matches the item's custom data, given as SNBT
	 */
	public ItemPredicate customData(String nbt) {
		return predicate("custom_data", nbt);
	}

	/**
	 * matches container contents by item sub-predicates ({@code contains} only —
	 * the {@code count}/{@code size} collection filters are simplified out here)
	 */
	public ItemPredicate container(ItemPredicate... items) {
		return predicate("container", Map.of("items", Map.of("contains", asMapList(items))));
	}

	/**
	 * matches bundle contents by item sub-predicates ({@code contains} only —
	 * the {@code count}/{@code size} collection filters are simplified out here)
	 */
	public ItemPredicate bundleContents(ItemPredicate... items) {
		return predicate("bundle_contents", Map.of("items", Map.of("contains", asMapList(items))));
	}

	/**
	 * matches a single firework explosion's shape/twinkle/trail; any parameter may be {@code null}
	 */
	public ItemPredicate fireworkExplosion(String shape, Boolean hasTwinkle, Boolean hasTrail) {
		Map<String, Object> value = new LinkedHashMap<>();
		if (shape != null) value.put("shape", shape);
		if (hasTwinkle != null) value.put("has_twinkle", hasTwinkle);
		if (hasTrail != null) value.put("has_trail", hasTrail);
		return predicate("firework_explosion", value);
	}

	/**
	 * matches flight duration only; the vanilla {@code explosions} collection filter
	 * is simplified out here
	 */
	public ItemPredicate fireworks(Range flightDuration) {
		return predicate("fireworks", Map.of("flight_duration", flightDuration.value()));
	}

	/**
	 * matches writable-book page contents by exact text ({@code contains} only —
	 * the {@code count}/{@code size} collection filters are simplified out here)
	 */
	public ItemPredicate writableBookContent(String... pageContents) {
		return predicate("writable_book_content", Map.of("pages", Map.of("contains", List.of(pageContents))));
	}

	/**
	 * matches written-book metadata; the vanilla {@code pages} collection filter
	 * (rich-text page contents) is simplified out here
	 */
	public ItemPredicate writtenBookContent(String author, String title, Range generation, Boolean resolved) {
		Map<String, Object> value = new LinkedHashMap<>();
		if (author != null) value.put("author", author);
		if (title != null) value.put("title", title);
		if (generation != null) value.put("generation", generation.value());
		if (resolved != null) value.put("resolved", resolved);
		return predicate("written_book_content", value);
	}

	/**
	 * matches attribute modifiers; each entry is built with {@link #attributeModifierEntry}
	 * ({@code contains} only — the {@code count}/{@code size} collection filters are
	 * simplified out here)
	 */
	@SafeVarargs
	public final ItemPredicate attributeModifiers(Map<String, ?>... entries) {
		return predicate("attribute_modifiers", Map.of("modifiers", Map.of("contains", List.of(entries))));
	}

	/**
	 * builds a single attribute-modifier match entry for {@link #attributeModifiers};
	 * any parameter may be {@code null} to match "any"
	 */
	public static Map<String, Object> attributeModifierEntry(String attributeOrTag, String modifierId, Range amount, String operation, String slot) {
		Map<String, Object> entry = new LinkedHashMap<>();
		if (attributeOrTag != null) entry.put("attribute", List.of(attributeOrTag));
		if (modifierId != null) entry.put("id", modifierId);
		if (amount != null) entry.put("amount", amount.value());
		if (operation != null) entry.put("operation", operation);
		if (slot != null) entry.put("slot", slot);
		return entry;
	}

	/**
	 * matches armor trim material and/or pattern by id/tag
	 */
	public ItemPredicate trim(List<String> materialsOrTag, List<String> patternsOrTag) {
		Map<String, Object> value = new LinkedHashMap<>();
		if (materialsOrTag != null) value.put("material", materialsOrTag);
		if (patternsOrTag != null) value.put("pattern", patternsOrTag);
		return predicate("trim", value);
	}

	public ItemPredicate jukeboxPlayable(String... songsOrTag) {
		return predicate("jukebox_playable", Map.of("song", List.of(songsOrTag)));
	}

	/**
	 * matches the villager variant this item represents (e.g. a villager spawn egg)
	 */
	public ItemPredicate villagerVariant(String... variantsOrTag) {
		return predicate("villager/variant", List.of(variantsOrTag));
	}

	private static List<Object> asMapList(ItemPredicate... items) {
		List<Object> list = new ArrayList<>(items.length);
		for (ItemPredicate item : items) {
			list.add(item.asMap());
		}
		return list;
	}
}
