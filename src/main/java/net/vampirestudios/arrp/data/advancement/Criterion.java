package net.vampirestudios.arrp.data.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class Criterion {
	private String trigger;
	private JsonElement conditions;

	private static final Codec<JsonElement> JSON = new Codec<>() {
		@Override public <T> DataResult<T> encode(JsonElement v, DynamicOps<T> ops, T prefix) {
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, v).convert(ops).getValue());
		}
		@Override public <T> DataResult<Pair<JsonElement, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			return DataResult.success(Pair.of(el, input));
		}
	};

	public static final Codec<Criterion> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("trigger").forGetter(c -> c.trigger),
			JSON.optionalFieldOf("conditions").forGetter(c -> java.util.Optional.ofNullable(c.conditions))
	).apply(i, (t, cond) -> {
		Criterion c = new Criterion();
		c.trigger = t;
		c.conditions = cond.orElse(null);
		return c;
	}));

	public static Criterion of(String trigger) { Criterion c = new Criterion(); c.trigger = trigger; return c; }
	public Criterion conditions(JsonElement e) { this.conditions = e; return this; }

	public String getTrigger() { return trigger; }
	public JsonElement getConditions() { return conditions; }

	public static Criterion inventoryChanged(AdvConditions.ItemPredicate... anyOf) {
		return Criterion.of("minecraft:inventory_changed").conditions(AdvConditions.inventoryChanged(anyOf));
	}

	public static Criterion recipeUnlocked(String recipeId) {
		return Criterion.of("minecraft:recipe_unlocked").conditions(AdvConditions.recipeUnlocked(recipeId));
	}

	public static Criterion placedBlock(String blockId, java.util.Map<String,String> state) {
		return Criterion.of("minecraft:placed_block").conditions(AdvConditions.placedOrEnterBlock("block", blockId, state));
	}

	public static Criterion enterBlock(String blockId, java.util.Map<String,String> state) {
		return Criterion.of("minecraft:enter_block").conditions(AdvConditions.placedOrEnterBlock("block", blockId, state));
	}

	public static Criterion location(AdvConditions.LocationPredicate loc) {
		return Criterion.of("minecraft:location").conditions(AdvConditions.location(loc));
	}

	public static Criterion playerKilledEntity(AdvConditions.EntityPredicate entity, AdvConditions.DamagePredicate dmg) {
		return Criterion.of("minecraft:player_killed_entity").conditions(AdvConditions.playerKilledEntity(entity, dmg));
	}

	public static Criterion tick() { return Criterion.of("minecraft:tick"); }
	public static Criterion impossible() { return Criterion.of("minecraft:impossible"); }
}
