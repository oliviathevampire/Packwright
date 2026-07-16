package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.LocationPredicate;

import java.util.Optional;

/**
 * conditions for the vanilla triggers whose conditions object is nothing but an optional
 * "player" entity predicate: {@code minecraft:location} and {@code minecraft:slept_in_bed}
 * (backed by {@code PlayerTrigger}), {@code minecraft:tick}, {@code minecraft:hero_of_the_village},
 * {@code minecraft:voluntary_exile} and {@code minecraft:avoid_vibration} (also {@code PlayerTrigger}),
 * and {@code minecraft:started_riding} (backed by the separate but identically-shaped
 * {@code StartRidingTrigger}). {@code minecraft:location}'s "location" checks aren't a
 * separate top-level field in vanilla — they're nested inside the player entity predicate
 * via {@link EntityPredicate#location}, so {@link #location(LocationPredicate)} builds that
 * nesting for convenience.
 */
public final class PlayerConditions extends CriterionConditions {
	public static final Identifier LOCATION = Identifier.withDefaultNamespace("location");
	public static final Identifier SLEPT_IN_BED = Identifier.withDefaultNamespace("slept_in_bed");
	public static final Identifier TICK = Identifier.withDefaultNamespace("tick");
	public static final Identifier RAID_WON = Identifier.withDefaultNamespace("hero_of_the_village");
	public static final Identifier VOLUNTARY_EXILE = Identifier.withDefaultNamespace("voluntary_exile");
	public static final Identifier AVOID_VIBRATION = Identifier.withDefaultNamespace("avoid_vibration");
	public static final Identifier STARTED_RIDING = Identifier.withDefaultNamespace("started_riding");

	static {
		CriterionConditions.register(LOCATION.toString(), mapCodec(LOCATION).codec());
		CriterionConditions.register(SLEPT_IN_BED.toString(), mapCodec(SLEPT_IN_BED).codec());
		CriterionConditions.register(TICK.toString(), mapCodec(TICK).codec());
		CriterionConditions.register(RAID_WON.toString(), mapCodec(RAID_WON).codec());
		CriterionConditions.register(VOLUNTARY_EXILE.toString(), mapCodec(VOLUNTARY_EXILE).codec());
		CriterionConditions.register(AVOID_VIBRATION.toString(), mapCodec(AVOID_VIBRATION).codec());
		CriterionConditions.register(STARTED_RIDING.toString(), mapCodec(STARTED_RIDING).codec());
	}

	private static MapCodec<PlayerConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player))
		).apply(i, player -> {
			PlayerConditions out = new PlayerConditions(trigger);
			out.player = player.orElse(null);
			return out;
		}));
	}

	private EntityPredicate player;

	private PlayerConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static PlayerConditions player(Identifier trigger, EntityPredicate player) {
		PlayerConditions out = new PlayerConditions(trigger);
		out.player = player;
		return out;
	}

	public static PlayerConditions location(LocationPredicate location) {
		PlayerConditions out = new PlayerConditions(LOCATION);
		out.player = EntityPredicate.of().location(location);
		return out;
	}

	public static PlayerConditions location(EntityPredicate player) {
		PlayerConditions out = new PlayerConditions(LOCATION);
		out.player = player;
		return out;
	}

	public static PlayerConditions sleptInBed() { return new PlayerConditions(SLEPT_IN_BED); }
	public static PlayerConditions tick() { return new PlayerConditions(TICK); }
	public static PlayerConditions raidWon() { return new PlayerConditions(RAID_WON); }
	public static PlayerConditions voluntaryExile() { return new PlayerConditions(VOLUNTARY_EXILE); }
	public static PlayerConditions avoidVibration() { return new PlayerConditions(AVOID_VIBRATION); }
	public static PlayerConditions startedRiding() { return new PlayerConditions(STARTED_RIDING); }

	public PlayerConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
}
