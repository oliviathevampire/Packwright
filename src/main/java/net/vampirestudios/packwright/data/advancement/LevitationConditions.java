package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DistancePredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;

import java.util.Optional;

/** conditions for {@code minecraft:levitation} */
public final class LevitationConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("levitation");

	public static final MapCodec<LevitationConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(x -> Optional.ofNullable(x.distance)),
			IntBound.CODEC.optionalFieldOf("duration").forGetter(x -> Optional.ofNullable(x.duration))
	).apply(i, (player, distance, duration) -> {
		LevitationConditions out = new LevitationConditions();
		out.player = player.orElse(null);
		out.distance = distance.orElse(null);
		out.duration = duration.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private DistancePredicate distance;
	private IntBound duration;

	public LevitationConditions() {
		super(TYPE.toString());
	}

	public static LevitationConditions levitated(DistancePredicate distance) {
		LevitationConditions out = new LevitationConditions();
		out.distance = distance;
		return out;
	}

	public LevitationConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public DistancePredicate getDistance() { return distance; }
	public IntBound getDuration() { return duration; }
}
