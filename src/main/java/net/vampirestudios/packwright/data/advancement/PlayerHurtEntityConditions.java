package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DamagePredicate;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:player_hurt_entity} */
public final class PlayerHurtEntityConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("player_hurt_entity");

	public static final MapCodec<PlayerHurtEntityConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(x -> Optional.ofNullable(x.damage)),
			AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("entity").forGetter(x -> Optional.ofNullable(x.entity))
	).apply(i, (player, damage, entity) -> {
		PlayerHurtEntityConditions out = new PlayerHurtEntityConditions();
		out.player = player.orElse(null);
		out.damage = damage.orElse(null);
		out.entity = entity.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private DamagePredicate damage;
	private EntityPredicate entity;

	public PlayerHurtEntityConditions() {
		super(TYPE.toString());
	}

	public static PlayerHurtEntityConditions playerHurtEntity(DamagePredicate damage, EntityPredicate entity) {
		PlayerHurtEntityConditions out = new PlayerHurtEntityConditions();
		out.damage = damage;
		out.entity = entity;
		return out;
	}

	public PlayerHurtEntityConditions player(Condition player) { this.player = player; return this; }

	public PlayerHurtEntityConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public DamagePredicate getDamage() { return damage; }
	public EntityPredicate getEntity() { return entity; }
}
