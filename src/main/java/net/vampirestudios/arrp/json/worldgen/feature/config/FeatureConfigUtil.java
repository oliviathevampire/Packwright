package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;

final class FeatureConfigUtil {
	private FeatureConfigUtil() {
	}

	static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	static String stripTagPrefix(String tag) {
		return tag != null && tag.startsWith("#") ? tag.substring(1) : tag;
	}

	static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
