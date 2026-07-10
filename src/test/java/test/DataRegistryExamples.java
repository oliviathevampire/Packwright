package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.LootFunction;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Examples for the smaller data registries: reusable predicates and item modifiers,
 * chat types, enchantment providers and plain mcfunctions.
 * Everything is registered into one pack and dumped as its own datapack.
 */
public class DataRegistryExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	/* ----------------------------------------------------------
	 * 1) Predicates: reusable loot conditions, referenced from
	 *    loot tables/advancements with Condition.reference(...)
	 *    (the same mechanism vanilla uses for tool/can_silk_touch
	 *    since 26.3).
	 * ---------------------------------------------------------- */

	/** the tool is any pickaxe with Fortune of any level */
	public static Condition buildFortunePickaxePredicate() {
		return Condition.matchTool(ItemPredicate.of()
				.items("#minecraft:pickaxes")
				.predicate("minecraft:enchantments", List.of(
						Map.of("enchantments", "minecraft:fortune")
				)));
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

	public static JsonObject buildWhisperChatType() {
		JsonObject chat = new JsonObject();
		chat.addProperty("translation_key", "chat.type.mymod.whisper");
		JsonArray parameters = new JsonArray();
		parameters.add("sender");
		parameters.add("content");
		chat.add("parameters", parameters);
		JsonObject style = new JsonObject();
		style.addProperty("color", "gray");
		style.addProperty("italic", true);
		chat.add("style", style);

		JsonObject narration = new JsonObject();
		narration.addProperty("translation_key", "chat.type.text.narrate");
		narration.add("parameters", parameters.deepCopy());

		JsonObject chatType = new JsonObject();
		chatType.add("chat", chat);
		chatType.add("narration", narration);
		return chatType;
	}

	/* ----------------------------------------------------------
	 * 4) Enchantment provider: rolls enchantments for naturally
	 *    spawned equipment and similar contexts.
	 * ---------------------------------------------------------- */

	public static JsonObject buildEmberBladeEnchantments() {
		JsonObject provider = new JsonObject();
		provider.addProperty("type", "minecraft:single");
		provider.addProperty("enchantment", "minecraft:fire_aspect");
		provider.addProperty("level", 2);
		return provider;
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
	 * Register + dump
	 * ---------------------------------------------------------- */

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addPredicate(myModId("fortune_pickaxe"), buildFortunePickaxePredicate());
		pack.addPredicate(myModId("thundering"), buildThunderingPredicate());
		pack.addItemModifier(myModId("auto_smelt"), buildAutoSmeltModifier());
		pack.addItemModifier(myModId("trophy"), buildTrophyModifier());
		pack.addChatType(myModId("whisper"), buildWhisperChatType());
		pack.addEnchantmentProvider(myModId("ember_blade"), buildEmberBladeEnchantments());
		pack.addMcFunction(myModId("greet"), buildGreetFunction());
	}

	public static void main() {
		System.out.println("Predicate JSON (mymod:fortune_pickaxe):");
		System.out.println(JsonBytes.encodeToPrettyString(Condition.CODEC, buildFortunePickaxePredicate()));
		System.out.println("Predicate JSON (mymod:thundering):");
		System.out.println(JsonBytes.encodeToPrettyString(Condition.CODEC, buildThunderingPredicate()));
		System.out.println("Item Modifier JSON (mymod:auto_smelt):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildAutoSmeltModifier()));
		System.out.println("Item Modifier JSON (mymod:trophy):");
		System.out.println(JsonBytes.encodeToPrettyString(LootFunction.CODEC, buildTrophyModifier()));
		System.out.println("Chat Type JSON (mymod:whisper):");
		System.out.println(buildWhisperChatType());
		System.out.println("Enchantment Provider JSON (mymod:ember_blade):");
		System.out.println(buildEmberBladeEnchantments());
		System.out.println("Function (mymod:greet):");
		buildGreetFunction().forEach(System.out::println);

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:data_registry_examples");
		pack.addDataPackMcmeta("Predicates, item modifiers, chat types, slot sources and functions, generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/data_registry_examples"));
	}
}
