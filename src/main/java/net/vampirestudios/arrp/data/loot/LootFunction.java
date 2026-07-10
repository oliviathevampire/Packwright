package net.vampirestudios.arrp.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.data.predicate.PredicateBuilder;
import net.vampirestudios.arrp.data.predicate.Range;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

/**
 * A loot function (item modifier). The common vanilla function types have typed factories
 * ({@link #setCount(NumberProvider)}, {@link #enchantWithLevels(NumberProvider)},
 * {@link #explosionDecay()}, ...); anything else — modded function types, new vanilla
 * fields — can be built freely with {@link #of(String)} and the inherited
 * {@code parameter} methods.
 */
public class LootFunction extends PredicateBuilder<LootFunction> {
	public static final Codec<LootFunction> CODEC = codecOf(LootFunction::new, "function", "Loot function");

	/**
	 * @see LootTable#function(String)
	 */
	public LootFunction(String function) {
		function(function);
	}

	public LootFunction() {
	}

	// ---------- factories ----------

	/**
	 * a function of the given type, e.g. {@code "minecraft:set_count"} or a modded id
	 */
	public static LootFunction of(String function) {
		return new LootFunction(function);
	}

	public static LootFunction of(Identifier function) {
		return new LootFunction(function.toString());
	}

	public static LootFunction setCount(NumberProvider count) {
		return of("minecraft:set_count").parameter("count", count);
	}

	/**
	 * @param add true to add to the current count instead of replacing it
	 */
	public static LootFunction setCount(NumberProvider count, boolean add) {
		return setCount(count).parameter("add", add);
	}

	public static LootFunction setDamage(NumberProvider damage) {
		return of("minecraft:set_damage").parameter("damage", damage);
	}

	public static LootFunction setDamage(NumberProvider damage, boolean add) {
		return setDamage(damage).parameter("add", add);
	}

	/**
	 * reduces the count based on the explosion radius ({@code minecraft:explosion_decay})
	 */
	public static LootFunction explosionDecay() {
		return of("minecraft:explosion_decay");
	}

	/**
	 * smelts the item as if in a furnace ({@code minecraft:furnace_smelt})
	 */
	public static LootFunction furnaceSmelt() {
		return of("minecraft:furnace_smelt");
	}

	public static LootFunction setName(String name) {
		return of("minecraft:set_name").parameter("name", name);
	}

	public static LootFunction setLore(String... lines) {
		return of("minecraft:set_lore").parameter("lore", List.of(lines));
	}

	public static LootFunction setItem(Identifier item) {
		return of("minecraft:set_item").parameter("item", item);
	}

	public static LootFunction setPotion(Identifier potion) {
		return of("minecraft:set_potion").parameter("id", potion);
	}

	/**
	 * sets custom NBT data via an SNBT string ({@code minecraft:set_custom_data})
	 */
	public static LootFunction setCustomData(String snbt) {
		return of("minecraft:set_custom_data").parameter("tag", snbt);
	}

	/**
	 * sets data component values; values must be plain Java objects
	 */
	public static LootFunction setComponents(Map<String, ?> components) {
		return of("minecraft:set_components").parameter("components", components);
	}

	public static LootFunction enchantWithLevels(NumberProvider levels) {
		return of("minecraft:enchant_with_levels").parameter("levels", levels);
	}

	/**
	 * @param options the possible enchantment ids, or a single {@code #tag}
	 */
	public static LootFunction enchantWithLevels(NumberProvider levels, String... options) {
		return enchantWithLevels(levels).parameter("options", List.of(options));
	}

	public static LootFunction enchantRandomly() {
		return of("minecraft:enchant_randomly");
	}

	/**
	 * @param options the possible enchantment ids, or a single {@code #tag}
	 */
	public static LootFunction enchantRandomly(String... options) {
		return enchantRandomly().parameter("options", List.of(options));
	}

