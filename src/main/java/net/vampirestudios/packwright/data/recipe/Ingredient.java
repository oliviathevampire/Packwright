package net.vampirestudios.packwright.data.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.vampirestudios.packwright.util.DynamicMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Ingredient {
	public static final Codec<Ingredient> CODEC = Codec.either(
			Codec.either(Codec.STRING, Codec.STRING.listOf()),
			DynamicMap.CODEC
	).comapFlatMap(
			e -> e.map(
					l -> l.map(Ingredient::fromString, Ingredient::fromList),
					Ingredient::fromFabricCustom
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
	private final List<Ingredient> alternatives = new ArrayList<>();
	private DynamicMap fabricCustom;

	Ingredient() {
	}

	public static Ingredient ingredient() {
		return new Ingredient();
	}

	public static Ingredient of(Identifier item) {
		return ingredient().item(item);
	}

	public static Ingredient ofTag(Identifier tag) {
		return ingredient().tag(tag);
	}

	public static Ingredient alternative(Ingredient... ingredients) {
		return ingredient().addAll(ingredients);
	}

	public Ingredient item(Item item) {
		return this.item(BuiltInRegistries.ITEM.getKey(item));
	}

	public Ingredient item(Identifier item) {
		this.clear();
		this.item = item;
		return this;
	}

	public Ingredient tag(Identifier tag) {
		this.clear();
		this.tag = tag;
		return this;
	}

	public Ingredient add(Ingredient ingredient) {
		if (ingredient != null && ingredient.isDefined()) {
			this.alternatives.add(ingredient);
		}

		return this;
	}

	public Ingredient addAll(Ingredient... ingredients) {
		if (ingredients != null) {
			for (Ingredient ingredient : ingredients) {
				this.add(ingredient);
			}
		}

		return this;
	}

	public static Ingredient fabricComponents(Item base, Consumer<DynamicMap> components) {
		return fabricComponents(BuiltInRegistries.ITEM.getKey(base), components, false);
	}

	public static Ingredient fabricComponents(Identifier base, Consumer<DynamicMap> components) {
		return fabricComponents(base, components, false);
	}

	public static Ingredient fabricComponents(Item base, Consumer<DynamicMap> components, boolean strict) {
		return fabricComponents(BuiltInRegistries.ITEM.getKey(base), components, strict);
	}

	public static Ingredient fabricComponents(Identifier base, Consumer<DynamicMap> components, boolean strict) {
		return ingredient().fabricCustom(FABRIC_COMPONENTS, dynamicMap -> {
			dynamicMap.set("base", base.toString());
			dynamicMap.set("strict", strict);
			dynamicMap.set("components", dynamicMap(components));
		});
	}

	public Ingredient fabricCustom(Identifier type, Consumer<DynamicMap> data) {
		this.clear();
		this.fabricCustom = dynamicMap(dynamicMap -> {
			dynamicMap.set("fabric:type", type.toString());
			if (data != null) {
				data.accept(dynamicMap);
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

	public List<Ingredient> getIngredients() {
		return List.copyOf(this.alternatives);
	}

	public DynamicMap getFabricCustom() {
		return this.fabricCustom;
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
		throw new IllegalStateException("Ingredient is empty");
	}

	private List<String> serializedValues() {
		return this.alternatives.stream().map(Ingredient::asString).toList();
	}

	private static DataResult<Ingredient> fromString(String value) {
		if (value == null || value.isBlank()) {
			return DataResult.error(() -> "Ingredient cannot be empty");
		}

		try {
			return value.startsWith("#")
					? value.length() == 1
					  ? DataResult.error(() -> "Ingredient tag cannot be empty")
					  : DataResult.success(ingredient().tag(Identifier.parse(value.substring(1))))
					: DataResult.success(ingredient().item(Identifier.parse(value)));
		} catch (Exception e) {
			return DataResult.error(() -> "Ingredient has invalid id: " + value);
		}
	}

	private static DataResult<Ingredient> fromList(List<String> values) {
		if (values == null || values.isEmpty()) {
			return DataResult.error(() -> "Ingredient alternatives cannot be empty");
		}

		var ingredient = ingredient();

		for (String value : values) {
			var decoded = fromString(value);
			if (decoded.isError()) return decoded;
			ingredient.add(decoded.getOrThrow());
		}

		return DataResult.success(ingredient);
	}

	private static DataResult<Ingredient> fromFabricCustom(DynamicMap object) {
		if (!object.has("fabric:type")) {
			return DataResult.error(() -> "Ingredient object must have 'fabric:type'");
		}

		var ingredient = ingredient();
		ingredient.fabricCustom = object;
		return DataResult.success(ingredient);
	}

	private static DynamicMap dynamicMap(Consumer<DynamicMap> consumer) {
		var map = DynamicMap.object();
		if (consumer != null) {
			consumer.accept(map);
		}
		return map;
	}
}
