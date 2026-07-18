package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.loot.Entry;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.loot.functions.ListOperation;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.loot.functions.TooltipToggle;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.List;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Examples for the loot item functions added around the 26.3 data refresh (item modifiers,
 * usable from loot tables or {@code /item modify}).
 */
public class LootFunctionExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	private static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	/** a fixed attack-damage bonus, always active while in the main hand */
	public static LootFunction buildAttributeBoostModifier() {
		return LootFunction.setAttributes(
				LootFunction.attributeModifier(
						myModId("ember_blade_bonus_damage"),
						vanillaId("attack_damage"),
						"add_value",
						NumberProvider.constant(2.0),
						"mainhand"
				)
		);
	}

	/** turns a map into a treasure map pointing at the nearest ruined portal, zoomed in tighter than the vanilla default */
	public static LootFunction buildRuinedPortalMapModifier() {
		return LootFunction.makeExplorationMap()
				.destination("#minecraft:on_treasure_maps")
				.mapDecoration(vanillaId("target_x"))
				.zoom(3)
				.searchRadius(80)
				.skipExistingChunks(true);
	}

	/** eating suspicious stew gives either a short glow or a longer haste effect */
	public static LootFunction buildStewSurpriseModifier() {
		return LootFunction.stewEffect()
				.effect(vanillaId("glowing"), NumberProvider.constant(6))
				.effect(vanillaId("haste"), NumberProvider.uniform(10, 20));
	}

	/** stamps two banner pattern layers onto the item */
	public static LootFunction buildEmberBannerPatternModifier() {
		return LootFunction.setBannerPattern(false)
				.pattern(vanillaId("flower"), "orange")
				.pattern(vanillaId("border"), "black");
	}

	/** re-dyes leather armor with a random pair of colors */
	public static LootFunction buildRandomDyeModifier() {
		return LootFunction.setRandomDyes(NumberProvider.constant(2));
	}

	/** picks a random potion from a curated list instead of every possible one */
	public static LootFunction buildCuratedPotionModifier() {
		return LootFunction.setRandomPotion(
				vanillaId("fire_resistance").toString(),
				vanillaId("strength").toString(),
				vanillaId("swiftness").toString()
		);
	}

	/** goat horn: picks one of two real vanilla instruments */
	public static LootFunction buildInstrumentModifier() {
		return LootFunction.setInstrument(vanillaId("ponder_goat_horn").toString(), vanillaId("sing_goat_horn").toString());
	}

	/** builds a firework rocket with two chained explosions */
	public static LootFunction buildFireworkModifier() {
		return LootFunction.setFireworks()
				.flightDuration(2)
				.explosions(ListOperation.replaceAll(),
						LootFunction.fireworkExplosion("large_ball", List.of(0xFF7A1C), null, true, false),
						LootFunction.fireworkExplosion("star", List.of(0xFFD37A), List.of(0xFFFFFF), false, true)
				);
	}

	/** covers a written book with a title, author, and a slightly worn look */
	public static LootFunction buildBookCoverModifier() {
		return LootFunction.setBookCover()
				.title("Notes from the Ember Wastes")
				.author("A Cautious Traveler")
				.generation(1);
	}

	/** replaces a written book's pages outright */
	public static LootFunction buildWrittenPagesModifier() {
		return LootFunction.setWrittenBookPages(ListOperation.replaceAll(),
				"{\"text\":\"Page one: the vents are louder at dusk.\"}",
				"{\"text\":\"Page two: the outpost trades ash for emeralds.\"}"
		);
	}

	/** hides the enchantment glint tooltip line without hiding the glint itself */
	public static LootFunction buildHiddenTooltipModifier() {
		return LootFunction.toggleTooltips(TooltipToggle.hide("minecraft:enchantments"));
	}

	/** rolls a random ominous bottle amplifier between 1 and 3 */
	public static LootFunction buildOminousBottleModifier() {
		return LootFunction.setOminousBottleAmplifier(NumberProvider.uniform(1, 3));
	}

	/** appends one extra custom-model-data float instead of replacing the whole list */
	public static LootFunction buildCustomModelDataModifier() {
		return LootFunction.setCustomModelData()
				.floats(ListOperation.append(), NumberProvider.constant(1.0));
	}

	/** copies the killing player's custom name onto the drop */
	public static LootFunction buildCopyKillerNameModifier() {
		return LootFunction.copyCustomData(EntityTarget.ATTACKING_PLAYER)
				.copy("CustomName", "display.Name");
	}

	/** turns a mob head into a specific player's head */
	public static LootFunction buildPlayerHeadModifier() {
		return LootFunction.fillPlayerHead(EntityTarget.THIS);
	}

	/** runs a smelt, then a rename, in one modifier */
	public static LootFunction buildSequenceModifier() {
		return LootFunction.sequence(
				LootFunction.furnaceSmelt(),
				LootFunction.setName("Smelted Find")
		);
	}

	/** replaces a shulker box's contents with a fixed loadout */
	public static LootFunction buildFixedShulkerContentsModifier() {
		return LootFunction.setContents("minecraft:container",
				Entry.item(vanillaId("bread")),
				Entry.item(vanillaId("torch"))
		);
	}

	/** rerolls a bundle's contents by running another modifier on them */
	public static LootFunction buildRerolledBundleModifier() {
		return LootFunction.modifyContents("minecraft:bundle_contents", LootFunction.enchantRandomly());
	}

	/** only applies the wrapped modifier to pickaxes, leaving everything else untouched */
	public static LootFunction buildFilteredModifier() {
		return LootFunction.filtered(ItemPredicate.of().items("#minecraft:pickaxes"))
				.onPass(LootFunction.setName("Miner's Pick"))
				.onFail(LootFunction.setName("Ordinary Tool"));
	}

	/** points a mob-head block entity at a real vanilla loot table instead of the default empty one */
	public static LootFunction buildSkullLootTableModifier() {
		return LootFunction.setLootTable(vanillaId("chests/simple_dungeon"), vanillaId("skull"));
	}

	/** deletes the item outright, e.g. for a "no drops" branch of a conditional loot table */
	public static LootFunction buildDiscardModifier() {
		return LootFunction.discard();
	}

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addItemModifier(myModId("attribute_boost"), buildAttributeBoostModifier());
		pack.addItemModifier(myModId("ruined_portal_map"), buildRuinedPortalMapModifier());
		pack.addItemModifier(myModId("stew_surprise"), buildStewSurpriseModifier());
		pack.addItemModifier(myModId("ember_banner_pattern"), buildEmberBannerPatternModifier());
		pack.addItemModifier(myModId("random_dye"), buildRandomDyeModifier());
		pack.addItemModifier(myModId("curated_potion"), buildCuratedPotionModifier());
		pack.addItemModifier(myModId("instrument"), buildInstrumentModifier());
		pack.addItemModifier(myModId("firework"), buildFireworkModifier());
		pack.addItemModifier(myModId("book_cover"), buildBookCoverModifier());
		pack.addItemModifier(myModId("written_pages"), buildWrittenPagesModifier());
		pack.addItemModifier(myModId("hidden_tooltip"), buildHiddenTooltipModifier());
		pack.addItemModifier(myModId("ominous_bottle"), buildOminousBottleModifier());
		pack.addItemModifier(myModId("custom_model_data"), buildCustomModelDataModifier());
		pack.addItemModifier(myModId("copy_killer_name"), buildCopyKillerNameModifier());
		pack.addItemModifier(myModId("player_head"), buildPlayerHeadModifier());
		pack.addItemModifier(myModId("sequence"), buildSequenceModifier());
		pack.addItemModifier(myModId("fixed_shulker_contents"), buildFixedShulkerContentsModifier());
		pack.addItemModifier(myModId("rerolled_bundle"), buildRerolledBundleModifier());
		pack.addItemModifier(myModId("filtered"), buildFilteredModifier());
		pack.addItemModifier(myModId("skull_loot_table"), buildSkullLootTableModifier());
		pack.addItemModifier(myModId("discard"), buildDiscardModifier());
	}

	public static void main() {
		System.out.println("Item Modifier JSON (mymod:attribute_boost):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildAttributeBoostModifier()));
		System.out.println("Item Modifier JSON (mymod:firework):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildFireworkModifier()));
		System.out.println("Item Modifier JSON (mymod:filtered):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildFilteredModifier()));

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:loot_function_examples");
		pack.addDataPackMcmeta("Loot item function examples generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/loot_function_examples"));
	}
}
