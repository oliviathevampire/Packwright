package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:lightning_strike} */
public final class LightningStrikeConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("lightning_strike");

	public static final MapCodec<LightningStrikeConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			EntityPredicate.CODEC.optionalFieldOf("lightning").forGetter(x -> Optional.ofNullable(x.lightning)),
			EntityPredicate.CODEC.optionalFieldOf("bystander").forGetter(x -> Optional.ofNullable(x.bystander))
	).apply(i, (player, lightning, bystander) -> {
		LightningStrikeConditions out = new LightningStrikeConditions();
		out.player = player.orElse(null);
		out.lightning = lightning.orElse(null);
		out.bystander = bystander.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private EntityPredicate lightning;
	private EntityPredicate bystander;

	public LightningStrikeConditions() {
		super(TYPE.toString());
	}

	public static LightningStrikeConditions lightningStrike(EntityPredicate lightning, EntityPredicate bystander) {
		LightningStrikeConditions out = new LightningStrikeConditions();
		out.lightning = lightning;
		out.bystander = bystander;
		return out;
	}

	public LightningStrikeConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public EntityPredicate getLightning() { return lightning; }
	public EntityPredicate getBystander() { return bystander; }
}
