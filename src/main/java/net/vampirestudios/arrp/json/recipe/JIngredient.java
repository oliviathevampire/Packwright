package net.vampirestudios.arrp.json.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class JIngredient implements Cloneable {
	public static final Codec<JIngredient> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<T> encode(JIngredient v, DynamicOps<T> ops, T prefix) {
			if (v.fabricCustom != null) {
				// write the custom object verbatim
				return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, v.fabricCustom).convert(ops).getValue());
			}
			if (v.item != null) {
				return ops.mapBuilder()
						.add(ops.createString("item"), Identifier.CODEC.encodeStart(ops, v.item).getOrThrow())
						.build(prefix);
			}
			if (v.tag != null) {
				return ops.mapBuilder()
						.add(ops.createString("tag"), Identifier.CODEC.encodeStart(ops, v.tag).getOrThrow())
						.build(prefix);
			}
			return DataResult.error(() -> "JIngredient: empty; set item(), tag(), or fabricCustom()");
		}

		@Override
		public <T> DataResult<Pair<JIngredient, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) return DataResult.error(() -> "Ingredient must be an object");
			JsonObject obj = el.getAsJsonObject();

			// Fabric custom branch
			var ft = obj.get("fabric:type");
			if (ft != null && ft.isJsonPrimitive() && ft.getAsJsonPrimitive().isString()) {
				JIngredient ing = new JIngredient();
				ing.fabricCustom = obj.deepCopy(); // store entire custom object
				return DataResult.success(Pair.of(ing, input));
			}

			// Vanilla branches
			var itemEl = obj.get("item");
			var tagEl = obj.get("tag");
			if (itemEl != null) {
				var id = Identifier.CODEC.parse(JsonOps.INSTANCE, itemEl).result();
				return id.<DataResult<Pair<JIngredient, T>>>map(identifier -> DataResult.success(Pair.of(JIngredient.ingredient().item(identifier), input))).orElseGet(() -> DataResult.error(() -> "Ingredient: bad 'item'"));
			}
			if (tagEl != null) {
				var id = Identifier.CODEC.parse(JsonOps.INSTANCE, tagEl).result();
				return id.map(identifier -> DataResult.success(Pair.of(JIngredient.ingredient().tag(identifier), input))).orElseGet(() -> DataResult.error(() -> "Ingredient: bad 'tag'"));
			}
			return DataResult.error(() -> "Ingredient: expected 'item', 'tag', or 'fabric:type'");
		}
	};
	protected Identifier item;
	protected Identifier tag;
	protected List<JIngredient> ingredients;
	private JsonObject fabricCustom;      // entire object for fabric custom ingredients

	JIngredient() {
	}

	public JIngredient(Identifier item, Identifier tag, List<JIngredient> ingredients) {
		this.item = item;
		this.tag = tag;
		this.ingredients = ingredients;
	}

	public static JIngredient ingredient() {
		return new JIngredient();
	}

	public boolean isFabricCustom() {
		return fabricCustom != null;
	}

	public JIngredient fabricCustom(Identifier type, java.util.function.Consumer<JsonObject> data) {
		JsonObject obj = new JsonObject();
		obj.addProperty("fabric:type", type.toString());
		if (data != null) data.accept(obj);
		this.fabricCustom = obj;
		this.item = null;
		this.tag = null;
		return this;
	}

	public JIngredient item(Item item) {
		return this.item(BuiltInRegistries.ITEM.getKey(item));
	}

	public JIngredient item(Identifier id) {
		if (this.isDefined()) {
			return this.add(JIngredient.ingredient().item(id));
		}

		this.item = id;

		return this;
	}

	public JIngredient tag(Identifier tag) {
		if (this.isDefined()) {
			return this.add(JIngredient.ingredient().tag(tag));
		}

		this.tag = tag;

		return this;
	}

	public JIngredient add(final JIngredient ingredient) {
		if (this.ingredients == null) {
			final List<JIngredient> ingredients = new ArrayList<>();

			if (this.isDefined()) {
				ingredients.add(this.clone());
			}

			this.ingredients = ingredients;
		}

		this.ingredients.add(ingredient);

		return this;
	}

	private boolean isDefined() {
		return this.item != null || this.tag != null;
	}

	@Override
	public JIngredient clone() {
		try {
			return (JIngredient) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	public Identifier getItem() {
		return item;
	}

	public Identifier getTag() {
		return tag;
	}

	public List<JIngredient> getIngredients() {
		return ingredients;
	}
}
