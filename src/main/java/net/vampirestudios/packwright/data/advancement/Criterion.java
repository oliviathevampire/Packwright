package net.vampirestudios.packwright.data.advancement;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.predicate.DamagePredicate;
import net.vampirestudios.packwright.data.predicate.DistancePredicate;
import net.vampirestudios.packwright.data.predicate.DoubleBound;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;

import java.util.Map;

public record Criterion(CriterionConditions conditions) {
	public static final Codec<Criterion> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(Criterion c, DynamicOps<T> ops, T prefix) {
			String trigger = c.conditions.getTrigger();
			Codec<CriterionConditions> codec = CriterionConditions.codecFor(trigger);
			if (codec == null) return DataResult.error(() -> "No conditions codec registered for trigger: " + trigger);
			DataResult<T> conditionsResult = codec.encodeStart(ops, c.conditions);
			return conditionsResult.flatMap(conditionsValue ->
					ops.mergeToMap(prefix, ops.createString("trigger"), ops.createString(trigger))
							.flatMap(withTrigger -> ops.mergeToMap(withTrigger, ops.createString("conditions"), conditionsValue)));
		}

		@Override
		public <T> DataResult<Pair<Criterion, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				T triggerNode = map.get("trigger");
				if (triggerNode == null) return DataResult.error(() -> "missing 'trigger'");
				var triggerRes = ops.getStringValue(triggerNode);
				if (triggerRes.result().isEmpty()) return DataResult.error(() -> "'trigger' must be a string");
				String trigger = triggerRes.result().get();

				Codec<CriterionConditions> codec = CriterionConditions.codecFor(trigger);
				if (codec == null) return DataResult.error(() -> "Unknown trigger: " + trigger);

