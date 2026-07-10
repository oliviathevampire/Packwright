package net.vampirestudios.arrp.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.data.predicate.PredicateBuilder;

/**
 * A loot pool: some entries, how often to roll them, and optional conditions/functions.
 */
public class Pool extends PredicateBuilder<Pool> {
	public static final Codec<Pool> CODEC = codecOf(Pool::new, null, "Loot pool");

	/**
	 * @see LootTable#pool()
	 */
	public Pool() {
	}

	public static Pool of() {
		return new Pool();
	}

	public Pool entry(Entry entry) {
		subList("entries").add(entry.asMap());
		return this;
	}

	public Pool entries(Entry... entries) {
		for (Entry entry : entries) {
			entry(entry);
		}
		return this;
	}

	public Pool condition(Condition condition) {
		subList("conditions").add(condition.asMap());
		return this;
	}

	public Pool function(LootFunction function) {
		subList("functions").add(function.asMap());
		return this;
	}

	public Pool rolls(Integer rolls) {
		return parameter("rolls", rolls);
	}

	public Pool rolls(NumberProvider rolls) {
		this.values.put("rolls", rolls.value());
		return this;
	}

	/**
	 * a uniformly random number of rolls between {@code min} and {@code max}
	 */
	public Pool rolls(int min, int max) {
		return rolls(NumberProvider.uniform(min, max));
	}

	public Pool bonusRolls(Integer bonusRolls) {
		return parameter("bonus_rolls", bonusRolls);
	}

	public Pool bonusRolls(NumberProvider bonusRolls) {
		this.values.put("bonus_rolls", bonusRolls.value());
		return this;
	}

	public Pool bonusRolls(int min, int max) {
		return bonusRolls(NumberProvider.uniform(min, max));
	}

	public Pool bonus(Integer bonusRolls) {
		return bonusRolls(bonusRolls);
	}

}
