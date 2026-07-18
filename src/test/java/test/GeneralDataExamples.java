package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.entity.BiomeSpawnCondition;
import net.vampirestudios.packwright.data.entity.CatVariant;
import net.vampirestudios.packwright.data.entity.FrogVariant;
import net.vampirestudios.packwright.data.entity.SpawnPrioritySelectors;
import net.vampirestudios.packwright.data.entity.StructuresSpawnCondition;
import net.vampirestudios.packwright.data.recipe.BrewingRecipe;
import net.vampirestudios.packwright.data.recipe.CookingBookCategory;
import net.vampirestudios.packwright.data.recipe.CraftingBookCategory;
import net.vampirestudios.packwright.data.recipe.Ingredient;
import net.vampirestudios.packwright.data.recipe.Ingredients;
import net.vampirestudios.packwright.data.recipe.IntRange;
import net.vampirestudios.packwright.data.recipe.PotionContentsPredicate;
import net.vampirestudios.packwright.data.recipe.Recipe;
import net.vampirestudios.packwright.data.recipe.Result;
import net.vampirestudios.packwright.data.registry.DamageType;
import net.vampirestudios.packwright.data.registry.Instrument;
import net.vampirestudios.packwright.data.registry.JukeboxSong;
import net.vampirestudios.packwright.data.registry.WorldClock;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Broader examples that don't belong to one narrow feature area: recipe variants,
 * entity variant spawn selectors, and small data registries.
 */
public class GeneralDataExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	private static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	public static Recipe buildChargedCrystalTransmuteRecipe() {
		return Recipe.transmute(
						Ingredient.of(vanillaId("amethyst_shard")),
						Ingredient.of(vanillaId("echo_shard")),
						Result.stackedResult(vanillaId("amethyst_cluster"), 2)
								.components(map -> map.set("minecraft:enchantment_glint_override", true))
				)
				.materialCount(IntRange.between(1, 3))
				.addMaterialCountToResult(true)
				.category(CraftingBookCategory.MISC)
				.showNotification(false);
	}

	public static Recipe buildTintedBrewingRecipe() {
		return Recipe.brewing(
				BrewingRecipe.PotionIngredient.of(vanillaId("potion")).potion(vanillaId("water")),
				BrewingRecipe.PotionIngredient.of(vanillaId("glow_ink_sac")),
				Result.result(vanillaId("potion"))
						.components(map -> map.set("minecraft:potion_contents",
								PotionContentsPredicate.CODEC,
								PotionContentsPredicate.potion(vanillaId("water"))))
		);
	}

	public static Recipe buildComponentIngredientRecipe() {
		return Recipe.shapeless(
						Ingredients.ingredients(
								Ingredient.fabricComponents(vanillaId("paper"), map ->
										map.set("minecraft:custom_name", "{\"text\":\"Blank Charter\"}")),
								Ingredient.of(vanillaId("emerald"))
						),
						Result.result(vanillaId("written_book"))
				)
				.category(CraftingBookCategory.MISC)
				.showNotification(true);
	}

	/** whimsical, but every referenced item is real: smoking sweet berries into glow berries */
	public static Recipe buildFoodCookingRecipe() {
		return Recipe.smoking(
						Ingredient.of(vanillaId("sweet_berries")),
						Result.result(vanillaId("glow_berries"))
				)
				.category(CookingBookCategory.FOOD)
				.cookingTime(120)
				.experience(0.35f);
	}

	public static CatVariant buildMistyCatVariant() {
		return CatVariant.catVariant()
				.assetId(myModId("entity/cat/misty"))
				.babyAssetId(myModId("entity/cat/misty_baby"))
				.spawnConditions(SpawnPrioritySelectors.selectors()
						.add(BiomeSpawnCondition.biomeCondition().biomeTag(vanillaId("is_forest")), 4)
						.add(StructuresSpawnCondition.structuresCondition().structure(vanillaId("village_plains")), 8)
						.fallbackSelector(1)
						.freeze());
	}

	public static FrogVariant buildMossyFrogVariant() {
		return FrogVariant.frogVariant()
				.assetId(myModId("entity/frog/mossy"))
				.spawnConditions(SpawnPrioritySelectors.selectors()
						.add(BiomeSpawnCondition.biomeCondition().biomes(
								vanillaId("swamp"),
								vanillaId("mangrove_swamp")
						), 12)
						.fallbackSelector(2)
						.freeze());
	}

	public static DamageType buildBrambleDamageType() {
		return DamageType.damageType()
				.messageId("mymod.bramble")
				.scaling(DamageType.Scaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER)
				.exhaustion(0.1f)
				.effects(DamageType.Effects.POKING)
				.deathMessageType(DamageType.DeathMessageType.DEFAULT);
	}

	public static Instrument buildQuietHornInstrument() {
		return Instrument.instrument()
				.soundEvent(vanillaId("item.goat_horn.sound.0"))
				.useDuration(0.0f)
				.durabilityDamage(1)
				.range(48.0f)
				.description("instrument.mymod.quiet_call");
	}

	public static JukeboxSong buildCampfireJukeboxSong() {
		return JukeboxSong.jukeboxSong()
				.soundEvent(vanillaId("music_disc.cat"))
				.description("jukebox_song.mymod.campfire_loop")
				.lengthInSeconds(96.0f)
				.comparatorOutput(6);
	}

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addRecipe(myModId("charged_crystal"), buildChargedCrystalTransmuteRecipe());
		pack.addRecipe(myModId("tinted_water_bottle"), buildTintedBrewingRecipe());
		pack.addRecipe(myModId("signed_charter"), buildComponentIngredientRecipe());
		pack.addRecipe(myModId("smoked_berries"), buildFoodCookingRecipe());
		pack.addCatVariant(myModId("misty"), buildMistyCatVariant());
		pack.addFrogVariant(myModId("mossy"), buildMossyFrogVariant());
		pack.addDamageType(myModId("bramble"), buildBrambleDamageType());
		pack.addInstrument(myModId("quiet_call"), buildQuietHornInstrument());
		pack.addJukeboxSong(myModId("campfire_loop"), buildCampfireJukeboxSong());
		pack.addWorldClock(myModId("local_overworld"), WorldClock.worldClock());
	}

	public static void main() {
		System.out.println("Recipe JSON (mymod:charged_crystal):");
		System.out.println(JsonBytes.encodeToPrettyString(Recipe.CODEC, buildChargedCrystalTransmuteRecipe()));
		System.out.println("Recipe JSON (mymod:tinted_water_bottle):");
		System.out.println(JsonBytes.encodeToPrettyString(Recipe.CODEC, buildTintedBrewingRecipe()));
		System.out.println("Cat Variant JSON (mymod:misty):");
		System.out.println(JsonBytes.encodeToPrettyString(CatVariant.CODEC, buildMistyCatVariant()));
		System.out.println("Damage Type JSON (mymod:bramble):");
		System.out.println(JsonBytes.encodeToPrettyString(DamageType.CODEC, buildBrambleDamageType()));

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:general_data_examples");
		pack.addDataPackMcmeta("General data examples generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/general_data_examples"));
	}
}
