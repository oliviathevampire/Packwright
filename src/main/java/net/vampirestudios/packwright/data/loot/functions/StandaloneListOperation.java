package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.data.loot.util.LootValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record StandaloneListOperation<T>(List<T> values, ListOperation mode) {
	public static <T> Object value(Codec<T> valueCodec, ListOperation mode, List<T> values) {
		Map<String, Object> value = new LinkedHashMap<>();
		mode.addFieldsTo(value);
		value.put("values", LootValue.encode(valueCodec.listOf(), values));
		return value;
	}
}
