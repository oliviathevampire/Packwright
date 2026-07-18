package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.registry.ChatType;
import net.vampirestudios.packwright.data.registry.EnchantmentProvider;
import net.vampirestudios.packwright.data.registry.SlotSource;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.List;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Examples for the smaller data registries: reusable predicates and item modifiers,
 * chat types, enchantment providers, slot sources (26.3) and plain mcfunctions.
 * Everything is registered into one pack and dumped as its own datapack.
 */
public class DataRegistryExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	/* ----------------------------------------------------------
	 * 1) Predicates: reusable loot conditions, referenced from
	 *    loot tables/advancements by bare predicate-file id, e.g.
	 *    Entry#condition(Identifier) / LootFunction#condition(Identifier)
	 *    (the same mechanism vanilla uses for tool/can_silk_touch
	 *    since 26.3).
	 * ---------------------------------------------------------- */

	/** the tool is a diamond pickaxe */
	public static Condition buildDiamondPickaxePredicate() {
		return Condition.matchTool(ItemPredicate.of()
				.items(Identifier.withDefaultNamespace("diamond_pickaxe")));
	}

	/** it is currently thundering — nice for storm-only drops */
	public static Condition buildThunderingPredicate() {
		return Condition.weatherCheck(null, true);
	}

	/* ----------------------------------------------------------
	 * 2) Item modifiers: reusable loot functions, usable from
	 *    "/item modify" and loot tables.
	 * ---------------------------------------------------------- */

	/** smelts the item as if mined with auto-smelt */
	public static LootFunction buildAutoSmeltModifier() {
		return LootFunction.furnaceSmelt();
	}

	/** brands the item with a custom name and lore */
	public static LootFunction buildTrophyModifier() {
		return LootFunction.setName("Ember Trophy")
				.condition(Condition.killedByPlayer());
	}

	/* ----------------------------------------------------------
	 * 3) Chat type: how messages of this type render in chat
	 *    and how they are narrated.
	 * ---------------------------------------------------------- */

	public static ChatType buildWhisperChatType() {
		return ChatType.chatType(
				ChatType.Decoration.of("chat.type.mymod.whisper", "sender", "content")
						.style(ChatType.Style.style().color("gray").italic(true)),
				ChatType.Decoration.of("chat.type.text.narrate", "sender", "content")
		);
	}

	/* ----------------------------------------------------------
	 * 4) Enchantment provider: rolls enchantments for naturally
	 *    spawned equipment and similar contexts.
	 * ---------------------------------------------------------- */

	public static EnchantmentProvider buildEmberBladeEnchantments() {
		return EnchantmentProvider.single(Identifier.withDefaultNamespace("fire_aspect"), 2);
	}

	/* ----------------------------------------------------------
	 * 5) Slot source (26.3): reusable slot selections for /item
	 *    and "/execute if slots|items", referenced inline with
	 *    minecraft:reference.
	 * ---------------------------------------------------------- */

	/** the whole hotbar of the targeted entity */
	public static SlotSource buildHotbarSlotSource() {
		return SlotSource.slotRange("hotbar.*");
	}

	/* ----------------------------------------------------------
	 * 6) Functions: plain .mcfunction files.
	 * ---------------------------------------------------------- */

	public static List<String> buildGreetFunction() {
		return List.of(
				"# runs when a player uses /function mymod:greet",
				"tellraw @s {\"text\":\"Welcome to the Ember Wastes!\",\"color\":\"gold\"}",
				"particle minecraft:flame ~ ~1 ~ 0.4 0.4 0.4 0.01 30",
				"playsound minecraft:block.fire.ambient master @s"
		);
	}

	/* ----------------------------------------------------------
	 * 7) Number provider registry entry reused by the examples
	 * ---------------------------------------------------------- */

	public static NumberProvider buildStormBonusProvider() {
		return NumberProvider.conditionalValue(
				buildThunderingPredicate(),
				NumberProvider.uniform(2, 3),
				NumberProvider.constant(1)
		);
	}

	/* ----------------------------------------------------------
	 * Register + dump
	 * ---------------------------------------------------------- */

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addPredicate(myModId("diamond_pickaxe"), buildDiamondPickaxePredicate());
		pack.addPredicate(myModId("thundering"), buildThunderingPredicate());
		pack.addItemModifier(myModId("auto_smelt"), buildAutoSmeltModifier());
		pack.addItemModifier(myModId("trophy"), buildTrophyModifier());
		pack.addChatType(myModId("whisper"), buildWhisperChatType());
		pack.addEnchantmentProvider(myModId("ember_blade"), buildEmberBladeEnchantments());
		pack.addSlotSource(myModId("hotbar"), buildHotbarSlotSource());
		pack.addMcFunction(myModId("greet"), buildGreetFunction());
		pack.addNumberProvider(myModId("storm_bonus"), buildStormBonusProvider());
	}

	public static void main() {
		System.out.println("Predicate JSON (mymod:diamond_pickaxe):");
		System.out.println(JsonBytes.encodeToPrettyString(Condition.CODEC, buildDiamondPickaxePredicate()));
		System.out.println("Predicate JSON (mymod:thundering):");
		System.out.println(JsonBytes.encodeToPrettyString(Condition.CODEC, buildThunderingPredicate()));
		System.out.println("Item Modifier JSON (mymod:auto_smelt):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildAutoSmeltModifier()));
		System.out.println("Item Modifier JSON (mymod:trophy):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildTrophyModifier()));
		System.out.println("Chat Type JSON (mymod:whisper):");
		System.out.println(JsonBytes.encodeToPrettyString(ChatType.CODEC, buildWhisperChatType()));
		System.out.println("Enchantment Provider JSON (mymod:ember_blade):");
		System.out.println(JsonBytes.encodeToPrettyString(EnchantmentProvider.CODEC, buildEmberBladeEnchantments()));
		System.out.println("Slot Source JSON (mymod:hotbar):");
		System.out.println(JsonBytes.encodeToPrettyString(SlotSource.CODEC, buildHotbarSlotSource()));
		System.out.println("Function (mymod:greet):");
		buildGreetFunction().forEach(System.out::println);

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:data_registry_examples");
		pack.addDataPackMcmeta("Predicates, item modifiers, chat types, slot sources and functions, generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/data_registry_examples"));
	}
}
