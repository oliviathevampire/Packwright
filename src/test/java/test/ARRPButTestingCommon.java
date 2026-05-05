package test;

import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.json.advancement.*;
import net.vampirestudios.arrp.json.blockstate.JBlockModel;
import net.vampirestudios.arrp.json.blockstate.JState;
import net.vampirestudios.arrp.json.blockstate.JVariant;
import net.vampirestudios.arrp.json.lang.JLang;
import net.vampirestudios.arrp.json.models.JElement;
import net.vampirestudios.arrp.json.models.JFace;
import net.vampirestudios.arrp.json.models.JFaces;
import net.vampirestudios.arrp.json.models.JModel;
import net.vampirestudios.arrp.json.recipe.*;
import net.vampirestudios.arrp.json.tags.JTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ARRPButTestingCommon {
	public static final String MOD_ID = "arrp_but_testing";

	public static void main(String[] args) {
		RuntimeResourcePack pack = RuntimeResourcePack.create(Identifier.fromNamespaceAndPath(MOD_ID, "before_user"));
		pack.addLang(Identifier.fromNamespaceAndPath(MOD_ID, "en_us"), new JLang()
				.block(Identifier.withDefaultNamespace("torch"), "Torch but it's different but it's not so it's the same")
				.item(Identifier.withDefaultNamespace("stick"), "It's still a stick")
		);
		pack.addBlockState(new JState().add(
				new JVariant()
						.put(Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false"),
								new JBlockModel(Identifier.withDefaultNamespace("block/spruce_door_bottom_left")))
		), Identifier.withDefaultNamespace("acacia_door"));
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "pumpkin"),
				JRecipe.shaped(
						JPattern.pattern("PPP", "P P", "PPP"),
						JKeys.keys().key("P", JIngredient.ingredient().item(Identifier.withDefaultNamespace("pumpkin_pie"))),
						JResult.stackedResult(Identifier.withDefaultNamespace("pumpkin"), 3)
				)
		);
		pack.addRecipe(
				Identifier.fromNamespaceAndPath(MOD_ID, "golden_sword"),
				JRecipe.shapeless(
						JIngredients.ingredients()
								.add(JIngredient.ingredient().item(Identifier.withDefaultNamespace("stick")))
								.add(JIngredient.ingredient().item(Identifier.withDefaultNamespace("gold_ingot")))
								.add(JIngredient.ingredient().item(Identifier.withDefaultNamespace("gold_ingot")))
								.add(JIngredient.ingredient().item(Identifier.withDefaultNamespace("gold_ingot"))),
						JResult.result(Identifier.withDefaultNamespace("golden_sword")).components(builder -> builder
								.set(DataComponents.DAMAGE, 3)
								.set(DataComponents.RARITY, Rarity.RARE)
						)
				)
		);
		Component burntBreadName = Component.literal("Burnt Bread").setStyle(Style.EMPTY.withBold(true));
		List<Component> burntBreadLore = List.of(Component.literal("A burnt piece of bread"), Component.literal("Does nothing"));
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "burnt_bread"),
				JRecipe.blasting(JIngredient.ingredient().item(Identifier.withDefaultNamespace("bread")),
						JResult.result(Identifier.withDefaultNamespace("coal"))
								.components(builder -> builder
										.set(DataComponents.ITEM_NAME, burntBreadName)
										.set(DataComponents.LORE, new ItemLore(burntBreadLore))
								)
				).cookingTime(30)
		);
		pack.addAdvancement(
				new JAdvancement()
						.display(new JDisplay()
								.icon(JIcon.of(Identifier.withDefaultNamespace("bread")))
								.title(Component.literal("Cooked Bread?"))
								.description(Component.literal("Burn a piece of bread. Congratulations?"))
								.frame(JDisplay.Frame.GOAL)
						)
						.criterion("burn_bread", JCriterion.inventoryChanged(
								AdvConditions.ItemPredicate.anyOf(Identifier.withDefaultNamespace("bread"))
										.componentEquals(
												AdvComponentPreds.id(DataComponents.ITEM_NAME),
												AdvComponentPreds.encodeValue(DataComponents.ITEM_NAME, burntBreadName)
										).componentEquals(
												AdvComponentPreds.id(DataComponents.LORE),
												AdvComponentPreds.encodeValue(DataComponents.LORE, ItemLore.EMPTY
														.withLineAdded(burntBreadLore.getFirst())
												)
										)
								)
						),
				Identifier.fromNamespaceAndPath(MOD_ID, "root")
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_furnace"),
				JCookingRecipe.smelting(JIngredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), JResult.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(100)
						.experience(50)
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_blast"),
				JCookingRecipe.blasting(JIngredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), JResult.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(80)
						.experience(500)
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_smoker"),
				JCookingRecipe.smoking(JIngredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), JResult.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(50)
						.experience(5000)
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_campfire"),
				JCookingRecipe.campfire(JIngredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), JResult.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(10)
		);
		pack.addRecipe(
				Identifier.fromNamespaceAndPath(MOD_ID, "bread_trims"),
				JRecipe.smithingTrim(
						JIngredient.ingredient().tag(Identifier.withDefaultNamespace("trimmable_armor")),
						JIngredient.ingredient().item(Identifier.withDefaultNamespace("bread")),
						JIngredient.ingredient().tag(Identifier.withDefaultNamespace("trim_templates"))
				)
		);

		// Custom Model Test
		pack.addModel(new JModel().element(
				new JElement()
						.bounds(11, 0, 8, 13, 2, 10)
						.faces(JFaces.allSame(new JFace("missing").uv(0, 0, 2, 2)))
		).element(
				new JElement()
						.bounds(10, 0, 7, 12, 2, 9)
						.faces(JFaces.allSame(new JFace("missing").uv(0, 0, 2, 2)))
		), Identifier.fromNamespaceAndPath(MOD_ID, "block/test_model"));
		// Replace cobblestone block state
		pack.addBlockState(
				new JState()
						.add(new JVariant().put("", new JBlockModel(Identifier.fromNamespaceAndPath(MOD_ID, "block/test_model")))),
				Identifier.withDefaultNamespace("cobblestone")
		);
		pack.addTag(Identifier.withDefaultNamespace("block/mineable/pickaxe"), new JTag().add(Identifier.withDefaultNamespace("oak_log")));

		pack.dumpDirect(Path.of("aaaaaaaa"));
	}
}