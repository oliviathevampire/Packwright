package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;
import net.vampirestudios.packwright.data.predicate.Range;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

	/**
	 * @param mode how {@code lines} merges into any existing lore; vanilla has no default for this
	 * field, so it must always be supplied
	 */
	public static LootFunction setLore(ListOperation mode, String... lines) {
		return of("minecraft:set_lore").mode(mode).parameter("lore", List.of(lines));
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

	/**
	 * adds attribute modifiers, e.g. weapon damage ({@code minecraft:set_attributes});
	 * build modifiers with {@link #attributeModifier(Identifier, Identifier, String, NumberProvider, String...)}
	 */
	@SafeVarargs
	public static LootFunction setAttributes(Map<String, ?>... modifiers) {
		return of("minecraft:set_attributes").parameter("modifiers", List.of(modifiers));
	}

	/**
	 * @param replace false to add to any existing modifiers instead of replacing them (vanilla default is true)
	 */
	@SafeVarargs
	public static LootFunction setAttributes(boolean replace, Map<String, ?>... modifiers) {
		return setAttributes(modifiers).parameter("replace", replace);
	}

	/**
	 * one entry of {@link #setAttributes}'s {@code modifiers}
	 *
	 * @param operation e.g. {@code "add_value"}, {@code "add_multiplied_base"}, {@code "add_multiplied_total"}
	 * @param slots the equipment slot group(s) this modifier applies in, e.g. {@code "mainhand"}, {@code "any"}
	 */
	public static Map<String, Object> attributeModifier(Identifier id, Identifier attribute, String operation, NumberProvider amount, String... slots) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", id.toString());
		map.put("attribute", attribute.toString());
		map.put("operation", operation);
		map.put("amount", amount.value());
		map.put("slot", List.of(slots));
		return map;
	}

	/**
	 * turns a map into a treasure map pointing at the nearest matching structure
	 * ({@code minecraft:exploration_map}); vanilla defaults to {@code #minecraft:on_treasure_maps},
	 * the woodland mansion icon, zoom 2, search radius 50, skipping known structures — set any of
	 * those explicitly with the chained setters below
	 */
	public static LootFunction makeExplorationMap() {
		return of("minecraft:exploration_map");
	}

	/**
	 * @param structureTag a structure tag, e.g. {@code "#minecraft:on_treasure_maps"}
	 */
	public LootFunction destination(String structureTag) {
		return parameter("destination", structureTag);
	}

	public LootFunction mapDecoration(Identifier decoration) {
		return parameter("decoration", decoration);
	}

	public LootFunction zoom(int zoom) {
		return parameter("zoom", zoom);
	}

	public LootFunction searchRadius(int radius) {
		return parameter("search_radius", radius);
	}

	public LootFunction skipExistingChunks(boolean skip) {
		return parameter("skip_existing_chunks", skip);
	}

	/**
	 * gives the suspicious stew a random chance of one of the added effects
	 * ({@code minecraft:set_stew_effect}); add effects with {@link #effect(Identifier, NumberProvider)}
	 */
	public static LootFunction stewEffect() {
		return of("minecraft:set_stew_effect");
	}

	/**
	 * adds a possible effect, for {@link #stewEffect()}
	 *
	 * @param duration in seconds
	 */
	public LootFunction effect(Identifier effect, NumberProvider duration) {
		Map<String, Object> entry = new LinkedHashMap<>();
		entry.put("type", effect.toString());
		entry.put("duration", duration.value());
		subList("effects").add(entry);
		return this;
	}

	/**
	 * sets the contents of a container-like data component, e.g. a bundle or shulker box
	 * ({@code minecraft:set_contents})
	 *
	 * @param component the data component's id, e.g. {@code "minecraft:container"},
	 * {@code "minecraft:bundle_contents"}, {@code "minecraft:charged_projectiles"}
	 */
	public static LootFunction setContents(String component, Entry... entries) {
		List<Map<String, Object>> list = new ArrayList<>(entries.length);
		for (Entry entry : entries) {
			list.add(entry.asMap());
		}
		return of("minecraft:set_contents").parameter("component", component).parameter("entries", list);
	}

	/**
	 * runs another function against each item already inside a container-like data component
	 * ({@code minecraft:modify_contents})
	 *
	 * @param component see {@link #setContents(String, Entry...)}
	 */
	public static LootFunction modifyContents(String component, LootFunction modifier) {
		return of("minecraft:modify_contents").parameter("component", component).parameter("modifier", modifier);
	}

	/**
	 * runs {@code onPass}/{@code onFail} depending on whether the item matches the filter
	 * ({@code minecraft:filtered}); chain {@link #onPass(LootFunction)}/{@link #onFail(LootFunction)}
	 */
	public static LootFunction filtered(ItemPredicate itemFilter) {
		return of("minecraft:filtered").parameter("item_filter", itemFilter);
	}

	public LootFunction onPass(LootFunction function) {
		return parameter("on_pass", function);
	}

	public LootFunction onFail(LootFunction function) {
		return parameter("on_fail", function);
	}

	/**
	 * sets a container's contained loot table, e.g. a shulker box that generates its own loot when
	 * opened ({@code minecraft:set_loot_table})
	 *
	 * @param blockEntityType the block entity type id, e.g. {@code "minecraft:shulker_box"}
	 */
	public static LootFunction setLootTable(Identifier lootTable, Identifier blockEntityType) {
		return of("minecraft:set_loot_table").parameter("name", lootTable).parameter("type", blockEntityType);
	}

	public static LootFunction setLootTable(Identifier lootTable, Identifier blockEntityType, long seed) {
		return setLootTable(lootTable, blockEntityType).parameter("seed", seed);
	}

	/**
	 * fills a player head with the killer/attacker's profile ({@code minecraft:fill_player_head})
	 */
	public static LootFunction fillPlayerHead(EntityTarget entity) {
		return of("minecraft:fill_player_head").parameter("entity", entity.id());
	}

	/**
	 * copies custom NBT data from a loot context entity ({@code minecraft:copy_custom_data});
	 * chain {@link #copy(String, String)} for each path to copy
	 */
	public static LootFunction copyCustomData(EntityTarget source) {
		return of("minecraft:copy_custom_data").parameter("source", source.id());
	}

	/**
	 * @param source a raw NBT source object, e.g. {@code {"type": "storage", "source": "mymod:data"}};
	 * this project has no typed builder for {@code NbtProvider} sources other than entities yet
	 */
	public static LootFunction copyCustomData(Map<String, ?> source) {
		return of("minecraft:copy_custom_data").parameter("source", source);
	}

	/**
	 * adds a copy operation, for {@link #copyCustomData}
	 *
	 * @param mergeStrategy {@code "replace"}, {@code "append"}, or {@code "merge"}
	 */
	public LootFunction copy(String sourcePath, String targetPath, String mergeStrategy) {
		Map<String, Object> op = new LinkedHashMap<>();
		op.put("source", sourcePath);
		op.put("target", targetPath);
		op.put("op", mergeStrategy);
		subList("ops").add(op);
		return this;
	}

	public LootFunction copy(String sourcePath, String targetPath) {
		return copy(sourcePath, targetPath, "replace");
	}

	/**
	 * sets or appends banner pattern layers ({@code minecraft:set_banner_pattern}); chain
	 * {@link #pattern(Identifier, String)} for each layer
	 *
	 * @param append true to add to any existing patterns instead of replacing them
	 */
	public static LootFunction setBannerPattern(boolean append) {
		return of("minecraft:set_banner_pattern").parameter("append", append);
	}

	/**
	 * adds a pattern layer, for {@link #setBannerPattern(boolean)}
	 *
	 * @param color a dye color name, e.g. {@code "white"}
	 */
	public LootFunction pattern(Identifier pattern, String color) {
		Map<String, Object> layer = new LinkedHashMap<>();
		layer.put("pattern", pattern.toString());
		layer.put("color", color);
		subList("patterns").add(layer);
		return this;
	}

	/**
	 * dyes the item with random dye colors, e.g. leather armor ({@code minecraft:set_random_dyes})
	 */
	public static LootFunction setRandomDyes(NumberProvider numberOfDyes) {
		return of("minecraft:set_random_dyes").parameter("number_of_dyes", numberOfDyes);
	}

	/**
	 * sets a random potion from the given options, or from all potions if none are given
	 * ({@code minecraft:set_random_potion})
	 *
	 * @param options potion ids, or a single {@code #tag}
	 */
	public static LootFunction setRandomPotion(String... options) {
		LootFunction function = of("minecraft:set_random_potion");
		if (options.length > 0) {
			function.parameter("options", List.of(options));
		}
		return function;
	}

	/**
	 * sets a random instrument from the given options ({@code minecraft:set_instrument})
	 *
	 * @param options instrument ids, or a single {@code #tag}
	 */
	public static LootFunction setInstrument(String... options) {
		return of("minecraft:set_instrument").parameter("options", List.of(options));
	}

	/**
	 * references a function file at {@code data/<namespace>/item_modifier/<path>.json}
	 * ({@code minecraft:reference})
	 */
	public static LootFunction reference(Identifier name) {
		return of("minecraft:reference").parameter("name", name);
	}

	/**
	 * runs several functions in order ({@code minecraft:sequence})
	 */
	public static LootFunction sequence(LootFunction... functions) {
		List<Map<String, Object>> list = new ArrayList<>(functions.length);
		for (LootFunction function : functions) {
			list.add(function.asMap());
		}
		return of("minecraft:sequence").parameter("functions", list);
	}

	/**
	 * sets a firework rocket's flight duration and/or explosions ({@code minecraft:set_fireworks});
	 * chain {@link #explosions(ListOperation, Map[])} for the explosions, built with
	 * {@link #fireworkExplosion}
	 */
	public static LootFunction setFireworks() {
		return of("minecraft:set_fireworks");
	}

	public LootFunction flightDuration(int flightDuration) {
		return parameter("flight_duration", flightDuration);
	}

	@SafeVarargs
	public final LootFunction explosions(ListOperation mode, Map<String, ?>... explosions) {
		Map<String, Object> standAlone = new LinkedHashMap<>(mode.value());
		standAlone.put("values", List.of(explosions));
		return parameter("explosions", standAlone);
	}

	/**
	 * one entry of {@link #explosions}, or the whole payload of {@link #setFireworkExplosion}
	 *
	 * @param shape e.g. {@code "small_ball"}, {@code "large_ball"}, {@code "star"}, {@code "creeper"}, {@code "burst"}
	 * @param colors RGB ints, or null
	 * @param fadeColors RGB ints, or null
	 */
	public static Map<String, Object> fireworkExplosion(String shape, List<Integer> colors, List<Integer> fadeColors, Boolean trail, Boolean twinkle) {
		Map<String, Object> map = new LinkedHashMap<>();
		if (shape != null) map.put("shape", shape);
		if (colors != null) map.put("colors", colors);
		if (fadeColors != null) map.put("fade_colors", fadeColors);
		if (trail != null) map.put("trail", trail);
		if (twinkle != null) map.put("twinkle", twinkle);
		return map;
	}

	/**
	 * overrides individual fields of the firework star's explosion ({@code minecraft:set_firework_explosion});
	 * all fields are optional and left unchanged if omitted
	 */
	public static LootFunction setFireworkExplosion(String shape, List<Integer> colors, List<Integer> fadeColors, Boolean trail, Boolean twinkle) {
		LootFunction function = of("minecraft:set_firework_explosion");
		function.values.putAll(fireworkExplosion(shape, colors, fadeColors, trail, twinkle));
		return function;
	}

	/**
	 * sets a written book's cover ({@code minecraft:set_book_cover}); all fields optional
	 */
	public static LootFunction setBookCover() {
		return of("minecraft:set_book_cover");
	}

	public LootFunction title(String title) {
		return parameter("title", Map.of("raw", title));
	}

	public LootFunction author(String author) {
		return parameter("author", author);
	}

	/**
	 * @param generation 0-3; 0 is the original, 3 is "tattered"
	 */
	public LootFunction generation(int generation) {
		return parameter("generation", generation);
	}

	/**
	 * sets a written book's pages ({@code minecraft:set_written_book_pages})
	 *
	 * @param mode how {@code pages} merges into any existing pages; vanilla has no default, so it
	 * must always be supplied
	 */
	public static LootFunction setWrittenBookPages(ListOperation mode, String... pages) {
		return of("minecraft:set_written_book_pages").mode(mode).parameter("pages", List.of(pages));
	}

	/**
	 * sets a writable (unsigned) book's pages ({@code minecraft:set_writable_book_pages})
	 *
	 * @param mode how {@code pages} merges into any existing pages; vanilla has no default, so it
	 * must always be supplied
	 */
	public static LootFunction setWritableBookPages(ListOperation mode, String... pages) {
		return of("minecraft:set_writable_book_pages").mode(mode).parameter("pages", List.of(pages));
	}

	/**
	 * shows or hides individual tooltip lines, e.g. hiding the enchantment tooltip
	 * ({@code minecraft:toggle_tooltips})
	 *
	 * @param toggles data component id -&gt; whether its tooltip is shown
	 */
	public static LootFunction toggleTooltips(Map<String, Boolean> toggles) {
		return of("minecraft:toggle_tooltips").parameter("toggles", toggles);
	}

	/**
	 * sets an ominous bottle's amplifier, clamped to 0-4 ({@code minecraft:set_ominous_bottle_amplifier})
	 */
	public static LootFunction setOminousBottleAmplifier(NumberProvider amplifier) {
		return of("minecraft:set_ominous_bottle_amplifier").parameter("amplifier", amplifier);
	}

	/**
	 * sets custom model data ({@code minecraft:set_custom_model_data}); all fields optional,
	 * chain {@link #floats}/{@link #flags}/{@link #strings}/{@link #colors}
	 */
	public static LootFunction setCustomModelData() {
		return of("minecraft:set_custom_model_data");
	}

	public LootFunction floats(ListOperation mode, NumberProvider... values) {
		List<Object> list = new ArrayList<>(values.length);
		for (NumberProvider value : values) {
			list.add(value.value());
		}
		return parameter("floats", standAlone(mode, list));
	}

	public LootFunction flags(ListOperation mode, boolean... values) {
		List<Object> list = new ArrayList<>(values.length);
		for (boolean value : values) {
			list.add(value);
		}
		return parameter("flags", standAlone(mode, list));
	}

	public LootFunction strings(ListOperation mode, String... values) {
		return parameter("strings", standAlone(mode, List.of(values)));
	}

	/**
	 * @param values a plain RGB int, wrapped as a constant {@link NumberProvider}
	 */
	public LootFunction colors(ListOperation mode, NumberProvider... values) {
		List<Object> list = new ArrayList<>(values.length);
		for (NumberProvider value : values) {
			list.add(value.value());
		}
		return parameter("colors", standAlone(mode, list));
	}

	private static Map<String, Object> standAlone(ListOperation mode, List<?> values) {
		Map<String, Object> map = new LinkedHashMap<>(mode.value());
		map.put("values", values);
		return map;
	}

	/**
	 * empties the item stack, removing it from the loot ({@code minecraft:discard})
	 */
	public static LootFunction discard() {
		return of("minecraft:discard");
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
	 * merges a {@link ListOperation}'s {@code mode} (and any {@code offset}/{@code size}) fields
	 * directly into this function, as required by {@code minecraft:set_lore},
	 * {@code minecraft:set_written_book_pages}, and {@code minecraft:set_writable_book_pages}
	 */
	public LootFunction mode(ListOperation mode) {
		this.values.putAll(mode.value());
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
