package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DistancePredicate;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:nether_travel}, {@code minecraft:fall_from_height} and
 * {@code minecraft:ride_entity_in_lava} — all backed by {@code DistanceTrigger}, sharing
 * the exact same {@code (player, start_position, distance)} shape
 */
public final class DistanceConditions extends CriterionConditions {
	public static final Identifier NETHER_TRAVEL = Identifier.withDefaultNamespace("nether_travel");
	public static final Identifier FALL_FROM_HEIGHT = Identifier.withDefaultNamespace("fall_from_height");
	public static final Identifier RIDE_ENTITY_IN_LAVA = Identifier.withDefaultNamespace("ride_entity_in_lava");

	static {
		CriterionConditions.register(NETHER_TRAVEL.toString(), mapCodec(NETHER_TRAVEL).codec());
		CriterionConditions.register(FALL_FROM_HEIGHT.toString(), mapCodec(FALL_FROM_HEIGHT).codec());
		CriterionConditions.register(RIDE_ENTITY_IN_LAVA.toString(), mapCodec(RIDE_ENTITY_IN_LAVA).codec());
	}

	private static MapCodec<DistanceConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				LocationPredicate.CODEC.optionalFieldOf("start_position").forGetter(x -> Optional.ofNullable(x.startPosition)),
				DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(x -> Optional.ofNullable(x.distance))
		).apply(i, (player, startPosition, distance) -> {
			DistanceConditions out = new DistanceConditions(trigger);
			out.player = player.orElse(null);
			out.startPosition = startPosition.orElse(null);
			out.distance = distance.orElse(null);
			return out;
		}));
	}

	private Condition player;
	private LocationPredicate startPosition;
	private DistancePredicate distance;

	private DistanceConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static DistanceConditions travelledThroughNether(DistancePredicate distance) {
		DistanceConditions out = new DistanceConditions(NETHER_TRAVEL);
		out.distance = distance;
		return out;
	}

	public static DistanceConditions fallFromHeight(EntityPredicate player, DistancePredicate distance, LocationPredicate startPosition) {
		DistanceConditions out = new DistanceConditions(FALL_FROM_HEIGHT);
		out.player = Condition.entityProperties(EntityTarget.THIS, player);
		out.distance = distance;
		out.startPosition = startPosition;
		return out;
	}

	public static DistanceConditions rideEntityInLava(EntityPredicate player, DistancePredicate distance) {
		DistanceConditions out = new DistanceConditions(RIDE_ENTITY_IN_LAVA);
		out.player = Condition.entityProperties(EntityTarget.THIS, player);
		out.distance = distance;
		return out;
	}

	public DistanceConditions player(Condition player) { this.player = player; return this; }

	public DistanceConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public LocationPredicate getStartPosition() { return startPosition; }
	public DistancePredicate getDistance() { return distance; }
}
