package net.vampirestudios.arrp.json.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.function.Consumer;

public class JResult {
	protected static final Codec<JsonObject> JSON_OBJECT_CODEC = Codec.PASSTHROUGH.xmap(
			dynamic -> dynamic.convert(JsonOps.INSTANCE).getValue().getAsJsonObject(),
			object -> new Dynamic<>(JsonOps.INSTANCE, object)
	);

	public static final Codec<JResult> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(r -> r.itemId),
			Codec.INT.optionalFieldOf("count").forGetter(r ->
					r.hasCount() ? Optional.of(r.getCount()) : Optional.empty()
			),
			JSON_OBJECT_CODEC.optionalFieldOf("components").forGetter(r ->
					r.hasComponents() ? Optional.of(r.components) : Optional.empty()
			)
	).apply(i, (id, count, components) -> {
		JResult result = count.isPresent()
				? JResult.stackedResult(id, count.get())
				: JResult.result(id);

		components.ifPresent(result::components);
		return result;
	}));

	protected final Identifier itemId;
	protected JsonObject components;

	JResult(final Identifier id) {
		this.itemId = id;
	}

	public JResult(Identifier itemId, JsonObject components) {
		this.itemId = itemId;
		this.components = components == null ? null : components.deepCopy();
	}

	public static JResult item(final Item item) {
		return result(BuiltInRegistries.ITEM.getKey(item));
	}

	public static JResult result(final Identifier id) {
		return new JResult(id);
	}

	public static JResult item(Item item, JsonObject components) {
		return result(BuiltInRegistries.ITEM.getKey(item)).components(components);
	}

	public static JStackedResult itemStack(Item item, int count, JsonObject components) {
		return (JStackedResult) stackedResult(BuiltInRegistries.ITEM.getKey(item), count).components(components);
	}

	public static JStackedResult itemStack(final Item item, final int count) {
		return stackedResult(BuiltInRegistries.ITEM.getKey(item), count);
	}

	public static JStackedResult stackedResult(final Identifier id, final int count) {
		if (count < 1) {
			throw new IllegalArgumentException("Recipe result count must be at least 1: " + count);
		}

		final JStackedResult result = new JStackedResult(id);
		result.count = count;
		return result;
	}

	public JResult components(JsonObject components) {
		this.components = components == null ? null : components.deepCopy();
		return this;
	}

	public JResult components(Consumer<JsonObject> build) {
		JsonObject object = new JsonObject();

		if (build != null) {
			build.accept(object);
		}

		this.components = object;
		return this;
	}

	public Identifier getItemId() {
		return itemId;
	}

	public JsonObject getComponents() {
		return components == null ? null : components.deepCopy();
	}

	public int getCount() {
		return this instanceof JStackedResult stacked ? stacked.getCount() : 1;
	}

	public boolean hasComponents() {
		return components != null && !components.isEmpty();
	}

	public boolean hasCount() {
		return this instanceof JStackedResult stacked && stacked.getCount() != 1;
	}
}