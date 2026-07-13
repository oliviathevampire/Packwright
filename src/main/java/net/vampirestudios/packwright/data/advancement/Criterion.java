package net.vampirestudios.packwright.data.advancement;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DamagePredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;

import java.util.Map;

public final class Criterion {
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

	private final CriterionConditions conditions;

	public Criterion(CriterionConditions conditions) {
		this.conditions = conditions;
	}

	public CriterionConditions getConditions() {
		return conditions;
	}

	public String getTrigger() {
		return conditions.getTrigger();
	}

	public static Criterion inventoryChanged(ItemPredicate... anyOf) {
		return new Criterion(InventoryChangedConditions.inventoryChanged(anyOf));
	}

	public static Criterion recipeUnlocked(Identifier recipeId) {
		return new Criterion(RecipeUnlockedConditions.recipeUnlocked(recipeId));
	}

	public static Criterion placedBlock(Identifier blockId, Map<String, String> state) {
		return new Criterion(PlacedBlockConditions.placedBlock(blockId, state));
	}

	public static Criterion enterBlock(Identifier blockId, Map<String, String> state) {
		return new Criterion(PlacedBlockConditions.enterBlock(blockId, state));
	}

	public static Criterion location(LocationPredicate loc) {
		return new Criterion(LocationConditions.location(loc));
	}

	public static Criterion playerKilledEntity(EntityPredicate entity, DamagePredicate dmg) {
		return new Criterion(PlayerKilledEntityConditions.playerKilledEntity(entity, dmg));
	}

	public static Criterion tick() { return new Criterion(EmptyConditions.tick()); }
	public static Criterion impossible() { return new Criterion(EmptyConditions.impossible()); }
}
