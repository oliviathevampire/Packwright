package net.vampirestudios.arrp.data.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public final class Criterion {
	public String trigger;         // e.g. "minecraft:inventory_changed"
	public JsonElement conditions; // trigger-specific payload (optional)

	// JsonElement <-> any ops bridge
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

	// Builders
	public static Criterion of(String trigger) { Criterion c = new Criterion(); c.trigger = trigger; return c; }
	public Criterion conditions(JsonElement e) { this.conditions = e; return this; }

	// ---- Typed helpers (vanilla triggers) ----

	/** minecraft:inventory_changed — any of the provided item predicates */
	public static Criterion inventoryChanged(AdvConditions.ItemPredicate... anyOf) {
		JsonObject cond = AdvConditions.inventoryChanged(anyOf);
		return Criterion.of("minecraft:inventory_changed").conditions(cond);
	}

	/** minecraft:recipe_unlocked */
	public static Criterion recipeUnlocked(String recipeId) {
		return Criterion.of("minecraft:recipe_unlocked").conditions(AdvConditions.recipeUnlocked(recipeId));
	}

	/** minecraft:placed_block */
	public static Criterion placedBlock(String blockId, java.util.Map<String,String> state) {
		return Criterion.of("minecraft:placed_block").conditions(AdvConditions.placedOrEnterBlock("block", blockId, state));
	}

	/** minecraft:enter_block */
	public static Criterion enterBlock(String blockId, java.util.Map<String,String> state) {
		return Criterion.of("minecraft:enter_block").conditions(AdvConditions.placedOrEnterBlock("block", blockId, state));
	}

	/** minecraft:location (dimension/biome/structure/position ranges) */
	public static Criterion location(AdvConditions.LocationPredicate loc) {
		return Criterion.of("minecraft:location").conditions(AdvConditions.location(loc));
	}

	/** minecraft:player_killed_entity (entity predicate + optional killing blow) */
	public static Criterion playerKilledEntity(AdvConditions.EntityPredicate entity, AdvConditions.DamagePredicate dmg) {
		return Criterion.of("minecraft:player_killed_entity").conditions(AdvConditions.playerKilledEntity(entity, dmg));
	}

	/** minecraft:tick — no conditions */
	public static Criterion tick() { return Criterion.of("minecraft:tick"); }

	/** minecraft:impossible — always false, useful for gated grants */
	public static Criterion impossible() { return Criterion.of("minecraft:impossible"); }
}