	/**
	 * adds an enchantment with an exact level; chain {@link #enchantment(String, NumberProvider)}
	 * for more ({@code minecraft:set_enchantments})
	 */
	public static LootFunction setEnchantments() {
		return of("minecraft:set_enchantments");
	}

	/**
	 * increases the count based on an enchantment level, e.g. looting
	 * ({@code minecraft:enchanted_count_increase})
	 */
	public static LootFunction enchantedCountIncrease(Identifier enchantment, NumberProvider count) {
		return of("minecraft:enchanted_count_increase")
				.parameter("enchantment", enchantment)
				.parameter("count", count);
	}

	public static LootFunction enchantedCountIncrease(Identifier enchantment, NumberProvider count, int limit) {
		return enchantedCountIncrease(enchantment, count).parameter("limit", limit);
	}

	/**
	 * applies the fortune formula used by ores ({@code minecraft:apply_bonus})
	 */
	public static LootFunction applyBonusOreDrops(Identifier enchantment) {
		return of("minecraft:apply_bonus")
				.parameter("enchantment", enchantment)
				.parameter("formula", "minecraft:ore_drops");
	}

	public static LootFunction applyBonusUniform(Identifier enchantment, int bonusMultiplier) {
		return of("minecraft:apply_bonus")
				.parameter("enchantment", enchantment)
				.parameter("formula", "minecraft:uniform_bonus_count")
				.parameter("parameters", Map.of("bonusMultiplier", bonusMultiplier));
	}

	public static LootFunction applyBonusBinomial(Identifier enchantment, int extra, float probability) {
		Map<String, Object> parameters = new java.util.LinkedHashMap<>();
		parameters.put("extra", extra);
		parameters.put("probability", probability);
		return of("minecraft:apply_bonus")
				.parameter("enchantment", enchantment)
				.parameter("formula", "minecraft:binomial_with_bonus_count")
				.parameter("parameters", parameters);
	}

	/**
	 * clamps the count into the given range ({@code minecraft:limit_count})
	 */
	public static LootFunction limitCount(Range limit) {
		return of("minecraft:limit_count").parameter("limit", limit);
	}

	/**
	 * copies the block entity's custom name onto the item ({@code minecraft:copy_name})
	 */
	public static LootFunction copyName() {
		return of("minecraft:copy_name").parameter("source", "block_entity");
	}

	/**
	 * copies block state properties into the item ({@code minecraft:copy_state})
	 */
	public static LootFunction copyState(Identifier block, String... properties) {
		return of("minecraft:copy_state")
				.parameter("block", block)
				.parameter("properties", List.of(properties));
	}

	/**
	 * copies data components from the block entity ({@code minecraft:copy_components})
	 */
	public static LootFunction copyComponents() {
		return of("minecraft:copy_components").parameter("source", "block_entity");
	}

	public static LootFunction copyComponents(String... include) {
		return copyComponents().parameter("include", List.of(include));
	}

	// ---------- builder ----------

	public LootFunction function(String function) {
		return parameter("function", function);
	}

	/**
	 * replaces all parameters with the given map, keeping the current function type
	 * unless the map specifies its own
	 */
	public LootFunction set(Map<String, ?> parameters) {
		Object function = this.values.get("function");
		this.values.clear();
		this.values.putAll(parameters);
		if (function != null) {
			this.values.putIfAbsent("function", function);
		}
		return this;
	}

	public LootFunction parameter(String key, NumberProvider value) {
		this.values.put(key, value.value());
		return this;
	}

	/**
	 * only applies this function when the condition passes
	 */
	public LootFunction condition(Condition condition) {
		subList("conditions").add(condition.asMap());
		return this;
	}

	public LootFunction condition(String condition) {
		return condition(new Condition(condition));
	}

	/**
	 * adds an enchantment level, for {@link #setEnchantments()}
	 */
	public LootFunction enchantment(String enchantment, NumberProvider level) {
		subMap("enchantments").put(enchantment, level.value());
		return this;
	}
}
