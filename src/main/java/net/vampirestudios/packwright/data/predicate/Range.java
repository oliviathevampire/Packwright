package net.vampirestudios.packwright.data.predicate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A numeric bound as used all over vanilla predicates: either an exact value or a
 * {@code {"min": x, "max": y}} object with one or both ends.
 */
public final class Range {
	private final Object value;

	private Range(Object value) {
		this.value = value;
	}

	public static Range exactly(Number value) {
		return new Range(value);
	}

	public static Range atLeast(Number min) {
		return new Range(Map.of("min", min));
	}

	public static Range atMost(Number max) {
		return new Range(Map.of("max", max));
	}

	public static Range between(Number min, Number max) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("min", min);
		map.put("max", max);
		return new Range(map);
	}

	/**
	 * @return the plain Java value: a {@link Number} or a min/max {@link Map}
	 */
	public Object value() {
		return this.value;
	}
}
