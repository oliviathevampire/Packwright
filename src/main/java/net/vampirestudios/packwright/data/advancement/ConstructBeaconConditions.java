package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;

import java.util.Optional;

/** conditions for {@code minecraft:construct_beacon} */
public final class ConstructBeaconConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("construct_beacon");

	public static final MapCodec<ConstructBeaconConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			IntBound.CODEC.optionalFieldOf("level").forGetter(x -> Optional.ofNullable(x.level))
	).apply(i, (player, level) -> {
		ConstructBeaconConditions out = new ConstructBeaconConditions();
		out.player = player.orElse(null);
		out.level = level.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private IntBound level;

	public ConstructBeaconConditions() {
		super(TYPE.toString());
	}

	public static ConstructBeaconConditions constructedBeacon() {
		return new ConstructBeaconConditions();
	}

	public static ConstructBeaconConditions constructedBeacon(IntBound level) {
		ConstructBeaconConditions out = new ConstructBeaconConditions();
		out.level = level;
		return out;
	}

	public ConstructBeaconConditions player(Condition player) { this.player = player; return this; }

	public ConstructBeaconConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public IntBound getLevel() { return level; }
}
