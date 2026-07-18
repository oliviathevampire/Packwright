package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DoubleBound;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:used_ender_eye} */
public final class UsedEnderEyeConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("used_ender_eye");

	public static final MapCodec<UsedEnderEyeConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			DoubleBound.CODEC.optionalFieldOf("distance").forGetter(x -> Optional.ofNullable(x.distance))
	).apply(i, (player, distance) -> {
		UsedEnderEyeConditions out = new UsedEnderEyeConditions();
		out.player = player.orElse(null);
		out.distance = distance.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private DoubleBound distance;

	public UsedEnderEyeConditions() {
		super(TYPE.toString());
	}

	public static UsedEnderEyeConditions usedEnderEye(DoubleBound distance) {
		UsedEnderEyeConditions out = new UsedEnderEyeConditions();
		out.distance = distance;
		return out;
	}

	public UsedEnderEyeConditions player(Condition player) { this.player = player; return this; }

	public UsedEnderEyeConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public DoubleBound getDistance() { return distance; }
}
