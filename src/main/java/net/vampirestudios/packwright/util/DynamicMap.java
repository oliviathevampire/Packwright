package net.vampirestudios.packwright.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A small typed stand-in for a free-form JSON object, used for predicates, conditions
 * and other loosely structured data that doesn't warrant its own record type. Backed by
 * plain Java values (String/Number/Boolean/Map/List) and bridged to any codec target via
 * {@link JavaOps}, so it never depends on a particular JSON library.
 */
public final class DynamicMap {
	public static final Codec<DynamicMap> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				Object value = dynamic.convert(JavaOps.INSTANCE).getValue();
				return value instanceof Map<?, ?> map
						? DataResult.success(new DynamicMap(castMap(map)))
						: DataResult.error(() -> "Expected an object");
			},
			map -> new Dynamic<>(JavaOps.INSTANCE, map.values)
	);

	private final Map<String, Object> values;

	private DynamicMap(Map<String, Object> values) {
		this.values = values;
	}

	public static DynamicMap object() {
		return new DynamicMap(new LinkedHashMap<>());
	}

	public DynamicMap set(String key, String value) { this.values.put(key, value); return this; }
	public DynamicMap set(String key, Number value) { this.values.put(key, value); return this; }
	public DynamicMap set(String key, Boolean value) { this.values.put(key, value); return this; }

	public DynamicMap set(String key, Identifier value) {
		this.values.put(key, value == null ? null : value.toString());
		return this;
	}

	public DynamicMap set(String key, DynamicMap value) {
		this.values.put(key, value == null ? null : value.values);
		return this;
	}

	public DynamicMap setStrings(String key, List<String> value) {
		this.values.put(key, value == null ? null : List.copyOf(value));
		return this;
	}

	public DynamicMap setObjects(String key, List<DynamicMap> value) {
		this.values.put(key, value == null ? null : value.stream().<Object>map(v -> v.values).toList());
		return this;
	}

	public <T> DynamicMap set(String key, Codec<T> codec, T value) {
		this.values.put(key, codec.encodeStart(JavaOps.INSTANCE, value).getOrThrow());
		return this;
	}

	/** stores an already-encoded value (String/Number/Boolean/Map/List/null) as-is */
	public DynamicMap setEncoded(String key, Object encodedValue) {
		this.values.put(key, encodedValue);
		return this;
	}

	public boolean has(String key) {
		return this.values.containsKey(key);
	}

	public boolean isEmpty() {
		return this.values.isEmpty();
	}

	/** returns the nested object at {@code key}, creating it if absent; mutations write through */
	public DynamicMap ensureObject(String key) {
		Object existing = this.values.get(key);
		if (existing instanceof Map<?, ?> map) {
			return new DynamicMap(castMap(map));
		}
		DynamicMap created = DynamicMap.object();
		this.values.put(key, created.values);
		return created;
	}

	public DynamicMap copy() {
		return new DynamicMap(new LinkedHashMap<>(this.values));
	}

	/** an ops-erased view of this object, for interop with generic {@code Dynamic<?>} APIs */
	public Dynamic<?> toDynamic() {
		return new Dynamic<>(JavaOps.INSTANCE, this.values);
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> castMap(Map<?, ?> map) {
		return (Map<String, Object>) map;
	}
}
