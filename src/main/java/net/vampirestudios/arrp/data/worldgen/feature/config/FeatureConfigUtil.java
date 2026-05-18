package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;

final class FeatureConfigUtil {
	private FeatureConfigUtil() {
	}

	static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	static String stripTagPrefix(Identifier tag) {
		var stringTag = tag.toString();
		return stringTag.startsWith("#") ? stringTag.substring(1) : stringTag;
	}

	static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
