package net.vampirestudios.arrp.data.loot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A loot number provider: a constant, a random distribution, or a scoreboard lookup.
 * Used for pool rolls, item counts, damage values, enchantment levels, ...
 * Since 26.3 providers can also branch on predicates ({@link #numberDispatcher},
 * {@link #conditionalValue}) or pick from a weighted distribution ({@link #weightedList}),
 * and reusable providers live in the {@code number_provider} data registry.
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
	 * returns the first case whose condition passes, or {@code defaultProvider} if none do
	 * ({@code minecraft:number_dispatcher}, since 26.3); build cases with
	 * {@link #dispatchCase(Condition, NumberProvider)}
	 *
	 * @param defaultProvider may be null; the game falls back to a constant {@code 0}
	 */
	public static NumberProvider numberDispatcher(NumberProvider defaultProvider, List<Map<String, Object>> cases) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:number_dispatcher");
		map.put("cases", new ArrayList<>(cases));
		if (defaultProvider != null) map.put("default", defaultProvider.value());
		return new NumberProvider(map);
	}

	/**
	 * one entry of {@link #numberDispatcher}'s {@code cases} list
	 */
	public static Map<String, Object> dispatchCase(Condition condition, NumberProvider provider) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("condition", condition.asMap());
		map.put("number_provider", provider.value());
		return map;
	}

	/**
	 * returns {@code onTrue} if the condition passes, otherwise {@code onFalse}
	 * ({@code minecraft:conditional_value}, since 26.3)
	 *
	 * @param onFalse may be null; the game falls back to a constant {@code 0}
	 */
	public static NumberProvider conditionalValue(Condition condition, NumberProvider onTrue, NumberProvider onFalse) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:conditional_value");
		map.put("conditions", condition.asMap());
		map.put("on_true", onTrue.value());
		if (onFalse != null) map.put("on_false", onFalse.value());
		return new NumberProvider(map);
	}

	/**
	 * picks from a distribution of weighted providers ({@code minecraft:weighted_list},
	 * since 26.3); build entries with {@link #weighted(NumberProvider, int)}
	 */
	public static NumberProvider weightedList(List<Map<String, Object>> distribution) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "minecraft:weighted_list");
		map.put("distribution", new ArrayList<>(distribution));
		return new NumberProvider(map);
	}

	/**
	 * one entry of {@link #weightedList}'s {@code distribution}
	 *
	 * @param weight positive
	 */
	public static Map<String, Object> weighted(NumberProvider data, int weight) {
		if (weight <= 0) {
			throw new IllegalArgumentException("weight must be positive: " + weight);
		}
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("data", data.value());
		map.put("weight", weight);
		return map;
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