				T conditionsNode = map.get("conditions") == null ? ops.emptyMap() : map.get("conditions");
				return codec.parse(ops, conditionsNode).map(conditions -> Pair.of(new Criterion(conditions), input));
			});
		}
	};

	public String getTrigger() {
		return conditions.getTrigger();
	}

	public static Criterion inventoryChanged(ItemPredicate... anyOf) {
		return new Criterion(InventoryChangedConditions.inventoryChanged(anyOf));
	}

	public static Criterion recipeUnlocked(Identifier recipeId) {
		return new Criterion(RecipeUnlockedConditions.recipeUnlocked(recipeId));
	}

	public static Criterion placedBlock(Condition... conditions) {
		return new Criterion(BlockUseConditions.placedBlock(conditions));
	}

	public static Criterion enterBlock(Identifier blockId, Map<String, String> state) {
		return new Criterion(PlacedBlockConditions.entersBlock(blockId, state));
	}

	public static Criterion location(LocationPredicate loc) {
		return new Criterion(PlayerConditions.location(loc));
	}

	public static Criterion playerKilledEntity(EntityPredicate entity, DamagePredicate dmg) {
		return new Criterion(PlayerKilledEntityConditions.playerKilledEntity(entity, dmg));
	}

	public static Criterion tick() {
		return new Criterion(PlayerConditions.tick());
	}

	public static Criterion impossible() {
		return new Criterion(EmptyConditions.impossible());
	}

	// ---------- player-only ----------

	public static Criterion sleptInBed() { return new Criterion(PlayerConditions.sleptInBed()); }
	public static Criterion raidWon() { return new Criterion(PlayerConditions.raidWon()); }
	public static Criterion voluntaryExile() { return new Criterion(PlayerConditions.voluntaryExile()); }
	public static Criterion avoidVibration() { return new Criterion(PlayerConditions.avoidVibration()); }
	public static Criterion startedRiding() { return new Criterion(PlayerConditions.startedRiding()); }

	// ---------- killed entity / player ----------

	public static Criterion entityKilledPlayer(EntityPredicate entity, DamagePredicate dmg) {
		return new Criterion(PlayerKilledEntityConditions.entityKilledPlayer(entity, dmg));
	}

	public static Criterion killMobNearSculkCatalyst() {
		return new Criterion(PlayerKilledEntityConditions.killMobNearSculkCatalyst());
	}

	// ---------- recipes ----------

	public static Criterion craftedItem(Identifier recipeId, ItemPredicate... ingredients) {
		return new Criterion(RecipeCraftedConditions.craftedItem(recipeId, ingredients));
	}

	public static Criterion crafterCraftedItem(Identifier recipeId) {
		return new Criterion(RecipeCraftedConditions.crafterCraftedItem(recipeId));
	}

	// ---------- blocks ----------

	public static Criterion slidesDownBlock(Identifier blockId, Map<String, String> state) {
		return new Criterion(PlacedBlockConditions.slidesDownBlock(blockId, state));
	}

	public static Criterion itemUsedOnBlock(Condition... location) { return new Criterion(BlockUseConditions.itemUsedOnBlock(location)); }
	public static Criterion allayDropItemOnBlock(Condition... location) { return new Criterion(BlockUseConditions.allayDropItemOnBlock(location)); }
	public static Criterion defaultBlockUse() { return new Criterion(BlockUseConditions.defaultBlockUse()); }
	public static Criterion anyBlockUse() { return new Criterion(BlockUseConditions.anyBlockUse()); }

	public static Criterion destroyedBeeNest(Identifier block, ItemPredicate item, IntBound numBeesInside) {
		return new Criterion(BeeNestDestroyedConditions.destroyedBeeNest(block, item, numBeesInside));
	}

	public static Criterion targetHit(IntBound signalStrength, EntityPredicate projectile) {
		return new Criterion(TargetBlockConditions.targetHit(signalStrength, projectile));
	}

	// ---------- combat ----------

	public static Criterion playerHurtEntity(DamagePredicate damage, EntityPredicate entity) {
		return new Criterion(PlayerHurtEntityConditions.playerHurtEntity(damage, entity));
	}

	public static Criterion entityHurtPlayer(DamagePredicate damage) {
		return new Criterion(EntityHurtPlayerConditions.entityHurtPlayer(damage));
	}

	public static Criterion channeledLightning(EntityPredicate... victims) {
		return new Criterion(ChanneledLightningConditions.channeledLightning(victims));
	}

	public static Criterion crossbowKilled(ItemPredicate firedFromWeapon, EntityPredicate... victims) {
		return new Criterion(KilledByArrowConditions.crossbowKilled(firedFromWeapon, victims));
	}

	public static Criterion spearMobs(int requiredCount) {
		return new Criterion(SpearMobsConditions.spearMobs(requiredCount));
	}

	public static Criterion lightningStrike(EntityPredicate lightning, EntityPredicate bystander) {
		return new Criterion(LightningStrikeConditions.lightningStrike(lightning, bystander));
	}

	// ---------- items ----------

	public static Criterion filledBucket(ItemPredicate item) { return new Criterion(SimpleItemConditions.filledBucket(item)); }
	public static Criterion usedItem(ItemPredicate item) { return new Criterion(SimpleItemConditions.usedItem(item)); }
	public static Criterion usedTotem(ItemPredicate item) { return new Criterion(SimpleItemConditions.usedTotem(item)); }
	public static Criterion shotCrossbow(ItemPredicate item) { return new Criterion(SimpleItemConditions.shotCrossbow(item)); }
	public static Criterion usingItem(ItemPredicate item) { return new Criterion(SimpleItemConditions.usingItem(item)); }

	public static Criterion enchantedItem() { return new Criterion(EnchantedItemConditions.enchantedItem()); }
	public static Criterion enchantedItem(ItemPredicate item, IntBound levels) {
		return new Criterion(EnchantedItemConditions.enchantedItem(item, levels));
	}

	public static Criterion changedDurability(ItemPredicate item, IntBound durability) {
		return new Criterion(ItemDurabilityConditions.changedDurability(item, durability));
	}

	public static Criterion fishedItem(ItemPredicate rod, EntityPredicate entity, ItemPredicate item) {
		return new Criterion(FishingRodHookedConditions.fishedItem(rod, entity, item));
	}

	public static Criterion thrownItemPickedUpByEntity(ItemPredicate item, EntityPredicate entity) {
		return new Criterion(PickedUpItemConditions.thrownItemPickedUpByEntity(item, entity));
	}

	public static Criterion thrownItemPickedUpByPlayer(ItemPredicate item, EntityPredicate entity) {
		return new Criterion(PickedUpItemConditions.thrownItemPickedUpByPlayer(item, entity));
	}

	public static Criterion itemUsedOnEntity(ItemPredicate item, EntityPredicate entity) {
		return new Criterion(PlayerInteractConditions.itemUsedOnEntity(item, entity));
	}

	public static Criterion equipmentSheared(ItemPredicate item, EntityPredicate entity) {
		return new Criterion(PlayerInteractConditions.equipmentSheared(item, entity));
	}

	// ---------- brewing / beacons / ender eye ----------

	public static Criterion brewedPotion() { return new Criterion(BrewedPotionConditions.brewedPotion()); }
	public static Criterion brewedPotion(Identifier potion) { return new Criterion(BrewedPotionConditions.brewedPotion(potion)); }

	public static Criterion constructedBeacon() { return new Criterion(ConstructBeaconConditions.constructedBeacon()); }
	public static Criterion constructedBeacon(IntBound level) { return new Criterion(ConstructBeaconConditions.constructedBeacon(level)); }

	public static Criterion usedEnderEye(DoubleBound distance) { return new Criterion(UsedEnderEyeConditions.usedEnderEye(distance)); }

	// ---------- entities ----------

	public static Criterion summonedEntity(EntityPredicate entity) { return new Criterion(EntityTriggerConditions.summonedEntity(entity)); }
	public static Criterion tamedAnimal() { return new Criterion(EntityTriggerConditions.tamedAnimal()); }
	public static Criterion tamedAnimal(EntityPredicate entity) { return new Criterion(EntityTriggerConditions.tamedAnimal(entity)); }

	public static Criterion bredAnimals() { return new Criterion(BredAnimalsConditions.bredAnimals()); }
	public static Criterion bredAnimals(EntityPredicate parent, EntityPredicate partner, EntityPredicate child) {
		return new Criterion(BredAnimalsConditions.bredAnimals(parent, partner, child));
	}

	public static Criterion curedZombieVillager() { return new Criterion(CuredZombieVillagerConditions.curedZombieVillager()); }
	public static Criterion tradedWithVillager() { return new Criterion(TradeConditions.tradedWithVillager()); }

	public static Criterion gotEffectsFrom(EntityPredicate source) { return new Criterion(EffectsChangedConditions.gotEffectsFrom(source)); }

	// ---------- distance / travel ----------

	public static Criterion levitated(DistancePredicate distance) { return new Criterion(LevitationConditions.levitated(distance)); }
	public static Criterion travelledThroughNether(DistancePredicate distance) { return new Criterion(DistanceConditions.travelledThroughNether(distance)); }
	public static Criterion fallFromHeight(EntityPredicate player, DistancePredicate distance, LocationPredicate startPosition) {
		return new Criterion(DistanceConditions.fallFromHeight(player, distance, startPosition));
	}
	public static Criterion rideEntityInLava(EntityPredicate player, DistancePredicate distance) {
		return new Criterion(DistanceConditions.rideEntityInLava(player, distance));
	}
	public static Criterion fallAfterExplosion(DistancePredicate distance, EntityPredicate cause) {
		return new Criterion(FallAfterExplosionConditions.fallAfterExplosion(distance, cause));
	}

	public static Criterion changedDimension() { return new Criterion(ChangeDimensionConditions.changedDimension()); }
	public static Criterion changedDimension(Identifier from, Identifier to) { return new Criterion(ChangeDimensionConditions.changedDimension(from, to)); }
	public static Criterion changedDimensionTo(Identifier to) { return new Criterion(ChangeDimensionConditions.changedDimensionTo(to)); }
	public static Criterion changedDimensionFrom(Identifier from) { return new Criterion(ChangeDimensionConditions.changedDimensionFrom(from)); }

	// ---------- loot ----------

	public static Criterion lootTableUsed(Identifier lootTable) { return new Criterion(LootTableConditions.lootTableUsed(lootTable)); }
}
