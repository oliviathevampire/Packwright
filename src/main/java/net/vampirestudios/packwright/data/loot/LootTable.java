package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A loot table. Create one with a typed factory ({@link #block()}, {@link #entity()},
 * {@link #chest()}, ...) or {@link #loot(String)}, then add pools:
 * <pre>{@code
 * LootTable.block()
 *     .pool(Pool.of().rolls(1)
 *         .entry(Entry.item("mymod:my_block")
 *             .condition(Condition.survivesExplosion())))
 * }</pre>
 */
public class LootTable extends PredicateBuilder<LootTable> {
	public static final Codec<LootTable> CODEC = codecOf(LootTable::new, null, "Loot table");

	private final List<Pool> pools = new ArrayList<>();

	public LootTable() {
	}

	/**
	 * @see LootTable#loot(String)
	 */
	public LootTable(String type) {
		if (type != null) {
			this.type(type);
		}
	}

	public LootTable(String type, List<Pool> pools) {
		this(type);
		for (Pool pool : pools) {
			this.pool(pool);
		}
	}

	// ---------- factories ----------

	public static LootTable loot(String type) {
		return new LootTable(type);
	}

	public static LootTable block() {
		return loot("minecraft:block");
	}

	public static LootTable entity() {
		return loot("minecraft:entity");
	}

	public static LootTable chest() {
		return loot("minecraft:chest");
	}

	public static LootTable fishing() {
		return loot("minecraft:fishing");
	}

	public static LootTable gift() {
		return loot("minecraft:gift");
	}

	public static LootTable barterTable() {
		return loot("minecraft:barter");
	}

	public static LootTable archaeology() {
		return loot("minecraft:archaeology");
	}

	public static LootTable generic() {
		return loot("minecraft:generic");
	}

	/** Single-item block drop: one pool, one roll, one item entry. */
	public static LootTable dropping(Identifier item) {
		return block().pool(Pool.of().rolls(1).entry(Entry.item(item)));
	}

	public static LootTable dropping(String item) {
		return dropping(Identifier.parse(item));
	}

	/**
	 * Single-item block drop that is destroyed by explosions, like most vanilla blocks.
	 */
	public static LootTable droppingSurvivesExplosion(Identifier item) {
		return block().pool(Pool.of().rolls(1)
				.entry(Entry.item(item).condition(Condition.survivesExplosion())));
	}

	/**
	 * Drops one of two items depending on whether the tool has silk touch, e.g. glass vs nothing
	 * or stone vs cobblestone.
	 */
	public static LootTable droppingWithSilkTouch(Identifier withSilkTouch, Identifier withoutSilkTouch) {
		return block().pool(Pool.of().rolls(1)
				.entry(Entry.alternatives(
						Entry.item(withSilkTouch).condition(hasSilkTouch()),
						Entry.item(withoutSilkTouch).condition(Condition.survivesExplosion()))));
	}

	/**
	 * the standard vanilla "tool has silk touch" check
	 */
	public static Condition hasSilkTouch() {
		return Condition.matchTool(ItemPredicate.of()
				.predicate("minecraft:enchantments", List.of(Map.of(
						"enchantments", "minecraft:silk_touch",
						"levels", Map.of("min", 1)))));
	}

	public static Entry entry() {
		return new Entry();
	}

	/**
	 * @param condition the predicate's condition identifier
	 */
	public static Condition predicate(String condition) {
		return new Condition(condition);
	}

	public static LootFunction function(String function) {
		return new LootFunction(function);
	}

	public static Pool pool() {
		return new Pool();
	}

	// ---------- builder ----------

	public LootTable type(String type) {
		return parameter("type", type);
	}

	public LootTable type(Identifier type) {
		return parameter("type", type);
	}

	public LootTable pool(Pool pool) {
		this.pools.add(pool);
		subList("pools").add(pool.asMap());
		return this;
	}

	public LootTable pools(Pool... pools) {
		for (Pool pool : pools) {
			pool(pool);
		}
		return this;
	}

	/**
	 * adds a table-wide function, applied to every item this table drops
	 */
	public LootTable function(LootFunction function) {
		subList("functions").add(function.asMap());
		return this;
	}

	public LootTable randomSequence(Identifier id) {
		return parameter("random_sequence", id);
	}

	public LootTable randomSequence(String id) {
		return parameter("random_sequence", id);
	}

	public Identifier getRandomSequence() {
		Object id = this.values.get("random_sequence");
		return id == null ? null : Identifier.tryParse(String.valueOf(id));
	}

	public String getType() {
		Object type = this.values.get("type");
		return type == null ? null : String.valueOf(type);
	}

	public List<Pool> getPools() {
		return List.copyOf(this.pools);
	}
}
