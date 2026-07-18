package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.vampirestudios.packwright.util.DynamicMap;

import java.util.Optional;
import java.util.function.Consumer;

public class Result {
	public static final Codec<Result> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("id").forGetter(r -> r.itemId),
			Codec.INT.optionalFieldOf("count").forGetter(r ->
					r.hasCount() ? Optional.of(r.getCount()) : Optional.empty()
			),
			DynamicMap.CODEC.optionalFieldOf("components").forGetter(r ->
					r.hasComponents() ? Optional.of(r.components) : Optional.empty()
			)
	).apply(i, (id, count, components) -> {
		Result result = count.isPresent()
				? Result.stackedResult(id, count.get())
				: Result.result(id);

		components.ifPresent(result::components);
		return result;
	}));

	protected final Identifier itemId;
	protected DynamicMap components;

	Result(final Identifier id) {
		this.itemId = id;
	}

	public Result(Identifier itemId, DynamicMap components) {
		this.itemId = itemId;
		this.components = components;
	}

	public static Result item(final Item item) {
		return result(BuiltInRegistries.ITEM.getKey(item));
	}

	public static Result result(final Identifier id) {
		return new Result(id);
	}

	public static Result item(Item item, DynamicMap components) {
		return result(BuiltInRegistries.ITEM.getKey(item)).components(components);
	}

	public static StackedResult itemStack(Item item, int count, DynamicMap components) {
		return (StackedResult) stackedResult(BuiltInRegistries.ITEM.getKey(item), count).components(components);
	}

	public static StackedResult itemStack(final Item item, final int count) {
		return stackedResult(BuiltInRegistries.ITEM.getKey(item), count);
	}

	public static StackedResult stackedResult(final Identifier id, final int count) {
		if (count < 1) {
			throw new IllegalArgumentException("Recipe result count must be at least 1: " + count);
		}

		final StackedResult result = new StackedResult(id);
		result.count = count;
		return result;
	}

	public Result components(DynamicMap components) {
		this.components = components;
		return this;
	}

	public Result components(Consumer<DynamicMap> build) {
		DynamicMap object = DynamicMap.object();

		if (build != null) {
			build.accept(object);
		}

		this.components = object;
		return this;
	}

	public Identifier getItemId() {
		return itemId;
	}

	public DynamicMap getComponents() {
		return components;
	}

	public int getCount() {
		return this instanceof StackedResult stacked ? stacked.getCount() : 1;
	}

	public boolean hasComponents() {
		return components != null && !components.isEmpty();
	}

	public boolean hasCount() {
		return this instanceof StackedResult stacked && stacked.getCount() != 1;
	}
}