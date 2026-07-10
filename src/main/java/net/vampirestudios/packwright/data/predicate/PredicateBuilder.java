package net.vampirestudios.packwright.data.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Base class for fluent, map-backed JSON builders. Values are plain Java objects
 * (strings, numbers, booleans, lists, nested maps) serialized through {@code JavaOps},
 * so anything not covered by a typed setter can be added with the {@code parameter} methods.
 */
public abstract class PredicateBuilder<SELF extends PredicateBuilder<SELF>> {
	protected final Map<String, Object> values = new LinkedHashMap<>();

	@SuppressWarnings("unchecked")
	protected final SELF self() {
		return (SELF) this;
	}

	public SELF parameter(String key, Number value) {
		return put(key, value);
	}

	public SELF parameter(String key, String value) {
		return put(key, value);
	}

	public SELF parameter(String key, Boolean value) {
		return put(key, value);
	}

	public SELF parameter(String key, Identifier value) {
		return put(key, value.toString());
	}

	/**
	 * sets a free-form object parameter; values must be plain Java objects
	 * (strings, numbers, booleans, lists, maps)
	 */
	public SELF parameter(String key, Map<String, ?> value) {
		return put(key, value);
	}

	/**
	 * sets a free-form list parameter; elements must be plain Java objects
	 */
	public SELF parameter(String key, List<?> value) {
		return put(key, value);
	}

	/**
	 * sets another builder (a predicate, condition, ...) as a nested object
	 */
	public SELF parameter(String key, PredicateBuilder<?> value) {
		return put(key, value.values);
	}

	/**
	 * sets an exact value or {@code {"min", "max"}} range
	 */
	public SELF parameter(String key, Range value) {
		return put(key, value.value());
	}

	protected final SELF put(String key, Object value) {
		this.values.put(key, value);
		return self();
	}

	@SuppressWarnings("unchecked")
	protected final Map<String, Object> subMap(String key) {
		return (Map<String, Object>) this.values.computeIfAbsent(key, k -> new LinkedHashMap<String, Object>());
	}

	@SuppressWarnings("unchecked")
	protected final List<Object> subList(String key) {
		return (List<Object>) this.values.computeIfAbsent(key, k -> new ArrayList<>());
	}

	/**
	 * @return the live map of values backing this builder
	 */
	public Map<String, Object> asMap() {
		return this.values;
	}

	/**
	 * a codec that reads/writes a builder as its raw object through {@code JavaOps}
	 *
	 * @param factory creates the empty builder to decode into
	 * @param requiredStringKey a key that must be present with a string value, or null for none
	 * @param description used in error messages, e.g. {@code "Loot condition"}
	 */
	public static <B extends PredicateBuilder<?>> Codec<B> codecOf(Supplier<B> factory, String requiredStringKey, String description) {
		return Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
			Object value = dynamic.convert(JavaOps.INSTANCE).getValue();
			if (!(value instanceof Map<?, ?> map)) return DataResult.error(() -> description + " must be an object");
			if (requiredStringKey != null && !(map.get(requiredStringKey) instanceof String)) {
				return DataResult.error(() -> description + " missing '" + requiredStringKey + "' string");
			}
			B builder = factory.get();
			map.forEach((k, v) -> builder.values.put(String.valueOf(k), v));
			return DataResult.success(builder);
		}, builder -> new Dynamic<>(JavaOps.INSTANCE, builder.values));
	}
}
