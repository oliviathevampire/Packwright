package net.vampirestudios.arrp.data.loot;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Pool {
	public static final Codec<Pool> CODEC = RecordCodecBuilder.create(i -> i.group(
			Condition.CODEC.listOf().optionalFieldOf("conditions").forGetter(p -> Optional.ofNullable(p.conditions)),
			LootFunction.CODEC.listOf().optionalFieldOf("functions").forGetter(p -> Optional.ofNullable(p.functions)),
			Entry.CODEC.listOf().optionalFieldOf("entries").forGetter(p -> Optional.ofNullable(p.entries)),
			Codec.either(Codec.INT, Roll.CODEC).optionalFieldOf("rolls").forGetter(p -> Optional.ofNullable(rollsEither(p))),
			Codec.either(Codec.INT, Roll.CODEC).optionalFieldOf("bonus_rolls").forGetter(p -> Optional.ofNullable(bonusEither(p)))
	).apply(i, (ocond, ofunc, oent, orolls, obonus) -> {
		Pool p = new Pool();
		p.conditions = ocond.orElse(null);
		p.functions = ofunc.orElse(null);
		p.entries = oent.orElse(null);
		orolls.ifPresent(e -> {
			if (e.left().isPresent()) p.rolls = e.left().get();
			else p.roll = e.right().get();
		});
		obonus.ifPresent(e -> {
			if (e.left().isPresent()) p.bonus_rolls = e.left().get();
			else p.bonus_roll = e.right().get();
		});
		return p;
	}));
	private List<Condition> conditions;
	private List<LootFunction> functions;
	private List<Entry> entries;
	private Integer rolls;
	private Roll roll;
	private Integer bonus_rolls;
	private Roll bonus_roll;
	/**
	 * @see LootTable#pool()
	 */
	public Pool() {
	}

	private static Either<Integer, Roll> rollsEither(Pool p) {
		if (p.roll != null) return Either.right(p.roll);
		if (p.rolls != null) return Either.left(p.rolls);
		return null;
	}

	private static Either<Integer, Roll> bonusEither(Pool p) {
		if (p.bonus_roll != null) return Either.right(p.bonus_roll);
		if (p.bonus_rolls != null) return Either.left(p.bonus_rolls);
		return null;
	}

	public Pool entry(Entry entry) {
		if (this.entries == null) {
			this.entries = new ArrayList<>(1);
		}
		this.entries.add(entry);
		return this;
	}

	public Pool condition(Condition condition) {
		if (this.conditions == null) {
			this.conditions = new ArrayList<>(1);
		}
		this.conditions.add(condition);
		return this;
	}

	public Pool function(LootFunction function) {
		if (this.functions == null) {
			this.functions = new ArrayList<>(1);
		}
		this.functions.add(function);
		return this;
	}

	public Pool rolls(Integer rolls) {
		this.rolls = rolls;
		return this;
	}

	public Pool rolls(Roll roll) {
		this.roll = roll;
		return this;
	}

	public Pool bonus(Integer bonus_rolls) {
		this.bonus_rolls = bonus_rolls;
		return this;
	}

	public Pool bonus(Roll bonus_roll) {
		this.bonus_roll = bonus_roll;
		return this;
	}
}
