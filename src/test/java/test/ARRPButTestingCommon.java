package test;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ItemLore;
import net.vampirestudios.arrp.api.RuntimeResourcePack;
import net.vampirestudios.arrp.data.advancement.*;
import net.vampirestudios.arrp.assets.blockstates.BlockModel;
import net.vampirestudios.arrp.assets.blockstates.BlockState;
import net.vampirestudios.arrp.assets.blockstates.Variant;
import net.vampirestudios.arrp.assets.lang.Lang;
import net.vampirestudios.arrp.assets.models.Element;
import net.vampirestudios.arrp.assets.models.Face;
import net.vampirestudios.arrp.assets.models.Faces;
import net.vampirestudios.arrp.assets.models.Model;
import net.vampirestudios.arrp.data.recipe.*;
import net.vampirestudios.arrp.data.tags.Tag;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ARRPButTestingCommon {
	public static final String MOD_ID = "arrp_but_testing";

	public static void main(String[] args) {
		RuntimeResourcePack pack = RuntimeResourcePack.create(Identifier.fromNamespaceAndPath(MOD_ID, "before_user"));
		pack.addLang(Identifier.fromNamespaceAndPath(MOD_ID, "en_us"), new Lang()
				.block(Identifier.withDefaultNamespace("torch"), "Torch but it's different but it's not so it's the same")
				.item(Identifier.withDefaultNamespace("stick"), "It's still a stick")
		);
		pack.addBlockState(new BlockState().add(
				new Variant()
						.put(Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false"),
								new BlockModel(Identifier.withDefaultNamespace("block/spruce_door_bottom_left")))
		), Identifier.withDefaultNamespace("acacia_door"));
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "pumpkin"),
				Recipe.shaped(
						Pattern.pattern("PPP", "P P", "PPP"),
						Keys.keys().item("P", Identifier.withDefaultNamespace("pumpkin_pie")),
						Result.stackedResult(Identifier.withDefaultNamespace("pumpkin"), 3)
				)
		);
		pack.addRecipe(
				Identifier.fromNamespaceAndPath(MOD_ID, "golden_sword"),
				Recipe.shapeless(
						Ingredients.ingredients()
								.addItem(Identifier.withDefaultNamespace("stick"))
								.addItem(Identifier.withDefaultNamespace("gold_ingot"))
								.addItem(Identifier.withDefaultNamespace("gold_ingot"))
								.addItem(Identifier.withDefaultNamespace("gold_ingot")),
						Result.result(Identifier.withDefaultNamespace("golden_sword"))
								.components(builder -> {
									builder.addProperty("minecraft:damage", 3);
									builder.addProperty("minecraft:rarity", "RARE");
								})
				)
		);
		Component burntBreadName = Component.literal("Burnt Bread").setStyle(Style.EMPTY.withBold(true));
		List<Component> burntBreadLore = List.of(Component.literal("A burnt piece of bread"), Component.literal("Does nothing"));
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "burnt_bread"),
				Recipe.blasting(Ingredient.ingredient().item(Identifier.withDefaultNamespace("bread")),
						Result.result(Identifier.withDefaultNamespace("coal"))
//								.components(builder -> builder
//										.set(DataComponents.ITEM_NAME, burntBreadName)
//										.set(DataComponents.LORE, new ItemLore(burntBreadLore))
//								)
				).cookingTime(30)
		);
		pack.addAdvancement(
				new Advancement()
						.display(new Display()
								.icon(Icon.of(Identifier.withDefaultNamespace("bread")))
								.title(Component.literal("Cooked Bread?"))
								.description(Component.literal("Burn a piece of bread. Congratulations?"))
								.frame(Display.Frame.GOAL)
						)
						.criterion("burn_bread", Criterion.inventoryChanged(
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
				CookingRecipe.smelting(Ingredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), Result.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(100)
						.experience(50)
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_blast"),
				CookingRecipe.blasting(Ingredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), Result.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(80)
						.experience(500)
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_smoker"),
				CookingRecipe.smoking(Ingredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), Result.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(50)
						.experience(5000)
		);
		pack.addRecipe(Identifier.fromNamespaceAndPath(MOD_ID, "bread_campfire"),
				CookingRecipe.campfire(Ingredient.ingredient().item(Identifier.withDefaultNamespace("wheat")), Result.result(Identifier.withDefaultNamespace("bread")))
						.cookingTime(10)
		);
		pack.addRecipe(
				Identifier.fromNamespaceAndPath(MOD_ID, "bread_trims"),
				Recipe.smithingTrim(
						Ingredient.ingredient().tag(Identifier.withDefaultNamespace("trimmable_armor")),
						Ingredient.ingredient().item(Identifier.withDefaultNamespace("bread")),
						Ingredient.ingredient().tag(Identifier.withDefaultNamespace("trim_templates"))
				)
		);

		// Custom Model Test
		pack.addModel(new Model().element(
				new Element()
						.bounds(11, 0, 8, 13, 2, 10)
						.faces(Faces.allSame(new Face("missing").uv(0, 0, 2, 2)))
		).element(
				new Element()
						.bounds(10, 0, 7, 12, 2, 9)
						.faces(Faces.allSame(new Face("missing").uv(0, 0, 2, 2)))
		), Identifier.fromNamespaceAndPath(MOD_ID, "block/test_model"));
		// Replace cobblestone block state
		pack.addBlockState(
				new BlockState()
						.add(new Variant().put("", new BlockModel(Identifier.fromNamespaceAndPath(MOD_ID, "block/test_model")))),
				Identifier.withDefaultNamespace("cobblestone")
		);
		pack.addTag(Identifier.withDefaultNamespace("block/mineable/pickaxe"), new Tag().add(Identifier.withDefaultNamespace("oak_log")));

		pack.dumpDirect(Path.of("aaaaaaaa"));
	}
}