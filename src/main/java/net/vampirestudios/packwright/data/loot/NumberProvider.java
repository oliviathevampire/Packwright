package net.vampirestudios.packwright.data.loot;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A loot number provider: a constant, a random distribution, or a scoreboard lookup.
 * Used for pool rolls, item counts, damage values, enchantment levels, ...
 */
public final class NumberProvider {
	private final Object value;

	private NumberProvider(Object value) {
		this.value = value;
	}

	public static NumberProvider constant(Number value) {
		return new NumberProvider(value);
	}

	/**
	 * a uniformly random value between {@code min} and {@code max} (both inclusive)
	 */
	public static NumberProvider uniform(Number min, Number max) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:uniform");
		map.put("min", min);
		map.put("max", max);
		return new NumberProvider(map);
	}

	/**
	 * a uniform distribution whose bounds are themselves number providers
	 */
	public static NumberProvider uniform(NumberProvider min, NumberProvider max) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:uniform");
		map.put("min", min.value());
		map.put("max", max.value());
		return new NumberProvider(map);
	}

	/**
	 * a binomial distribution with {@code n} trials of probability {@code p}
	 */
	public static NumberProvider binomial(int n, float p) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:binomial");
		map.put("n", n);
		map.put("p", p);
		return new NumberProvider(map);
	}

	/**
	 * reads a scoreboard objective of an entity in the loot context
	 */
	public static NumberProvider score(EntityTarget target, String objective) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:score");
		map.put("target", target.id());
		map.put("score", objective);
		return new NumberProvider(map);
	}

	public static NumberProvider score(EntityTarget target, String objective, float scale) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:score");
		map.put("target", target.id());
		map.put("score", objective);
		map.put("scale", scale);
		return new NumberProvider(map);
	}

	/**
	 * escape hatch for provider types this class has no factory for; values must be
	 * plain Java objects (strings, numbers, booleans, lists, maps)
	 */
	public static NumberProvider of(Map<String, ?> provider) {
		return new NumberProvider(provider);
	}

	/**
	 * @return the plain Java value: a {@link Number} or a provider {@link Map}
	 */
	public Object value() {
		return this.value;
	}
}
