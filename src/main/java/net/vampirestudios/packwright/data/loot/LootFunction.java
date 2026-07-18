package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.functions.*;
import net.vampirestudios.packwright.data.loot.providers.nbt.LootNbtSource;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.loot.util.LootValue;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;
import net.vampirestudios.packwright.data.predicate.Range;

import java.util.ArrayList;
import java.util.List;

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
		return new CustomLootFunction(function);
	}

	public static LootFunction of(Identifier function) {
		return new CustomLootFunction(function.toString());
	}

	public static LootFunction setCount(NumberProvider count) {
		return new SetCountFunction(count);
	}

	/**
	 * @param add true to add to the current count instead of replacing it
	 */
	public static LootFunction setCount(NumberProvider count, boolean add) {
		return new SetCountFunction(count, add);
	}

	public static LootFunction setDamage(NumberProvider damage) {
		return new SetDamageFunction(damage);
	}

	public static LootFunction setDamage(NumberProvider damage, boolean add) {
		return new SetDamageFunction(damage, add);
	}

	/**
	 * reduces the count based on the explosion radius ({@code minecraft:explosion_decay})
	 */
	public static LootFunction explosionDecay() {
		return new ApplyExplosionDecayFunction();
	}

	/**
	 * smelts the item as if in a furnace ({@code minecraft:furnace_smelt})
	 */
	public static LootFunction furnaceSmelt() {
		return new FurnaceSmeltFunction();
	}

	public static LootFunction setName(String name) {
		return new SetNameFunction(name);
	}

	public static LootFunction setName(String name, LootNameTarget target) {
		return new SetNameFunction(name, target);
	}

	public static LootFunction setName(String name, LootNameTarget target, EntityTarget entity) {
		return new SetNameFunction(name, target, entity);
	}

	/**
	 * @param mode how {@code lines} merges into any existing lore; vanilla has no default for this
	 * field, so it must always be supplied
	 */
	public static LootFunction setLore(ListOperation mode, String... lines) {
		return new SetLoreFunction(mode, lines);
	}

	public static LootFunction setLore(ListOperation mode, EntityTarget entity, String... lines) {
		return new SetLoreFunction(mode, entity, lines);
	}

	public static LootFunction setItem(Identifier item) {
		return new SetItemFunction(item);
	}

	public static LootFunction setPotion(Identifier potion) {
		return new SetPotionFunction(potion);
	}

	/**
	 * sets custom NBT data via an SNBT string ({@code minecraft:set_custom_data})
	 */
	public static LootFunction setCustomData(String snbt) {
		return new SetCustomDataFunction(snbt);
	}

	public static LootFunction setComponents() {
		return new SetComponentsFunction();
	}

	public LootFunction component(String component, Object value) {
		subMap("components").put(component, value);
		return this;
	}

	public static LootFunction enchantWithLevels(NumberProvider levels) {
		return new EnchantWithLevelsFunction(levels);
	}

	/**
	 * @param options the possible enchantment ids, or a single {@code #tag}
	 */
	public static LootFunction enchantWithLevels(NumberProvider levels, String... options) {
		return new EnchantWithLevelsFunction(levels, options);
	}

	public static LootFunction enchantRandomly() {
		return new EnchantRandomlyFunction();
	}

	/**
	 * @param options the possible enchantment ids, or a single {@code #tag}
	 */
	public static LootFunction enchantRandomly(String... options) {
		return new EnchantRandomlyFunction(options);
	}

	/**
	 * adds an enchantment with an exact level; chain {@link #enchantment(String, NumberProvider)}
	 * for more ({@code minecraft:set_enchantments})
	 */
	public static LootFunction setEnchantments() {
		return new SetEnchantmentsFunction();
	}

	/**
	 * increases the count based on an enchantment level, e.g. looting
	 * ({@code minecraft:enchanted_count_increase})
	 */
	public static LootFunction enchantedCountIncrease(Identifier enchantment, NumberProvider count) {
		return new EnchantedCountIncreaseFunction(enchantment, count);
	}

	public static LootFunction enchantedCountIncrease(Identifier enchantment, NumberProvider count, int limit) {
		return new EnchantedCountIncreaseFunction(enchantment, count, limit);
	}

	/**
	 * applies the fortune formula used by ores ({@code minecraft:apply_bonus})
	 */
	public static LootFunction applyBonusOreDrops(Identifier enchantment) {
		return applyBonus(enchantment, ApplyBonusFormula.oreDrops());
	}

	public static LootFunction applyBonusUniform(Identifier enchantment, int bonusMultiplier) {
		return applyBonus(enchantment, ApplyBonusFormula.uniformBonusCount(bonusMultiplier));
	}

	public static LootFunction applyBonusBinomial(Identifier enchantment, int extra, float probability) {
		return applyBonus(enchantment, ApplyBonusFormula.binomialWithBonusCount(extra, probability));
	}

	public static LootFunction applyBonus(Identifier enchantment, ApplyBonusFormula formula) {
		return new ApplyBonusCountFunction(enchantment, formula);
	}

	/**
	 * clamps the count into the given range ({@code minecraft:limit_count})
	 */
	public static LootFunction limitCount(Range limit) {
		return new LimitCountFunction(limit);
	}

	/**
	 * copies the block entity's custom name onto the item ({@code minecraft:copy_name})
	 */
	public static LootFunction copyName() {
		return new CopyNameFunction();
	}

	/**
	 * copies block state properties into the item ({@code minecraft:copy_state})
	 */
	public static LootFunction copyState(Identifier block, String... properties) {
		return new CopyStateFunction(block, properties);
	}

	/**
	 * copies data components from the block entity ({@code minecraft:copy_components})
	 */
	public static LootFunction copyComponents() {
		return new CopyComponentsFunction();
	}

	public static LootFunction copyComponentsFrom(EntityTarget source) {
		return new CopyComponentsFunction(source);
	}

	public static LootFunction copyComponentsFrom(String source) {
		return new CopyComponentsFunction(source);
	}

	public static LootFunction copyComponents(String... include) {
		return copyComponents().parameter("include", List.of(include));
	}

	public LootFunction includeComponents(String... include) {
		return parameter("include", List.of(include));
	}

	public LootFunction excludeComponents(String... exclude) {
		return parameter("exclude", List.of(exclude));
	}

	/**
	 * adds attribute modifiers, e.g. weapon damage ({@code minecraft:set_attributes});
	 * build modifiers with {@link #attributeModifier(Identifier, Identifier, String, NumberProvider, String...)}
	 */
	public static LootFunction setAttributes(LootAttributeModifier... modifiers) {
		return new SetAttributesFunction(modifiers);
	}

	/**
	 * @param replace false to add to any existing modifiers instead of replacing them (vanilla default is true)
	 */
	public static LootFunction setAttributes(boolean replace, LootAttributeModifier... modifiers) {
		return new SetAttributesFunction(replace, modifiers);
	}

	/**
	 * one entry of {@link #setAttributes}'s {@code modifiers}
	 *
	 * @param operation e.g. {@code "add_value"}, {@code "add_multiplied_base"}, {@code "add_multiplied_total"}
	 * @param slots the equipment slot group(s) this modifier applies in, e.g. {@code "mainhand"}, {@code "any"}
	 */
	public static LootAttributeModifier attributeModifier(Identifier id, Identifier attribute, String operation, NumberProvider amount, String... slots) {
		return new LootAttributeModifier(id, attribute, operation, amount, slots);
	}

	/**
	 * turns a map into a treasure map pointing at the nearest matching structure
	 * ({@code minecraft:exploration_map}); vanilla defaults to {@code #minecraft:on_treasure_maps},
	 * the woodland mansion icon, zoom 2, search radius 50, skipping known structures — set any of
	 * those explicitly with the chained setters below
	 */
	public static LootFunction makeExplorationMap() {
		return new ExplorationMapFunction();
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
		return new SetStewEffectFunction();
	}

	/**
	 * adds a possible effect, for {@link #stewEffect()}
	 *
	 * @param duration in seconds
	 */
	public LootFunction effect(Identifier effect, NumberProvider duration) {
		return effect(new StewEffectEntry(effect, duration));
	}

	public LootFunction effect(StewEffectEntry effect) {
		subList("effects").add(effect.value());
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
		return new SetContentsFunction(component, entries);
	}

	/**
	 * runs another function against each item already inside a container-like data component
	 * ({@code minecraft:modify_contents})
	 *
	 * @param component see {@link #setContents(String, Entry...)}
	 */
	public static LootFunction modifyContents(String component, LootFunction modifier) {
		return new ModifyContentsFunction(component, modifier);
	}

	/**
	 * runs {@code onPass}/{@code onFail} depending on whether the item matches the filter
	 * ({@code minecraft:filtered}); chain {@link #onPass(LootFunction)}/{@link #onFail(LootFunction)}
	 */
	public static LootFunction filtered(ItemPredicate itemFilter) {
		return new FilteredFunction(itemFilter);
	}

	public LootFunction onPass(LootFunction function) {
		return put("on_pass", LootValue.encode(LootFunction.CODEC, function));
	}

	public LootFunction onFail(LootFunction function) {
		return put("on_fail", LootValue.encode(LootFunction.CODEC, function));
	}

	/**
	 * sets a container's contained loot table, e.g. a shulker box that generates its own loot when
	 * opened ({@code minecraft:set_loot_table})
	 *
	 * @param blockEntityType the block entity type id, e.g. {@code "minecraft:shulker_box"}
	 */
	public static LootFunction setLootTable(Identifier lootTable, Identifier blockEntityType) {
		return new SetLootTableFunction(lootTable, blockEntityType);
	}

	public static LootFunction setLootTable(Identifier lootTable, Identifier blockEntityType, long seed) {
		return new SetLootTableFunction(lootTable, blockEntityType, seed);
	}

	/**
	 * fills a player head with the killer/attacker's profile ({@code minecraft:fill_player_head})
	 */
	public static LootFunction fillPlayerHead(EntityTarget entity) {
		return new FillPlayerHeadFunction(entity);
	}

	/**
	 * copies custom NBT data from a loot context entity ({@code minecraft:copy_custom_data});
	 * chain {@link #copy(String, String)} for each path to copy
	 */
	public static LootFunction copyCustomData(EntityTarget source) {
		return copyCustomData(LootNbtSource.context(source));
	}

	public static LootFunction copyCustomData(LootNbtSource source) {
		return new CopyCustomDataFunction(source);
	}

	/**
	 * adds a copy operation, for {@link #copyCustomData}
	 *
	 * @param mergeStrategy {@code "replace"}, {@code "append"}, or {@code "merge"}
	 */
	public LootFunction copy(String sourcePath, String targetPath, String mergeStrategy) {
		return copy(new CopyOperation(sourcePath, targetPath, mergeStrategy));
	}

	public LootFunction copy(CopyOperation operation) {
		subList("ops").add(operation.value());
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
		return new SetBannerPatternFunction(append);
	}

	/**
	 * adds a pattern layer, for {@link #setBannerPattern(boolean)}
	 *
	 * @param color a dye color name, e.g. {@code "white"}
	 */
	public LootFunction pattern(Identifier pattern, String color) {
		return pattern(new BannerPatternLayer(pattern, color));
	}

	public LootFunction pattern(BannerPatternLayer layer) {
		subList("patterns").add(layer.value());
		return this;
	}

	/**
	 * dyes the item with random dye colors, e.g. leather armor ({@code minecraft:set_random_dyes})
	 */
	public static LootFunction setRandomDyes(NumberProvider numberOfDyes) {
		return new SetRandomDyesFunction(numberOfDyes);
	}

	/**
	 * sets a random potion from the given options, or from all potions if none are given
	 * ({@code minecraft:set_random_potion})
	 *
	 * @param options potion ids, or a single {@code #tag}
	 */
	public static LootFunction setRandomPotion(String... options) {
		return new SetRandomPotionFunction(options);
	}

	/**
	 * sets a random instrument from the given options ({@code minecraft:set_instrument})
	 *
	 * @param options instrument ids, or a single {@code #tag}
	 */
	public static LootFunction setInstrument(String... options) {
		return new SetInstrumentFunction(options);
	}

	/**
	 * runs several functions in order ({@code minecraft:sequence})
	 */
	public static LootFunction sequence(LootFunction... functions) {
		return new SequenceFunction(functions);
	}

	/**
	 * sets a firework rocket's flight duration and/or explosions ({@code minecraft:set_fireworks});
	 * chain {@link #explosions(ListOperation, LootFireworkExplosion...)} for the explosions, built
	 * with {@link #fireworkExplosion}
	 */
	public static LootFunction setFireworks() {
		return new SetFireworksFunction();
	}

	public LootFunction flightDuration(int flightDuration) {
		return parameter("flight_duration", flightDuration);
	}

	public final LootFunction explosions(ListOperation mode, LootFireworkExplosion... explosions) {
		return put("explosions", StandaloneListOperation.value(LootFireworkExplosion.COMPONENT_CODEC, mode, List.of(explosions)));
	}

	/**
	 * one entry of {@link #explosions}, or the whole payload of {@link #setFireworkExplosion}
	 *
	 * @param shape e.g. {@code "small_ball"}, {@code "large_ball"}, {@code "star"}, {@code "creeper"}, {@code "burst"}
	 * @param colors RGB ints, or null
	 * @param fadeColors RGB ints, or null
	 */
	public static LootFireworkExplosion fireworkExplosion(String shape, List<Integer> colors, List<Integer> fadeColors, Boolean trail, Boolean twinkle) {
		return new LootFireworkExplosion(shape, colors, fadeColors, trail, twinkle);
	}

	/**
	 * overrides individual fields of the firework star's explosion ({@code minecraft:set_firework_explosion});
	 * all fields are optional and left unchanged if omitted
	 */
	public static LootFunction setFireworkExplosion(String shape, List<Integer> colors, List<Integer> fadeColors, Boolean trail, Boolean twinkle) {
		return new SetFireworkExplosionFunction(shape, colors, fadeColors, trail, twinkle);
	}

	/**
	 * sets a written book's cover ({@code minecraft:set_book_cover}); all fields optional
	 */
	public static LootFunction setBookCover() {
		return new SetBookCoverFunction();
	}

	public LootFunction title(String title) {
		return parameter("title", title);
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
		return new SetWrittenBookPagesFunction(mode, pages);
	}

	/**
	 * sets a writable (unsigned) book's pages ({@code minecraft:set_writable_book_pages})
	 *
	 * @param mode how {@code pages} merges into any existing pages; vanilla has no default, so it
	 * must always be supplied
	 */
	public static LootFunction setWritableBookPages(ListOperation mode, String... pages) {
		return new SetWritableBookPagesFunction(mode, pages);
	}

	/**
	 * shows or hides individual tooltip lines, e.g. hiding the enchantment tooltip
	 * ({@code minecraft:toggle_tooltips})
	 *
	 * @param toggles data component id -&gt; whether its tooltip is shown
	 */
	public static LootFunction toggleTooltips(TooltipToggle... toggles) {
		return new ToggleTooltipsFunction(toggles);
	}

	/**
	 * sets an ominous bottle's amplifier, clamped to 0-4 ({@code minecraft:set_ominous_bottle_amplifier})
	 */
	public static LootFunction setOminousBottleAmplifier(NumberProvider amplifier) {
		return new SetOminousBottleAmplifierFunction(amplifier);
	}

	/**
	 * sets custom model data ({@code minecraft:set_custom_model_data}); all fields optional,
	 * chain {@link #floats}/{@link #flags}/{@link #strings}/{@link #colors}
	 */
	public static LootFunction setCustomModelData() {
		return new SetCustomModelDataFunction();
	}

	public LootFunction floats(ListOperation mode, NumberProvider... values) {
		return put("floats", StandaloneListOperation.value(NumberProvider.CODEC, mode, List.of(values)));
	}

	public LootFunction flags(ListOperation mode, boolean... values) {
		List<Boolean> list = new ArrayList<>(values.length);
		for (boolean value : values) {
			list.add(value);
		}
		return put("flags", StandaloneListOperation.value(Codec.BOOL, mode, list));
	}

	public LootFunction strings(ListOperation mode, String... values) {
		return put("strings", StandaloneListOperation.value(Codec.STRING, mode, List.of(values)));
	}

	/**
	 * @param values a plain RGB int, wrapped as a constant {@link NumberProvider}
	 */
	public LootFunction colors(ListOperation mode, NumberProvider... values) {
		return put("colors", StandaloneListOperation.value(NumberProvider.CODEC, mode, List.of(values)));
	}

	/**
	 * empties the item stack, removing it from the loot ({@code minecraft:discard})
	 */
	public static LootFunction discard() {
		return new DiscardFunction();
	}

	// ---------- builder ----------

	public LootFunction function(String function) {
		return parameter("function", function);
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
		mode.addFieldsTo(this.values);
		return this;
	}

	/**
	 * only applies this function when the condition passes.
	 */
	public LootFunction condition(Condition condition) {
		subList("conditions").add(LootValue.encode(Condition.CODEC, condition));
		return this;
	}

	public LootFunction condition(String condition) {
		return condition(new Condition(condition));
	}

	/** references a predicate file at {@code data/<namespace>/predicate/<path>.json} by id, instead of embedding a condition inline */
	public LootFunction condition(Identifier reference) {
		return condition(Condition.reference(reference));
	}

	/**
	 * adds an enchantment level, for {@link #setEnchantments()}
	 */
	public LootFunction enchantment(String enchantment, NumberProvider level) {
		subMap("enchantments").put(enchantment, level.value());
		return this;
	}
}
