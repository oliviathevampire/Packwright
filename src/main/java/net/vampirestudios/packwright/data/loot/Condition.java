package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.assets.models.Model;
import net.vampirestudios.packwright.data.predicate.BlockPredicate;
import net.vampirestudios.packwright.data.predicate.DamageSourcePredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;
import net.vampirestudios.packwright.data.predicate.Range;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A loot condition (predicate). The common vanilla condition types have typed factories
 * ({@link #randomChance(float)}, {@link #anyOf(Condition...)}, {@link #invert(Condition)},
 * {@link #matchTool(ItemPredicate)}, ...); anything else — modded condition types, new
 * vanilla fields — can be built freely with {@link #of(String)} and the inherited
 * {@code parameter} methods.
 */
public class Condition extends PredicateBuilder<Condition> {
	public static final Codec<Condition> CODEC = codecOf(Condition::new, "type", "Loot condition");

	/**
	 * @see LootTable#predicate(String)
	 * @see Model#condition()
	 */
	public Condition(String condition) {
		if (condition != null) {
			this.condition(condition);
		}
	}

	public Condition() {
	}

	// ---------- factories ----------

	/**
	 * a condition of the given type, e.g. {@code "minecraft:random_chance"} or a modded id
	 */
	public static Condition of(String condition) {
		return new Condition(condition);
	}

	public static Condition of(Identifier condition) {
		return new Condition(condition.toString());
	}

	public static Condition survivesExplosion() {
		return of("minecraft:survives_explosion");
	}

	public static Condition killedByPlayer() {
		return of("minecraft:killed_by_player");
	}

	public static Condition randomChance(float chance) {
		return of("minecraft:random_chance").parameter("chance", chance);
	}

	/**
	 * inverts the given condition ({@code minecraft:inverted})
	 */
	public static Condition invert(Condition term) {
		return of("minecraft:inverted").parameter("term", term);
	}

	/**
	 * passes if any of the given conditions pass ({@code minecraft:any_of})
	 */
	public static Condition anyOf(Condition... terms) {
		return of("minecraft:any_of").terms(terms);
	}

	/**
	 * passes if any of the referenced predicate files pass — each term is a bare
	 * {@code data/<namespace>/predicate/<path>.json} id, the way vanilla references
	 * e.g. {@code minecraft:tool/can_shear}
	 */
	public static Condition anyOf(String... predicateReferences) {
		return of("minecraft:any_of").parameter("terms", List.of((Object[]) predicateReferences));
	}

	/**
	 * passes if all the given conditions pass ({@code minecraft:all_of})
	 */
	public static Condition allOf(Condition... terms) {
		return of("minecraft:all_of").terms(terms);
	}

	// note: the standalone "minecraft:reference" condition type was removed in 26.3-snapshot-4 —
	// every place a condition is expected (e.g. LootFunction#condition(Identifier)) now accepts a
	// bare predicate-file id directly instead

	/**
	 * @param enchantment the enchantment whose level indexes into {@code chances}
	 * @param chances the chance per enchantment level, starting at level 0
	 */
	public static Condition tableBonus(Identifier enchantment, float... chances) {
		List<Float> list = new ArrayList<>(chances.length);
		for (float chance : chances) {
			list.add(chance);
		}
		return of("minecraft:table_bonus")
				.parameter("enchantment", enchantment)
				.parameter("chances", list);
	}

	/**
	 * matches the tool used
	 */
	public static Condition matchTool(ItemPredicate predicate) {
		return of("minecraft:match_tool").parameter("predicate", predicate);
	}

	/**
	 * matches the tool used against the given items (or a {@code #tag})
	 */
	public static Condition matchTool(String... items) {
		return matchTool(ItemPredicate.of().items(items));
	}

	/**
	 * matches a {@link BlockPredicate} against the loot context's block
	 * ({@code minecraft:match_block}, since 26.3-snapshot-4 — replaces the old flat
	 * {@code minecraft:block_state_property} shape). The predicate's fields
	 * ({@code blocks}, {@code state}, ...) sit directly on the condition object,
	 * exactly as {@code BlockPredicate.MAP_CODEC} reads them.
	 */
	public static Condition matchBlock(BlockPredicate predicate) {
		Condition condition = of("minecraft:match_block");
		predicate.asMap().forEach(condition::put);
		return condition;
	}

	/**
	 * matches any block state of the given block id
	 */
	public static Condition matchBlock(Identifier block) {
		return matchBlock(BlockPredicate.of().blocks(block));
	}

	/**
	 * checks properties of an entity in the loot context
	 */
	public static Condition entityProperties(EntityTarget entity, EntityPredicate predicate) {
		return of("minecraft:entity_properties")
				.parameter("entity", entity.id())
				.parameter("predicate", predicate);
	}

	/**
	 * checks scoreboard scores of an entity in the loot context;
	 * add them with {@link #score(String, Range)}
	 */
	public static Condition entityScores(EntityTarget entity) {
		return of("minecraft:entity_scores").parameter("entity", entity.id());
	}

	public static Condition damageSourceProperties(DamageSourcePredicate predicate) {
		return of("minecraft:damage_source_properties").parameter("predicate", predicate);
	}

	public static Condition locationCheck(LocationPredicate predicate) {
		return of("minecraft:location_check").parameter("predicate", predicate);
	}

	/**
	 * checks the location at a block offset from the loot context's position
	 */
	public static Condition locationCheck(LocationPredicate predicate, int offsetX, int offsetY, int offsetZ) {
		return locationCheck(predicate)
				.parameter("offsetX", offsetX)
				.parameter("offsetY", offsetY)
				.parameter("offsetZ", offsetZ);
	}

	public static Condition weatherCheck(Boolean raining, Boolean thundering) {
		Condition condition = of("minecraft:weather_check");
		if (raining != null) {
			condition.parameter("raining", raining);
		}
		if (thundering != null) {
			condition.parameter("thundering", thundering);
		}
		return condition;
	}

	/**
	 * like {@link #randomChance(float)}, but the chance increases with the attacking entity's
	 * looting (or given) enchantment level ({@code minecraft:random_chance_with_enchanted_bonus},
	 * since 26.3)
	 *
	 * @param enchantedChance a constant chance used at any enchantment level above 0; for a curve
	 * that varies with level, use {@link #randomChanceWithEnchantedBonus(float, Object, Identifier)}
	 * with a raw {@code LevelBasedValue} object
	 */
	public static Condition randomChanceWithEnchantedBonus(float unenchantedChance, float enchantedChance, Identifier enchantment) {
		return randomChanceWithEnchantedBonus(unenchantedChance, (Object) enchantedChance, enchantment);
	}

	/**
	 * @param enchantedChance a plain float (constant), or a {@code LevelBasedValue} map/object;
	 * this project has no typed builder for {@code LevelBasedValue} yet, so it is passed through as-is
	 */
	public static Condition randomChanceWithEnchantedBonus(float unenchantedChance, Object enchantedChance, Identifier enchantment) {
		return of("minecraft:random_chance_with_enchanted_bonus")
				.parameter("unenchanted_chance", unenchantedChance)
				.put("enchanted_chance", enchantedChance)
				.parameter("enchantment", enchantment);
	}

	/**
	 * checks the world clock's current time ({@code minecraft:time_check}, since 26.3)
	 *
	 * @param clock a world clock id, e.g. {@code "minecraft:overworld"} or {@code "minecraft:the_end"}
	 */
	public static Condition timeCheck(String clock, Range value) {
		return of("minecraft:time_check").parameter("clock", clock).parameter("value", value);
	}

	/**
	 * @param period wraps the clock's time with {@code time % period} before checking it
	 */
	public static Condition timeCheck(String clock, Range value, long period) {
		return timeCheck(clock, value).parameter("period", period);
	}

	/**
	 * checks a number provider's value against a range ({@code minecraft:value_check}, since 26.3)
	 */
	public static Condition valueCheck(NumberProvider value, Range range) {
		return of("minecraft:value_check").parameter("value", value).parameter("range", range);
	}

	/**
	 * checks whether the loot context's enchantment (e.g. of an enchanted book being applied)
	 * is active ({@code minecraft:enchantment_active_check}, since 26.3)
	 */
	public static Condition enchantmentActiveCheck(boolean active) {
		return of("minecraft:enchantment_active_check").parameter("active", active);
	}

	/**
	 * checks a boolean environment attribute, e.g. {@code gameplay/can_start_raid}
	 * ({@code minecraft:environment_attribute_check}, since 26.3)
	 */
	public static Condition environmentAttributeCheck(Identifier attribute, boolean value) {
		return of("minecraft:environment_attribute_check").parameter("attribute", attribute).parameter("value", value);
	}

	/**
	 * checks a numeric environment attribute, e.g. {@code visual/star_brightness}
	 */
	public static Condition environmentAttributeCheck(Identifier attribute, double value) {
		return of("minecraft:environment_attribute_check").parameter("attribute", attribute).parameter("value", value);
	}

	/**
	 * checks a string/enum environment attribute, e.g. {@code visual/moon_phase}
	 */
	public static Condition environmentAttributeCheck(Identifier attribute, String value) {
		return of("minecraft:environment_attribute_check").parameter("attribute", attribute).parameter("value", value);
	}

	// ---------- builder ----------

	public Condition condition(String condition) {
		return parameter("type", condition);
	}

	/**
	 * replaces all parameters with the given map, keeping the current condition type
	 * unless the map specifies its own
	 */
	public Condition set(Map<String, ?> parameters) {
		Object type = this.values.get("type");
		this.values.clear();
		this.values.putAll(parameters);
		if (type != null) {
			this.values.putIfAbsent("type", type);
		}
		return this;
	}

	/**
	 * sets a list of nested conditions as a parameter
	 */
	public Condition parameter(String key, Condition... values) {
		List<Map<String, Object>> list = new ArrayList<>(values.length);
		for (Condition value : values) {
			list.add(value.values);
		}
		return parameter(key, list);
	}

	/**
	 * sets the {@code "terms"} sub-conditions, as used by {@code minecraft:any_of} and {@code minecraft:all_of}
	 */
	public Condition terms(Condition... conditions) {
		return parameter("terms", conditions);
	}

	/**
	 * adds a scoreboard objective check, for {@link #entityScores(EntityTarget)}
	 */
	public Condition score(String objective, Range range) {
		subMap("scores").put(objective, range.value());
		return this;
	}

}
