package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DamagePredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:player_killed_entity} */
public final class PlayerKilledEntityConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("player_killed_entity");

	public static final MapCodec<PlayerKilledEntityConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity)),
			DamagePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(x -> Optional.ofNullable(x.killingBlow))
	).apply(i, (entity, killingBlow) -> {
		PlayerKilledEntityConditions out = new PlayerKilledEntityConditions();
		out.entity = entity.orElse(null);
		out.killingBlow = killingBlow.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate entity;
	private DamagePredicate killingBlow;

	public PlayerKilledEntityConditions() {
		super(TYPE.toString());
	}

	public static PlayerKilledEntityConditions playerKilledEntity(EntityPredicate entity, DamagePredicate killingBlow) {
		PlayerKilledEntityConditions out = new PlayerKilledEntityConditions();
		out.entity = entity;
		out.killingBlow = killingBlow;
		return out;
	}

	public EntityPredicate getEntity() { return entity; }
	public DamagePredicate getKillingBlow() { return killingBlow; }
}
