package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DamagePredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:player_killed_entity}, {@code minecraft:entity_killed_player}
 * and {@code minecraft:kill_mob_near_sculk_catalyst} — all backed by {@code KilledTrigger},
 * which shares this exact {@code (player, entity, killing_blow)} shape across all three ids
 */
public final class PlayerKilledEntityConditions extends CriterionConditions {
	public static final Identifier PLAYER_KILLED_ENTITY = Identifier.withDefaultNamespace("player_killed_entity");
	public static final Identifier ENTITY_KILLED_PLAYER = Identifier.withDefaultNamespace("entity_killed_player");
	public static final Identifier KILL_MOB_NEAR_SCULK_CATALYST = Identifier.withDefaultNamespace("kill_mob_near_sculk_catalyst");

	static {
		CriterionConditions.register(PLAYER_KILLED_ENTITY.toString(), mapCodec(PLAYER_KILLED_ENTITY).codec());
		CriterionConditions.register(ENTITY_KILLED_PLAYER.toString(), mapCodec(ENTITY_KILLED_PLAYER).codec());
		CriterionConditions.register(KILL_MOB_NEAR_SCULK_CATALYST.toString(), mapCodec(KILL_MOB_NEAR_SCULK_CATALYST).codec());
	}

	private static MapCodec<PlayerKilledEntityConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				EntityPredicate.CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity)),
				DamagePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(x -> Optional.ofNullable(x.killingBlow))
		).apply(i, (player, entity, killingBlow) -> {
			PlayerKilledEntityConditions out = new PlayerKilledEntityConditions(trigger);
			out.player = player.orElse(null);
			out.entity = entity.orElse(null);
			out.killingBlow = killingBlow.orElse(null);
			return out;
		}));
	}

	private EntityPredicate player;
	private EntityPredicate entity;
	private DamagePredicate killingBlow;

	private PlayerKilledEntityConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static PlayerKilledEntityConditions playerKilledEntity(EntityPredicate entity, DamagePredicate killingBlow) {
		PlayerKilledEntityConditions out = new PlayerKilledEntityConditions(PLAYER_KILLED_ENTITY);
		out.entity = entity;
		out.killingBlow = killingBlow;
		return out;
	}

	public static PlayerKilledEntityConditions entityKilledPlayer(EntityPredicate entity, DamagePredicate killingBlow) {
		PlayerKilledEntityConditions out = new PlayerKilledEntityConditions(ENTITY_KILLED_PLAYER);
		out.entity = entity;
		out.killingBlow = killingBlow;
		return out;
	}

	public static PlayerKilledEntityConditions killMobNearSculkCatalyst() {
		return new PlayerKilledEntityConditions(KILL_MOB_NEAR_SCULK_CATALYST);
	}

	public PlayerKilledEntityConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public EntityPredicate getEntity() { return entity; }
	public DamagePredicate getKillingBlow() { return killingBlow; }
}
