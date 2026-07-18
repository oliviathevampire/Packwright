package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.util.LootValue;

import java.util.Map;
import java.util.Optional;

/**
 * How to merge a new list into an existing one.
 */
public interface ListOperation {
	Codec<ListOperation> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<ListOperation, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (string(map, ops, "mode", "")) {
				case "replace_all" -> ReplaceAll.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "append" -> Append.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "insert" -> Insert.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "replace_section" -> ReplaceSection.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unknown list operation mode");
			});
		}

		@Override
		public <T> DataResult<T> encode(ListOperation input, DynamicOps<T> ops, T prefix) {
			if (input instanceof ReplaceAll operation) return ReplaceAll.MAP_CODEC.codec().encode(operation, ops, prefix);
			if (input instanceof Append operation) return Append.MAP_CODEC.codec().encode(operation, ops, prefix);
			if (input instanceof Insert operation) return Insert.MAP_CODEC.codec().encode(operation, ops, prefix);
			if (input instanceof ReplaceSection operation) return ReplaceSection.MAP_CODEC.codec().encode(operation, ops, prefix);
			return DataResult.error(() -> "Unsupported list operation: " + input.getClass().getSimpleName());
		}
	};

	static ListOperation replaceAll() {
		return ReplaceAll.INSTANCE;
	}

	static ListOperation append() {
		return Append.INSTANCE;
	}

	static ListOperation insert(int offset) {
		return new Insert(offset);
	}

	static ListOperation replaceSection(int offset) {
		return replaceSection(offset, null);
	}

	static ListOperation replaceSection(int offset, Integer size) {
		return new ReplaceSection(offset, Optional.ofNullable(size));
	}

	default Object value() {
		return LootValue.encode(CODEC, this);
	}

	default void addFieldsTo(Map<String, Object> values) {
		if (this instanceof ReplaceAll) {
			values.put("mode", "replace_all");
		} else if (this instanceof Append) {
			values.put("mode", "append");
		} else if (this instanceof Insert operation) {
			values.put("mode", "insert");
			if (operation.offset() != 0) {
				values.put("offset", operation.offset());
			}
		} else if (this instanceof ReplaceSection operation) {
			values.put("mode", "replace_section");
			if (operation.offset() != 0) {
				values.put("offset", operation.offset());
			}
			operation.size().ifPresent(size -> values.put("size", size));
		} else {
			throw new IllegalStateException("Unsupported list operation: " + this.getClass().getSimpleName());
		}
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}

	final class ReplaceAll implements ListOperation {
		static final ReplaceAll INSTANCE = new ReplaceAll();
		static final MapCodec<ReplaceAll> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("mode").forGetter(x -> "replace_all")
		).apply(i, mode -> INSTANCE));

		private ReplaceAll() {
		}
	}

	final class Append implements ListOperation {
		static final Append INSTANCE = new Append();
		static final MapCodec<Append> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("mode").forGetter(x -> "append")
		).apply(i, mode -> INSTANCE));

		private Append() {
		}
	}

	record Insert(int offset) implements ListOperation {
		static final MapCodec<Insert> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("mode").forGetter(x -> "insert"),
				Codec.INT.optionalFieldOf("offset", 0).forGetter(Insert::offset)
		).apply(i, (mode, offset) -> new Insert(offset)));
	}

	record ReplaceSection(int offset, Optional<Integer> size) implements ListOperation {
		static final MapCodec<ReplaceSection> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("mode").forGetter(x -> "replace_section"),
				Codec.INT.optionalFieldOf("offset", 0).forGetter(ReplaceSection::offset),
				Codec.INT.optionalFieldOf("size").forGetter(ReplaceSection::size)
		).apply(i, (mode, offset, size) -> new ReplaceSection(offset, size)));
	}
}
