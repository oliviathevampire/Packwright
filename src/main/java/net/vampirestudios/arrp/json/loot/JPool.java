package net.vampirestudios.arrp.json.loot;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JPool {
	public static final Codec<JPool> CODEC = RecordCodecBuilder.create(i -> i.group(
			JCondition.CODEC.listOf().optionalFieldOf("conditions").forGetter(p -> Optional.ofNullable(p.conditions)),
			JFunction.CODEC.listOf().optionalFieldOf("functions").forGetter(p -> Optional.ofNullable(p.functions)),
			JEntry.CODEC.listOf().optionalFieldOf("entries").forGetter(p -> Optional.ofNullable(p.entries)),
			Codec.either(Codec.INT, JRoll.CODEC).optionalFieldOf("rolls").forGetter(p -> Optional.ofNullable(rollsEither(p))),
			Codec.either(Codec.INT, JRoll.CODEC).optionalFieldOf("bonus_rolls").forGetter(p -> Optional.ofNullable(bonusEither(p)))
	).apply(i, (ocond, ofunc, oent, orolls, obonus) -> {
		JPool p = new JPool();
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
	private List<JCondition> conditions;
	private List<JFunction> functions;
	private List<JEntry> entries;
	private Integer rolls;
	private JRoll roll;
	private Integer bonus_rolls;
	private JRoll bonus_roll;
	/**
	 * @see JLootTable#pool()
	 */
	public JPool() {
	}

	private static Either<Integer, JRoll> rollsEither(JPool p) {
		if (p.roll != null) return Either.right(p.roll);
		if (p.rolls != null) return Either.left(p.rolls);
		return null;
	}

	private static Either<Integer, JRoll> bonusEither(JPool p) {
		if (p.bonus_roll != null) return Either.right(p.bonus_roll);
		if (p.bonus_rolls != null) return Either.left(p.bonus_rolls);
		return null;
	}

	public JPool entry(JEntry entry) {
		if (this.entries == null) {
			this.entries = new ArrayList<>(1);
		}
		this.entries.add(entry);
		return this;
	}

	public JPool condition(JCondition condition) {
		if (this.conditions == null) {
			this.conditions = new ArrayList<>(1);
		}
		this.conditions.add(condition);
		return this;
	}

	public JPool function(JFunction function) {
		if (this.functions == null) {
			this.functions = new ArrayList<>(1);
		}
		this.functions.add(function);
		return this;
	}

	public JPool rolls(Integer rolls) {
		this.rolls = rolls;
		return this;
	}

	public JPool rolls(JRoll roll) {
		this.roll = roll;
		return this;
	}

	public JPool bonus(Integer bonus_rolls) {
		this.bonus_rolls = bonus_rolls;
		return this;
	}

	public JPool bonus(JRoll bonus_roll) {
		this.bonus_roll = bonus_roll;
		return this;
	}
}
