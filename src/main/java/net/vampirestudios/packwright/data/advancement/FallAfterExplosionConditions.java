package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DistancePredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:fall_after_explosion} */
public final class FallAfterExplosionConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("fall_after_explosion");

	public static final MapCodec<FallAfterExplosionConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			LocationPredicate.CODEC.optionalFieldOf("start_position").forGetter(x -> Optional.ofNullable(x.startPosition)),
			DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(x -> Optional.ofNullable(x.distance)),
			EntityPredicate.CODEC.optionalFieldOf("cause").forGetter(x -> Optional.ofNullable(x.cause))
	).apply(i, (player, startPosition, distance, cause) -> {
		FallAfterExplosionConditions out = new FallAfterExplosionConditions();
		out.player = player.orElse(null);
		out.startPosition = startPosition.orElse(null);
		out.distance = distance.orElse(null);
		out.cause = cause.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private LocationPredicate startPosition;
	private DistancePredicate distance;
	private EntityPredicate cause;

	public FallAfterExplosionConditions() {
		super(TYPE.toString());
	}

	public static FallAfterExplosionConditions fallAfterExplosion(DistancePredicate distance, EntityPredicate cause) {
		FallAfterExplosionConditions out = new FallAfterExplosionConditions();
		out.distance = distance;
		out.cause = cause;
		return out;
	}

	public FallAfterExplosionConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public LocationPredicate getStartPosition() { return startPosition; }
	public DistancePredicate getDistance() { return distance; }
	public EntityPredicate getCause() { return cause; }
}
