package net.vampirestudios.packwright.data.loot.providers.nbt;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.loot.util.LootValue;

public interface LootNbtSource {
	Codec<LootNbtSource> DISPATCH_CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<LootNbtSource, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "context" -> ContextLootNbtSource.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "storage" -> StorageLootNbtSource.MAP_CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unknown NBT source type");
			});
		}

		@Override
		public <T> DataResult<T> encode(LootNbtSource input, DynamicOps<T> ops, T prefix) {
			if (input instanceof ContextLootNbtSource source) return ContextLootNbtSource.MAP_CODEC.codec().encode(source, ops, prefix);
			if (input instanceof StorageLootNbtSource source) return StorageLootNbtSource.MAP_CODEC.codec().encode(source, ops, prefix);
			return DataResult.error(() -> "Unsupported NBT source: " + input.getClass().getSimpleName());
		}
	};
	Codec<LootNbtSource> CODEC = Codec.either(EntityTarget.CODEC, DISPATCH_CODEC).xmap(
			either -> either.map(v -> (LootNbtSource) new ContextLootNbtSource(v), source -> source),
			source -> source instanceof ContextLootNbtSource context ? Either.left(context.target()) : Either.right(source)
	);

	static LootNbtSource context(EntityTarget target) {
		return new ContextLootNbtSource(target);
	}

	static LootNbtSource storage(Identifier source) {
		return new StorageLootNbtSource(source);
	}

	default Object value() {
		return LootValue.encode(CODEC, this);
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
