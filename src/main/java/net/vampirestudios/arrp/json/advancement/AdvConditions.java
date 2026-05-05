// src/main/java/arrp/adv/AdvConditions.java
package net.vampirestudios.arrp.json.advancement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class AdvConditions {
	private AdvConditions() {
	}

	// -------- inventory_changed --------

	public static JsonObject inventoryChanged(ItemPredicate... anyOf) {
		JsonObject root = new JsonObject();
		if (anyOf != null && anyOf.length > 0) {
			JsonArray items = new JsonArray();
			for (ItemPredicate p : anyOf) items.add(p.toJson());
			root.add("items", items);
		}
		return root;
	}

	public static JsonObject recipeUnlocked(String recipeId) {
		JsonObject o = new JsonObject();
		o.addProperty("recipe", recipeId);
		return o;
	}

	// -------- recipe_unlocked --------

	public static JsonObject placedOrEnterBlock(String key, String blockId, Map<String, String> state) {
		JsonObject o = new JsonObject();
		o.addProperty(key, blockId);           // key = "block"
		if (state != null && !state.isEmpty()) {
			JsonObject st = new JsonObject();
			for (var e : state.entrySet()) st.addProperty(e.getKey(), e.getValue());
			o.add("state", st);
		}
		return o;
	}

	// -------- placed_block / enter_block --------

	public static JsonObject location(LocationPredicate p) {
		return p.obj;
	}

	// -------- location --------

	public static JsonObject playerKilledEntity(EntityPredicate entity, DamagePredicate damage) {
		JsonObject o = new JsonObject();
		if (entity != null) o.add("entity", entity.obj);
		if (damage != null) o.add("killing_blow", damage.obj);
		return o;
	}

	/** Minimal item predicate: items list + optional count min/max. Extend as needed. */
	public static final class ItemPredicate {
		private final JsonObject obj = new JsonObject();

		public static ItemPredicate anyOf(Identifier... itemIds) {
			ItemPredicate p = new ItemPredicate();
			JsonArray arr = new JsonArray();
			for (Identifier id : itemIds) arr.add(id.toString());
			p.obj.add("items", arr);
			return p;
		}

		public static ItemPredicate anyOf(Item... itemIds) {
			ItemPredicate p = new ItemPredicate();
			JsonArray arr = new JsonArray();
			for (Item id : itemIds) {
				arr.add(BuiltInRegistries.ITEM.getKey(id).toString());
			}
			p.obj.add("items", arr);
			return p;
		}

		public ItemPredicate countMin(int min) {
			ensureCount().addProperty("min", min);
			return this;
		}

		public ItemPredicate countMax(int max) {
			ensureCount().addProperty("max", max);
			return this;
		}

		// in AdvConditions.ItemPredicate
		public ItemPredicate componentPresent(String id) {
			JsonObject c = ensureComponents();
			JsonObject rule = new JsonObject();
			rule.addProperty("present", true);
			c.add(id, rule);
			return this;
		}

		public ItemPredicate componentAbsent(String id) {
			JsonObject c = ensureComponents();
			JsonObject rule = new JsonObject();
			rule.addProperty("present", false);
			c.add(id, rule);
			return this;
		}

		/** Exact match: value is the component's JSON value (Text, numbers, objects, etc.). */
		public ItemPredicate componentEquals(Identifier id, com.google.gson.JsonElement value) {
			ensureComponents().add(id.toString(), value);
			return this;
		}

		/** Numeric range for number-like components (min/max are optional). */
		public ItemPredicate componentNumberRange(String id, Double min, Double max) {
			JsonObject range = new JsonObject();
			if (min != null) range.addProperty("min", min);
			if (max != null) range.addProperty("max", max);
			ensureComponents().add(id, range);
			return this;
		}

		private JsonObject ensureComponents() {
			if (!obj.has("components")) obj.add("components", new JsonObject());
			return obj.getAsJsonObject("components");
		}

		private JsonObject ensureCount() {
			if (!obj.has("count")) obj.add("count", new JsonObject());
			return obj.getAsJsonObject("count");
		}

		public JsonObject toJson() {
			return obj;
		}
	}

	// -------- player_killed_entity --------

	public static final class LocationPredicate {
		private final JsonObject obj = new JsonObject();

		public static LocationPredicate create() {
			return new LocationPredicate();
		}

		public LocationPredicate dimension(String id) {
			obj.addProperty("dimension", id);
			return this;
		}

		public LocationPredicate biome(String id) {
			obj.addProperty("biome", id);
			return this;
		}

		public LocationPredicate structure(String id) {
			obj.addProperty("structure", id);
			return this;
		}

		/** x/y/z ranges: use {min,max}. Supply only what you need. */
		public LocationPredicate position(Double minY, Double maxY) {
			JsonObject pos = obj.has("position") ? obj.getAsJsonObject("position") : new JsonObject();
			JsonObject y = new JsonObject();
			if (minY != null) y.addProperty("min", minY);
			if (maxY != null) y.addProperty("max", maxY);
			pos.add("y", y);
			obj.add("position", pos);
			return this;
		}
	}

	public static final class EntityPredicate {
		private final JsonObject obj = new JsonObject();

		public static EntityPredicate type(String id) {
			EntityPredicate p = new EntityPredicate();
			p.obj.addProperty("type", id);
			return p;
		}

		/** Entity properties extension point; add more as needed. */
		public EntityPredicate nbt(String snbt) {
			obj.addProperty("nbt", snbt);
			return this;
		}
	}

	public static final class DamagePredicate {
		private final JsonObject obj = new JsonObject();

		public static DamagePredicate source(String id) {
			DamagePredicate d = new DamagePredicate();
			JsonObject src = new JsonObject();
			src.addProperty("tags", id);
			d.obj.add("source", src);
			return d;
		}

		public DamagePredicate dealtMin(double v) {
			ensureDealt().addProperty("min", v);
			return this;
		}

		private JsonObject ensureDealt() {
			if (!obj.has("dealt")) obj.add("dealt", new JsonObject());
			return obj.getAsJsonObject("dealt");
		}
	}
}
