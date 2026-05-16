package net.vampirestudios.arrp.json.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JIngredient {
	public static final Codec<JIngredient> CODEC = Codec.either(
			Codec.either(Codec.STRING, Codec.STRING.listOf()),
			Codecs.JSON_OBJECT
	).comapFlatMap(
			e -> e.map(
					l -> l.map(JIngredient::fromString, JIngredient::fromList),
					JIngredient::fromFabricCustom
			),
			i -> {
				if (i.isAlternative()) {
					return Either.left(
							Either.right(i.serializedValues())
					);
				}

				if (i.fabricCustom != null) {
					return Either.right(i.getFabricCustom());
				}

				return Either.left(Either.left(i.asString()));
			}
	);

	private static final Identifier FABRIC_COMPONENTS = Identifier.fromNamespaceAndPath("fabric", "components");

	private Identifier item;
	private Identifier tag;
	private final List<JIngredient> alternatives = new ArrayList<>();
	private JsonObject fabricCustom;

	JIngredient() {
	}

	public static JIngredient ingredient() {
		return new JIngredient();
	}

	public static JIngredient alternative(JIngredient... ingredients) {
		return ingredient().addAll(ingredients);
	}

	public JIngredient item(Item item) {
		return this.item(BuiltInRegistries.ITEM.getKey(item));
	}

	public JIngredient item(Identifier item) {
		this.clear();
		this.item = item;
		return this;
	}

	public JIngredient tag(Identifier tag) {
		this.clear();
		this.tag = tag;
		return this;
	}

	public JIngredient add(JIngredient ingredient) {
		if (ingredient != null && ingredient.isDefined()) {
			this.alternatives.add(ingredient);
		}

		return this;
	}

	public JIngredient addAll(JIngredient... ingredients) {
		if (ingredients != null) {
			for (JIngredient ingredient : ingredients) {
				this.add(ingredient);
			}
		}

		return this;
	}

	public static JIngredient fabricComponents(Item base, Consumer<JsonObject> components) {
		return fabricComponents(BuiltInRegistries.ITEM.getKey(base), components, false);
	}

	public static JIngredient fabricComponents(Identifier base, Consumer<JsonObject> components) {
		return fabricComponents(base, components, false);
	}

	public static JIngredient fabricComponents(Item base, Consumer<JsonObject> components, boolean strict) {
		return fabricComponents(BuiltInRegistries.ITEM.getKey(base), components, strict);
	}

	public static JIngredient fabricComponents(Identifier base, Consumer<JsonObject> components, boolean strict) {
		return ingredient().fabricCustom(FABRIC_COMPONENTS, json -> {
			json.addProperty("base", base.toString());
			json.addProperty("strict", strict);
			json.add("components", jsonObject(components));
		});
	}

	public JIngredient fabricCustom(Identifier type, Consumer<JsonObject> data) {
		this.clear();
		this.fabricCustom = jsonObject(json -> {
			json.addProperty("fabric:type", type.toString());
			if (data != null) {
				data.accept(json);
			}
		});
		return this;
	}

	public boolean isAlternative() {
		return !this.alternatives.isEmpty();
	}

	public boolean isDefined() {
		return this.item != null || this.tag != null || this.fabricCustom != null || this.isAlternative();
	}

	public Identifier getItem() {
		return this.item;
	}

	public Identifier getTag() {
		return this.tag;
	}

	public List<JIngredient> getIngredients() {
		return List.copyOf(this.alternatives);
	}

	public JsonObject getFabricCustom() {
		return this.fabricCustom == null ? null : this.fabricCustom.deepCopy();
	}

	private void clear() {
		this.item = null;
		this.tag = null;
		this.fabricCustom = null;
		this.alternatives.clear();
	}

	private String asString() {
		if (this.item != null) return this.item.toString();
		if (this.tag != null) return "#" + this.tag;
		throw new IllegalStateException("JIngredient is empty");
	}

	private List<String> serializedValues() {
		return this.alternatives.stream().map(JIngredient::asString).toList();
	}

	private static DataResult<JIngredient> fromString(String value) {
		if (value == null || value.isBlank()) {
			return DataResult.error(() -> "JIngredient cannot be empty");
		}

		try {
			return value.startsWith("#")
					? value.length() == 1
					  ? DataResult.error(() -> "JIngredient tag cannot be empty")
					  : DataResult.success(ingredient().tag(Identifier.parse(value.substring(1))))
					: DataResult.success(ingredient().item(Identifier.parse(value)));
		} catch (Exception e) {
			return DataResult.error(() -> "JIngredient has invalid id: " + value);
		}
	}

	private static DataResult<JIngredient> fromList(List<String> values) {
		if (values == null || values.isEmpty()) {
			return DataResult.error(() -> "JIngredient alternatives cannot be empty");
		}

		var ingredient = ingredient();

		for (String value : values) {
			var decoded = fromString(value);
			if (decoded.isError()) return decoded;
			ingredient.add(decoded.getOrThrow());
		}

		return DataResult.success(ingredient);
	}

	private static DataResult<JIngredient> fromFabricCustom(JsonObject object) {
		if (!object.has("fabric:type")) {
			return DataResult.error(() -> "JIngredient object must have 'fabric:type'");
		}

		var ingredient = ingredient();
		ingredient.fabricCustom = object.deepCopy();
		return DataResult.success(ingredient);
	}

	private static JsonObject jsonObject(Consumer<JsonObject> consumer) {
		var json = new JsonObject();
		if (consumer != null) {
			consumer.accept(json);
		}
		return json;
	}
}