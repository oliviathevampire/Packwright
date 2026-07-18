package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.DamagePredicate;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:entity_hurt_player} */
public final class EntityHurtPlayerConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("entity_hurt_player");

	public static final MapCodec<EntityHurtPlayerConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(x -> Optional.ofNullable(x.damage))
	).apply(i, (player, damage) -> {
		EntityHurtPlayerConditions out = new EntityHurtPlayerConditions();
		out.player = player.orElse(null);
		out.damage = damage.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private DamagePredicate damage;

	public EntityHurtPlayerConditions() {
		super(TYPE.toString());
	}

	public static EntityHurtPlayerConditions entityHurtPlayer(DamagePredicate damage) {
		EntityHurtPlayerConditions out = new EntityHurtPlayerConditions();
		out.damage = damage;
		return out;
	}

	public EntityHurtPlayerConditions player(Condition player) { this.player = player; return this; }

	public EntityHurtPlayerConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public DamagePredicate getDamage() { return damage; }
}
