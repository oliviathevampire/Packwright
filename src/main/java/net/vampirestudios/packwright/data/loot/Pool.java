package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.loot.util.LootValue;

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
		subList("entries").add(LootValue.encode(Entry.CODEC, entry));
		return this;
	}

	public Pool entries(Entry... entries) {
		for (Entry entry : entries) {
			entry(entry);
		}
		return this;
	}

	/**
	 * only rolls this pool when the condition passes (26.3-snapshot-4 changed this from a
	 * {@code "conditions"} list to a single optional {@code "condition"} — combine several with
	 * {@link Condition#allOf(Condition...)} if needed)
	 */
	public Pool condition(Condition condition) {
		return put("condition", LootValue.encode(Condition.TYPE_CODEC, condition));
	}

	/**
	 * applied to every item this pool produces (26.3-snapshot-4 changed this from a
	 * {@code "functions"} list to a single optional {@code "modifier"} — combine several with
	 * {@link LootFunction#sequence(LootFunction...)} if needed)
	 */
	public Pool function(LootFunction function) {
		return put("modifier", LootValue.encode(LootFunction.CODEC, function));
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
