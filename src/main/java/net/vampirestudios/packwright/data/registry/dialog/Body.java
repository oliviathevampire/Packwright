package net.vampirestudios.packwright.data.registry.dialog;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.util.DynamicMap;

import java.util.Optional;

/**
 * A dialog body element. Vanilla defines two types (bootstrapped by {@code DialogBodyTypes}):
 * {@link PlainMessage} (plain/translatable text) and {@link Item} (an item render with an
 * optional description).
 */
public sealed interface Body permits Body.PlainMessage, Body.Item {
	Codec<Body> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<Body, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "plain_message" -> PlainMessage.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "item" -> Item.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported dialog body type");
			});
		}

		@Override
		public <T> DataResult<T> encode(Body input, DynamicOps<T> ops, T prefix) {
			if (input instanceof PlainMessage b) return PlainMessage.CODEC.codec().encode(b, ops, prefix);
			if (input instanceof Item b) return Item.CODEC.codec().encode(b, ops, prefix);
			return DataResult.error(() -> "Unsupported dialog body: " + input.getClass().getSimpleName());
		}
	};

	static PlainMessage plainMessage(String contents) { return new PlainMessage(contents, 200); }
	static Item item(ItemStack item) { return new Item(item, Optional.empty(), true, true, 16, 16); }

	/** plain (or translatable-key) text, vanilla's {@code minecraft:plain_message} body type */
	record PlainMessage(String contents, int width) implements Body {
		/** just the {@code contents}/{@code width} fields, no {@code type} — for embedding elsewhere (e.g. {@link Item#description}) */
		static final MapCodec<PlainMessage> FIELDS_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("contents").forGetter(PlainMessage::contents),
				Codec.INT.fieldOf("width").orElse(200).forGetter(PlainMessage::width)
		).apply(i, PlainMessage::new));

		public static final MapCodec<PlainMessage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:plain_message"),
				FIELDS_CODEC.forGetter(x -> x)
		).apply(i, (type, fields) -> fields));

		public PlainMessage width(int width) { return new PlainMessage(contents, width); }
	}

	/** renders an item with an optional description, vanilla's {@code minecraft:item} body type */
	record Item(ItemStack item, Optional<PlainMessage> description, boolean showDecorations, boolean showTooltip, int width, int height) implements Body {
		public static final MapCodec<Item> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:item"),
				ItemStack.CODEC.fieldOf("item").forGetter(Item::item),
				PlainMessage.FIELDS_CODEC.codec().optionalFieldOf("description").forGetter(Item::description),
				Codec.BOOL.fieldOf("show_decorations").orElse(true).forGetter(Item::showDecorations),
				Codec.BOOL.fieldOf("show_tooltip").orElse(true).forGetter(Item::showTooltip),
				Codec.INT.fieldOf("width").orElse(16).forGetter(Item::width),
				Codec.INT.fieldOf("height").orElse(16).forGetter(Item::height)
		).apply(i, (type, item, description, showDecorations, showTooltip, width, height) ->
				new Item(item, description, showDecorations, showTooltip, width, height)));

		public Item description(String description) { return new Item(item, Optional.of(Body.plainMessage(description)), showDecorations, showTooltip, width, height); }
		public Item showDecorations(boolean showDecorations) { return new Item(item, description, showDecorations, showTooltip, width, height); }
		public Item showTooltip(boolean showTooltip) { return new Item(item, description, showDecorations, showTooltip, width, height); }
		public Item width(int width) { return new Item(item, description, showDecorations, showTooltip, width, height); }
		public Item height(int height) { return new Item(item, description, showDecorations, showTooltip, width, height); }
	}

	/** a minimal item reference for {@link Item} bodies (vanilla's {@code ItemStackTemplate}: {@code id}/{@code count}/{@code components}) */
	record ItemStack(String id, int count, Optional<DynamicMap> components) {
		public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(ItemStack::id),
				Codec.INT.fieldOf("count").orElse(1).forGetter(ItemStack::count),
				DynamicMap.CODEC.optionalFieldOf("components").forGetter(ItemStack::components)
		).apply(i, ItemStack::new));

		public static ItemStack of(String id) { return new ItemStack(id, 1, Optional.empty()); }
		public static ItemStack of(String id, int count) { return new ItemStack(id, count, Optional.empty()); }
		public ItemStack components(DynamicMap components) { return new ItemStack(id, count, Optional.of(components)); }
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
