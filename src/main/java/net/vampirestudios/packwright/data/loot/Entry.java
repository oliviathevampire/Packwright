package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.data.loot.util.LootValue;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;
import net.minecraft.resources.Identifier;

/**
 * A loot pool entry. The vanilla entry types have typed factories ({@link #item(Identifier)},
 * {@link #alternatives(Entry...)}, {@link #lootTable(Identifier)}, ...); modded entry types
 * can be built with {@link #of(String)} and the inherited {@code parameter} methods.
 */
public class Entry extends PredicateBuilder<Entry> {
	public static final Codec<Entry> CODEC = codecOf(Entry::new, "type", "Loot entry");

	/**
	 * @see LootTable#entry()
	 */
	public Entry() {
	}

	// ---------- factories ----------

	/**
	 * an entry of the given type, e.g. {@code "minecraft:item"} or a modded id
	 */
	public static Entry of(String type) {
		return new Entry().type(type);
	}

	/**
	 * drops the given item ({@code minecraft:item})
	 */
	public static Entry item(String item) {
		return of("minecraft:item").name(item);
	}

	public static Entry item(Identifier item) {
		return item(item.toString());
	}

	/**
	 * drops items from a tag ({@code minecraft:tag})
	 *
	 * @param expand true to make one weighted sub-entry per item in the tag,
	 *               false to drop every item in the tag
	 */
	public static Entry tag(String tag, boolean expand) {
		return of("minecraft:tag").name(tag).expand(expand);
	}

	/**
	 * rolls another loot table ({@code minecraft:loot_table})
	 */
	public static Entry lootTable(Identifier table) {
		return of("minecraft:loot_table").parameter("value", table);
	}

	/**
	 * drops loot from the {@code minecraft:dynamic} drop source, e.g. {@code "minecraft:contents"}
	 */
	public static Entry dynamic(String name) {
		return of("minecraft:dynamic").name(name);
	}

	/**
	 * drops nothing ({@code minecraft:empty})
	 */
	public static Entry empty() {
		return of("minecraft:empty");
	}

	/**
	 * uses the first child whose conditions pass ({@code minecraft:alternatives})
	 */
	public static Entry alternatives(Entry... children) {
		return of("minecraft:alternatives").children(children);
	}

	/**
	 * uses all children ({@code minecraft:group})
	 */
	public static Entry group(Entry... children) {
		return of("minecraft:group").children(children);
	}

	/**
	 * uses children until one fails its conditions ({@code minecraft:sequence})
	 */
	public static Entry sequence(Entry... children) {
		return of("minecraft:sequence").children(children);
	}

	// ---------- builder ----------

	public Entry type(String type) {
		return parameter("type", type);
	}

	public Entry type(Identifier type) {
		return parameter("type", type);
	}

	public Entry name(String name) {
		return parameter("name", name);
	}

	public Entry name(Identifier name) {
		return parameter("name", name);
	}

	public Entry child(Entry child) {
		if (this == child) {
			throw new IllegalArgumentException("Can't add entry as its own child!");
		}
		subList("children").add(LootValue.encode(Entry.CODEC, child));
		return this;
	}

	public Entry children(Entry... children) {
		for (Entry child : children) {
			child(child);
		}
		return this;
	}

	public Entry expand(Boolean expand) {
		return parameter("expand", expand);
	}

	/** only applies this function when this entry is used */
	public Entry function(LootFunction function) {
		subList("functions").add(LootValue.encode(LootFunction.CODEC, function));
		return this;
	}

	public Entry function(String function) {
		return function(LootTable.function(function));
	}

	/** only uses this entry when the condition passes */
	public Entry condition(Condition condition) {
		subList("conditions").add(LootValue.encode(Condition.CODEC, condition));
		return this;
	}

	public Entry condition(String condition) {
		return condition(LootTable.predicate(condition));
	}

	/** references a predicate file at {@code data/<namespace>/predicate/<path>.json} by id, instead of embedding a condition inline */
	public Entry condition(Identifier reference) {
		return condition(Condition.reference(reference));
	}

	public Entry weight(Integer weight) {
		return parameter("weight", weight);
	}

	public Entry quality(Integer quality) {
		return parameter("quality", quality);
	}
}
