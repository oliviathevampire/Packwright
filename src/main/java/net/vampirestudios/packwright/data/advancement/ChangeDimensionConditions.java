package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:changed_dimension}. Vanilla's "from"/"to" fields are
 * {@code ResourceKey<Level>}; represented here as dimension ids.
 */
public final class ChangeDimensionConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("changed_dimension");

	public static final MapCodec<ChangeDimensionConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Identifier.CODEC.optionalFieldOf("from").forGetter(x -> Optional.ofNullable(x.from)),
			Identifier.CODEC.optionalFieldOf("to").forGetter(x -> Optional.ofNullable(x.to))
	).apply(i, (player, from, to) -> {
		ChangeDimensionConditions out = new ChangeDimensionConditions();
		out.player = player.orElse(null);
		out.from = from.orElse(null);
		out.to = to.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private Identifier from;
	private Identifier to;

	public ChangeDimensionConditions() {
		super(TYPE.toString());
	}

	public static ChangeDimensionConditions changedDimension() {
		return new ChangeDimensionConditions();
	}

	public static ChangeDimensionConditions changedDimension(Identifier from, Identifier to) {
		ChangeDimensionConditions out = new ChangeDimensionConditions();
		out.from = from;
		out.to = to;
		return out;
	}

	public static ChangeDimensionConditions changedDimensionTo(Identifier to) {
		ChangeDimensionConditions out = new ChangeDimensionConditions();
		out.to = to;
		return out;
	}

	public static ChangeDimensionConditions changedDimensionFrom(Identifier from) {
		ChangeDimensionConditions out = new ChangeDimensionConditions();
		out.from = from;
		return out;
	}

	public ChangeDimensionConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public Identifier getFrom() { return from; }
	public Identifier getTo() { return to; }
}
