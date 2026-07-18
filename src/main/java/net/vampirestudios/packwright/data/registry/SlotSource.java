package net.vampirestudios.packwright.data.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

/**
 * A reusable slot source in the {@code slot_source} registry (since 26.3), used by
 * {@code /item} and {@code /execute if slots|items}.
 *
 * <pre>{@code
 * SlotSource.slotRange("hotbar.*")
 * }</pre>
 */
public sealed interface SlotSource permits
		SlotRangeSource,
		GroupSlotSource,
		FilteredSlotSource,
		LimitSlotSource,
		ContentsSlotSource,
		EmptySlotSource {
	Codec<SlotSource> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<SlotSource, T>> decode(DynamicOps<T> ops, T input) {
			DataResult<Pair<SlotSource, T>> byType = ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "slot_range" -> SlotRangeSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "group" -> GroupSlotSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "filtered" -> FilteredSlotSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "limit_slots" -> LimitSlotSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "contents" -> ContentsSlotSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "empty" -> EmptySlotSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported slot source type");
			});
			if (byType.result().isPresent()) {
				return byType;
			}
			// bare-array shorthand for minecraft:group
			return Codec.list(this).decode(ops, input).map(pair -> pair.mapFirst(list -> (SlotSource) new GroupSlotSource(list)));
		}

		@Override
		public <T> DataResult<T> encode(SlotSource input, DynamicOps<T> ops, T prefix) {
			if (input instanceof SlotRangeSource s) return SlotRangeSource.CODEC.codec().encode(s, ops, prefix);
			if (input instanceof GroupSlotSource s) return GroupSlotSource.CODEC.codec().encode(s, ops, prefix);
			if (input instanceof FilteredSlotSource s) return FilteredSlotSource.CODEC.codec().encode(s, ops, prefix);
			if (input instanceof LimitSlotSource s) return LimitSlotSource.CODEC.codec().encode(s, ops, prefix);
			if (input instanceof ContentsSlotSource s) return ContentsSlotSource.CODEC.codec().encode(s, ops, prefix);
			if (input instanceof EmptySlotSource s) return EmptySlotSource.CODEC.codec().encode(s, ops, prefix);
			return DataResult.error(() -> "Unsupported slot source: " + input.getClass().getSimpleName());
		}
	};

	static SlotRangeSource slotRange(String slots) {
		return new SlotRangeSource(slots);
	}

	static SlotRangeSource slotRange(String source, String slots) {
		return new SlotRangeSource(source, slots);
	}

	static GroupSlotSource group(SlotSource... terms) {
		return new GroupSlotSource(java.util.List.of(terms));
	}

	static FilteredSlotSource filtered(SlotSource slotSource, ItemPredicate itemFilter) {
		return new FilteredSlotSource(slotSource, itemFilter);
	}

	static LimitSlotSource limit(SlotSource slotSource, int limit) {
		return new LimitSlotSource(slotSource, limit);
	}

	static ContentsSlotSource contents(SlotSource slotSource, String component) {
		return new ContentsSlotSource(slotSource, component);
	}

	static EmptySlotSource empty() {
		return EmptySlotSource.INSTANCE;
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
