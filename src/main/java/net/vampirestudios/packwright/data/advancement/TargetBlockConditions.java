package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;

import java.util.Optional;

/** conditions for {@code minecraft:target_hit} */
public final class TargetBlockConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("target_hit");

	public static final MapCodec<TargetBlockConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			IntBound.CODEC.optionalFieldOf("signal_strength").forGetter(x -> Optional.ofNullable(x.signalStrength)),
			EntityPredicate.CODEC.optionalFieldOf("projectile").forGetter(x -> Optional.ofNullable(x.projectile))
	).apply(i, (player, signalStrength, projectile) -> {
		TargetBlockConditions out = new TargetBlockConditions();
		out.player = player.orElse(null);
		out.signalStrength = signalStrength.orElse(null);
		out.projectile = projectile.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private IntBound signalStrength;
	private EntityPredicate projectile;

	public TargetBlockConditions() {
		super(TYPE.toString());
	}

	public static TargetBlockConditions targetHit(IntBound signalStrength, EntityPredicate projectile) {
		TargetBlockConditions out = new TargetBlockConditions();
		out.signalStrength = signalStrength;
		out.projectile = projectile;
		return out;
	}

	public TargetBlockConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public IntBound getSignalStrength() { return signalStrength; }
	public EntityPredicate getProjectile() { return projectile; }
}
