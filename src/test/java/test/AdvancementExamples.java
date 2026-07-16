package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.advancement.*;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.predicate.*;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Examples for advancement criteria added around the 26.3 data refresh. The examples
 * intentionally mix the convenience {@link Criterion} factories with direct condition
 * classes, so both common and less common trigger shapes are covered.
 */
public class AdvancementExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	public static Advancement buildRootAdvancement() {
		return new Advancement()
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("map")))
						.title("Field Notes")
						.description("Start recording little moments from the world.")
						.background("minecraft:textures/gui/advancements/backgrounds/adventure.png")
						.frame(Display.Frame.TASK)
				)
				.criterion("started", Criterion.tick())
				.telemetry(false);
	}

	public static Advancement buildCampSetupAdvancement() {
		LocationPredicate campfireAtFeet = LocationPredicate.of()
				.block(BlockPredicate.of().blocks(Identifier.withDefaultNamespace("campfire")).state("lit", true));

		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("campfire")))
						.title("Make Camp")
						.description("Light a campfire with a fire charge, then sleep nearby.")
						.frame(Display.Frame.TASK)
				)
				.criterion("light_campfire", new Criterion(BlockUseConditions.itemUsedOnBlock(
						Condition.locationCheck(campfireAtFeet),
						Condition.matchTool(ItemPredicate.of().items(Identifier.withDefaultNamespace("fire_charge")))
				)))
				.criterion("sleep_near_camp", new Criterion(PlayerConditions.sleptInBed()
						.player(EntityPredicate.of().location(campfireAtFeet))))
				.requirements(List.of(List.of("light_campfire"), List.of("sleep_near_camp")));
	}

	public static Advancement buildOverworldErrandAdvancement() {
		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("ender_eye")))
						.title("There and Back")
						.description("Leave the Overworld, return with an eye, and follow it far enough.")
						.frame(Display.Frame.GOAL)
				)
				.criterion("leave_overworld", new Criterion(ChangeDimensionConditions.changedDimensionFrom(
						Identifier.withDefaultNamespace("overworld")
				)))
				.criterion("find_a_bearing", new Criterion(UsedEnderEyeConditions.usedEnderEye(
						DoubleBound.atLeast(128.0)
				)))
				.criterion("ender_eye_in_pack", Criterion.inventoryChanged(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("ender_eye"))
				))
				.requirements(List.of(List.of("leave_overworld"), List.of("find_a_bearing"), List.of("ender_eye_in_pack")))
				.rewards(Rewards.ofXp(50));
	}

	public static Advancement buildWorkshopAdvancement() {
		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("enchanted_book")))
						.title("Workshop Habits")
						.description("Brew, enchant, repair, and keep enough spare room to work.")
						.frame(Display.Frame.GOAL)
				)
				.criterion("brew_strength", new Criterion(BrewedPotionConditions.brewedPotion(
						Identifier.withDefaultNamespace("strength")
				)))
				.criterion("enchant_tool", new Criterion(EnchantedItemConditions.enchantedItem(
						ItemPredicate.of().items("#minecraft:pickaxes"),
						IntBound.atLeast(2)
				)))
				.criterion("wear_down_tool", new Criterion(ItemDurabilityConditions.changedDurability(
						ItemPredicate.of().items("#minecraft:pickaxes"),
						IntBound.atMost(64)
				)))
				.criterion("open_slots", new Criterion(InventoryChangedConditions.inventoryChanged()
						.slots(null, null, IntBound.atLeast(3))))
				.requirements(List.of(List.of("brew_strength"), List.of("enchant_tool"), List.of("wear_down_tool"), List.of("open_slots")));
	}

	public static Advancement buildVillageStoryAdvancement() {
		EntityPredicate villager = EntityPredicate.of().type(Identifier.withDefaultNamespace("villager"));
		EntityPredicate animal = EntityPredicate.of().type("#minecraft:animals");

		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("emerald")))
						.title("Good Neighbor")
						.description("Trade, raise animals, and build a beacon for the settlement.")
						.frame(Display.Frame.GOAL)
				)
				.criterion("trade", new Criterion(TradeConditions.tradedWithVillager().player(
						EntityPredicate.of().targetedEntity(villager)
				)))
				.criterion("breed_animals", new Criterion(BredAnimalsConditions.bredAnimals(animal, animal, null)))
				.criterion("build_beacon", new Criterion(ConstructBeaconConditions.constructedBeacon(IntBound.atLeast(4))))
				.requirements(List.of(List.of("trade"), List.of("breed_animals"), List.of("build_beacon")))
				.rewards(Rewards.ofXp(100));
	}

	public static Advancement buildDangerousAirAdvancement() {
		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("feather")))
						.title("Dangerous Air")
						.description("Float, survive the hit, and come down ready to keep moving.")
						.frame(Display.Frame.CHALLENGE)
				)
				.criterion("levitate_far", new Criterion(LevitationConditions.levitated(
						DistancePredicate.of().y(DoubleBound.atLeast(50.0))
				)))
				.criterion("take_knockback", new Criterion(PlayerHurtEntityConditions.playerHurtEntity(
						DamagePredicate.of().dealt(Range.atLeast(1.0)),
						EntityPredicate.of().type("#minecraft:raiders")
				)))
				.criterion("gain_slow_falling", new Criterion(EffectsChangedConditions.gotEffectsFrom(null)
						.effect("minecraft:slow_falling", Map.of("duration", Map.of("min", 100)))))
				.criterion("use_pearl", new Criterion(SimpleItemConditions.usedItem(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("ender_pearl"))
				)))
				.requirements(List.of(List.of("levitate_far"), List.of("take_knockback"), List.of("gain_slow_falling"), List.of("use_pearl")))
				.rewards(Rewards.ofXp(250));
	}

	public static Advancement buildBlockInteractionAdvancement() {
		LocationPredicate carvedPumpkin = LocationPredicate.of()
				.block(BlockPredicate.of().blocks(Identifier.withDefaultNamespace("carved_pumpkin")));
		EntityPredicate carefulPlayer = EntityPredicate.of().sneaking(true);

		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("carved_pumpkin")))
						.title("Hands on the World")
						.description("Use block interaction triggers with context-aware location checks.")
						.frame(Display.Frame.TASK)
				)
				.criterion("place_pumpkin", Criterion.placedBlock(
						Condition.locationCheck(carvedPumpkin)
				))
				.criterion("allay_delivery", new Criterion(BlockUseConditions.allayDropItemOnBlock(
						Condition.locationCheck(LocationPredicate.of()
								.block(BlockPredicate.of().blocks(Identifier.withDefaultNamespace("note_block"))))
				)))
				.criterion("default_use", new Criterion(BlockUseConditions.defaultBlockUse().player(carefulPlayer)))
				.criterion("any_use", new Criterion(BlockUseConditions.anyBlockUse()))
				.criterion("enter_powder_snow", new Criterion(PlacedBlockConditions.entersBlock(
						Identifier.withDefaultNamespace("powder_snow"),
						Map.of()
				)))
				.criterion("slide_honey", new Criterion(PlacedBlockConditions.slidesDownBlock(
						Identifier.withDefaultNamespace("honey_block"),
						Map.of()
				)))
				.requirements(List.of(
						List.of("place_pumpkin"),
						List.of("allay_delivery"),
						List.of("default_use", "any_use"),
						List.of("enter_powder_snow", "slide_honey")
				));
	}

	public static Advancement buildMobEncounterAdvancement() {
		EntityPredicate wolf = EntityPredicate.of().type(Identifier.withDefaultNamespace("wolf"));
		EntityPredicate zombieVillager = EntityPredicate.of().type(Identifier.withDefaultNamespace("zombie_villager"));
		EntityPredicate evokerFang = EntityPredicate.of().type(Identifier.withDefaultNamespace("evoker_fangs"));
		DamagePredicate magicDamage = DamagePredicate.of().dealt(Range.atLeast(1.0));

		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("bone")))
						.title("Creature Notes")
						.description("Summon, tame, cure, fight, and survive mob encounters.")
						.frame(Display.Frame.GOAL)
				)
				.criterion("summon_helper", new Criterion(EntityTriggerConditions.summonedEntity(
						EntityPredicate.of().type(Identifier.withDefaultNamespace("iron_golem"))
				)))
				.criterion("tame_wolf", new Criterion(EntityTriggerConditions.tamedAnimal(wolf)))
				.criterion("cure_villager", new Criterion(CuredZombieVillagerConditions.curedZombieVillager()
						.player(EntityPredicate.of().targetedEntity(zombieVillager))))
				.criterion("hurt_by_entity", new Criterion(EntityHurtPlayerConditions.entityHurtPlayer(magicDamage)
						.player(EntityPredicate.of().targetedEntity(evokerFang))))
				.criterion("entity_killed_player", new Criterion(PlayerKilledEntityConditions.entityKilledPlayer(
						EntityPredicate.of().type("#minecraft:raiders"),
						null
				)))
				.criterion("kill_near_catalyst", new Criterion(PlayerKilledEntityConditions.killMobNearSculkCatalyst()))
				.requirements(List.of(
						List.of("summon_helper"),
						List.of("tame_wolf"),
						List.of("cure_villager"),
						List.of("hurt_by_entity", "entity_killed_player"),
						List.of("kill_near_catalyst")
				))
				.rewards(Rewards.ofXp(150));
	}

	public static Advancement buildItemUseAdvancement() {
		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("crossbow")))
						.title("Pocket Tools")
						.description("Exercise item-use criteria for common inventory moments.")
						.frame(Display.Frame.TASK)
				)
				.criterion("fill_bucket", new Criterion(SimpleItemConditions.filledBucket(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("water_bucket"))
				)))
				.criterion("eat_stew", new Criterion(SimpleItemConditions.usedItem(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("mushroom_stew"))
				)))
				.criterion("use_totem", new Criterion(SimpleItemConditions.usedTotem(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("totem_of_undying"))
				)))
				.criterion("shoot_crossbow", new Criterion(SimpleItemConditions.shotCrossbow(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("crossbow"))
				)))
				.criterion("hold_spyglass", new Criterion(SimpleItemConditions.usingItem(
						ItemPredicate.of().items(Identifier.withDefaultNamespace("spyglass"))
				)))
				.criterion("unlock_recipe", new Criterion(RecipeUnlockedConditions.recipeUnlocked(
						Identifier.withDefaultNamespace("crossbow")
				)))
				.requirements(List.of(
						List.of("fill_bucket"),
						List.of("eat_stew"),
						List.of("use_totem"),
						List.of("shoot_crossbow", "hold_spyglass"),
						List.of("unlock_recipe")
				));
	}

	public static Advancement buildPlayerStateAdvancement() {
		EntityPredicate rider = EntityPredicate.of().vehicle(EntityPredicate.of()
				.type(Identifier.withDefaultNamespace("horse")));

		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("saddle")))
						.title("Changing Reputation")
						.description("Show the player-only criteria that carry optional player predicates.")
						.frame(Display.Frame.GOAL)
				)
				.criterion("started_riding", new Criterion(PlayerConditions.startedRiding().player(rider)))
				.criterion("raid_won", new Criterion(PlayerConditions.raidWon()))
				.criterion("voluntary_exile", new Criterion(PlayerConditions.voluntaryExile()))
				.criterion("avoid_vibration", new Criterion(PlayerConditions.avoidVibration()
						.player(EntityPredicate.of().sneaking(true))))
				.requirements(List.of(
						List.of("started_riding"),
						List.of("raid_won", "voluntary_exile"),
						List.of("avoid_vibration")
				))
				.rewards(Rewards.ofXp(75));
	}

	public static Advancement buildHiddenDebugGateAdvancement() {
		return new Advancement()
				.parent(myModId("field_notes/root"))
				.display(new Display()
						.icon(Icon.of(Identifier.withDefaultNamespace("barrier")))
						.title("Never by Accident")
						.description("A hidden example of the impossible trigger for manual grants or debug gates.")
						.frame(Display.Frame.CHALLENGE)
						.hidden(true)
						.showToast(false)
						.announceChat(false)
				)
				.criterion("manual_only", Criterion.impossible());
	}

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addAdvancement(buildRootAdvancement(), myModId("field_notes/root"));
		pack.addAdvancement(buildCampSetupAdvancement(), myModId("field_notes/make_camp"));
		pack.addAdvancement(buildOverworldErrandAdvancement(), myModId("field_notes/there_and_back"));
		pack.addAdvancement(buildWorkshopAdvancement(), myModId("field_notes/workshop_habits"));
		pack.addAdvancement(buildVillageStoryAdvancement(), myModId("field_notes/good_neighbor"));
		pack.addAdvancement(buildDangerousAirAdvancement(), myModId("field_notes/dangerous_air"));
		pack.addAdvancement(buildBlockInteractionAdvancement(), myModId("field_notes/hands_on_the_world"));
		pack.addAdvancement(buildMobEncounterAdvancement(), myModId("field_notes/creature_notes"));
		pack.addAdvancement(buildItemUseAdvancement(), myModId("field_notes/pocket_tools"));
		pack.addAdvancement(buildPlayerStateAdvancement(), myModId("field_notes/changing_reputation"));
		pack.addAdvancement(buildHiddenDebugGateAdvancement(), myModId("field_notes/never_by_accident"));
	}

	public static void main() {
		System.out.println("Advancement JSON (mymod:field_notes/root):");
		System.out.println(JsonBytes.encodeToPrettyString(Advancement.CODEC, buildRootAdvancement()));
		System.out.println("Advancement JSON (mymod:field_notes/dangerous_air):");
		System.out.println(JsonBytes.encodeToPrettyString(Advancement.CODEC, buildDangerousAirAdvancement()));
		System.out.println("Advancement JSON (mymod:field_notes/hands_on_the_world):");
		System.out.println(JsonBytes.encodeToPrettyString(Advancement.CODEC, buildBlockInteractionAdvancement()));
		System.out.println("Advancement JSON (mymod:field_notes/pocket_tools):");
		System.out.println(JsonBytes.encodeToPrettyString(Advancement.CODEC, buildItemUseAdvancement()));

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:advancement_examples");
		pack.addDataPackMcmeta("Advancement condition examples generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/advancement_examples"));
	}
}
