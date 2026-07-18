package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.recipe.Ingredient;
import net.vampirestudios.packwright.data.recipe.Recipe;
import net.vampirestudios.packwright.data.recipe.Result;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Examples for the {@code crafting_special_*} family of recipe types, plus {@code crafting_dye},
 * {@code crafting_imbue} and {@code crafting_decorated_pot} (26.3). These all resolve their
 * result dynamically at craft time (e.g. "whatever banner you fed in, undyed"), so most don't
 * take a {@link Result} the way {@code crafting_shaped}/{@code crafting_shapeless} do — check
 * each factory's parameters before assuming a shape.
 */
public class SpecialRecipeExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	private static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	private static Ingredient item(String id) {
		return Ingredient.ingredient().item(vanillaId(id));
	}

	private static Ingredient tag(String path) {
		return Ingredient.ingredient().tag(vanillaId(path));
	}

	/** {@code crafting_dye}: recolors {@code target} using any dye (mirrors vanilla's own leather-armor dyeing) */
	public static Recipe buildDyeRecipe() {
		return Recipe.dye(item("leather_boots"), tag("dyes"), Result.result(vanillaId("leather_boots")));
	}

	/** {@code crafting_imbue}: infuses {@code source} with {@code material} (mirrors vanilla's own item-frame glow-ink recipe) */
	public static Recipe buildImbueRecipe() {
		return Recipe.imbue(item("item_frame"), item("glow_ink_sac"), Result.result(vanillaId("glow_item_frame")));
	}

	/** {@code crafting_decorated_pot}: four sherds (or plain bricks) around the edges */
	public static Recipe buildDecoratedPotRecipe() {
		return Recipe.decoratedPot(item("brick"), Result.result(vanillaId("decorated_pot")));
	}

	/** {@code crafting_decorated_pot}, explicit per-side sherds */
	public static Recipe buildFourSherdPotRecipe() {
		return Recipe.decoratedPot(
				item("angler_pottery_sherd"),
				item("archer_pottery_sherd"),
				item("arms_up_pottery_sherd"),
				item("blade_pottery_sherd"),
				Result.result(vanillaId("decorated_pot"))
		);
	}

	/** {@code crafting_special_bookcloning}: copies a written book onto blank books */
	public static Recipe buildBookCloningRecipe() {
		return Recipe.bookCloning(item("written_book"), item("writable_book"), Result.result(vanillaId("written_book")));
	}

	/** {@code crafting_special_mapextending}: zooms a filled map out one level */
	public static Recipe buildMapExtendingRecipe() {
		return Recipe.mapExtending(item("filled_map"), item("paper"), Result.result(vanillaId("filled_map")));
	}

	/** {@code crafting_special_firework_rocket}: shell + gunpowder + optional star */
	public static Recipe buildFireworkRocketRecipe() {
		return Recipe.fireworkRocket(item("paper"), item("gunpowder"), item("firework_star"), Result.result(vanillaId("firework_rocket")));
	}

	/** {@code crafting_special_firework_star}: gunpowder + shape item + optional trail/twinkle/dyes */
	public static Recipe buildFireworkStarRecipe() {
		return Recipe.fireworkStar(item("glowstone_dust"), item("diamond"), item("gunpowder"), tag("dyes"), Result.result(vanillaId("firework_star")));
	}

	/** {@code crafting_special_firework_star_fade}: adds a fade color to an existing star */
	public static Recipe buildFireworkStarFadeRecipe() {
		return Recipe.fireworkStarFade(item("firework_star"), tag("dyes"), Result.result(vanillaId("firework_star")));
	}

	/** {@code crafting_special_bannerduplicate}: copies a patterned banner onto a blank one */
	public static Recipe buildBannerDuplicateRecipe() {
		return Recipe.bannerDuplicate(item("white_banner"), Result.result(vanillaId("white_banner")));
	}

	/** {@code crafting_special_shielddecoration}: stamps a banner's pattern onto a shield */
	public static Recipe buildShieldDecorationRecipe() {
		return Recipe.shieldDecoration(item("white_banner"), item("shield"), Result.result(vanillaId("shield")));
	}

	/** {@code crafting_special_repairitem}: no fields at all, just registers the vanilla item-repair behavior */
	public static Recipe buildRepairItemRecipe() {
		return Recipe.repairItem();
	}

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addRecipe(myModId("recolored_dye"), buildDyeRecipe());
		pack.addRecipe(myModId("glowing_book_imbue"), buildImbueRecipe());
		pack.addRecipe(myModId("plain_decorated_pot"), buildDecoratedPotRecipe());
		pack.addRecipe(myModId("four_sherd_pot"), buildFourSherdPotRecipe());
		pack.addRecipe(myModId("book_cloning"), buildBookCloningRecipe());
		pack.addRecipe(myModId("map_extending"), buildMapExtendingRecipe());
		pack.addRecipe(myModId("firework_rocket"), buildFireworkRocketRecipe());
		pack.addRecipe(myModId("firework_star"), buildFireworkStarRecipe());
		pack.addRecipe(myModId("firework_star_fade"), buildFireworkStarFadeRecipe());
		pack.addRecipe(myModId("banner_duplicate"), buildBannerDuplicateRecipe());
		pack.addRecipe(myModId("shield_decoration"), buildShieldDecorationRecipe());
		pack.addRecipe(myModId("repair_item"), buildRepairItemRecipe());
	}

	public static void main() {
		System.out.println("Recipe JSON (mymod:recolored_dye):");
		System.out.println(JsonBytes.encodeToPrettyString(Recipe.CODEC, buildDyeRecipe()));
		System.out.println("Recipe JSON (mymod:firework_star):");
		System.out.println(JsonBytes.encodeToPrettyString(Recipe.CODEC, buildFireworkStarRecipe()));
		System.out.println("Recipe JSON (mymod:repair_item):");
		System.out.println(JsonBytes.encodeToPrettyString(Recipe.CODEC, buildRepairItemRecipe()));

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:special_recipe_examples");
		pack.addDataPackMcmeta("crafting_special_* and related recipe examples generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/special_recipe_examples"));
	}
}
