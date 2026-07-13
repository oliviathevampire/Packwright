package net.vampirestudios.packwright.data.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;

/**
 * A reusable slot source in the {@code slot_source} registry (since 26.3), used by
 * {@code /item} and {@code /execute if slots|items} and referenced inline with
 * {@code minecraft:reference}.
 *
 * <pre>{@code
 * SlotSource.slotRange("hotbar.*")
 * }</pre>
 */
public sealed interface SlotSource permits SlotRangeSource, ReferenceSlotSource {
	Codec<SlotSource> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<SlotSource, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "slot_range" -> SlotRangeSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "reference" -> ReferenceSlotSource.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported slot source type");
			});
		}

		@Override
		public <T> DataResult<T> encode(SlotSource input, DynamicOps<T> ops, T prefix) {
			if (input instanceof SlotRangeSource s) return SlotRangeSource.CODEC.codec().encode(s, ops, prefix);
			if (input instanceof ReferenceSlotSource s) return ReferenceSlotSource.CODEC.codec().encode(s, ops, prefix);
			return DataResult.error(() -> "Unsupported slot source: " + input.getClass().getSimpleName());
		}
	};

	static SlotRangeSource slotRange(String slots) {
		return new SlotRangeSource(slots);
	}

	static ReferenceSlotSource reference(Identifier id) {
		return new ReferenceSlotSource(id);
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
