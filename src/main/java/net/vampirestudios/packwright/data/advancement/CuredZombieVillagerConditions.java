package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:cured_zombie_villager} */
public final class CuredZombieVillagerConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("cured_zombie_villager");

	public static final MapCodec<CuredZombieVillagerConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			EntityPredicate.CODEC.optionalFieldOf("zombie").forGetter(x -> Optional.ofNullable(x.zombie)),
			EntityPredicate.CODEC.optionalFieldOf("villager").forGetter(x -> Optional.ofNullable(x.villager))
	).apply(i, (player, zombie, villager) -> {
		CuredZombieVillagerConditions out = new CuredZombieVillagerConditions();
		out.player = player.orElse(null);
		out.zombie = zombie.orElse(null);
		out.villager = villager.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private EntityPredicate zombie;
	private EntityPredicate villager;

	public CuredZombieVillagerConditions() {
		super(TYPE.toString());
	}

	public static CuredZombieVillagerConditions curedZombieVillager() {
		return new CuredZombieVillagerConditions();
	}

	public CuredZombieVillagerConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public EntityPredicate getZombie() { return zombie; }
	public EntityPredicate getVillager() { return villager; }
}
