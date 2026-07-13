package net.vampirestudios.packwright.data.worldgen.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;

public class Utils {
	public static String normalizeType(String type) {
		int separator = type.indexOf(':');

		return separator >= 0
				? type.substring(separator + 1)
				: type;
	}

	public static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}

	public static Codec<Identifier> typeCodec(Identifier expected) {
		return Identifier.CODEC.validate(type -> type.equals(expected)
				? DataResult.success(type)
				: DataResult.error(() -> "Expected type " + expected + ", got " + type)
		);
	}
}
