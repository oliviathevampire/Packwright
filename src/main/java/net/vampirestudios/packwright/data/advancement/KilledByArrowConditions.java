package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.IntBound;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** conditions for {@code minecraft:killed_by_arrow} */
public final class KilledByArrowConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("killed_by_arrow");

	public static final MapCodec<KilledByArrowConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			EntityPredicate.CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(x -> x.victims),
			IntBound.CODEC.optionalFieldOf("unique_entity_types").forGetter(x -> Optional.ofNullable(x.uniqueEntityTypes)),
			ItemPredicate.CODEC.optionalFieldOf("fired_from_weapon").forGetter(x -> Optional.ofNullable(x.firedFromWeapon))
	).apply(i, (player, victims, uniqueEntityTypes, firedFromWeapon) -> {
		KilledByArrowConditions out = new KilledByArrowConditions();
		out.player = player.orElse(null);
		out.victims.addAll(victims);
		out.uniqueEntityTypes = uniqueEntityTypes.orElse(null);
		out.firedFromWeapon = firedFromWeapon.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private final List<EntityPredicate> victims = new ArrayList<>();
	private IntBound uniqueEntityTypes;
	private ItemPredicate firedFromWeapon;

	public KilledByArrowConditions() {
		super(TYPE.toString());
	}

	public static KilledByArrowConditions crossbowKilled(ItemPredicate firedFromWeapon, EntityPredicate... victims) {
		KilledByArrowConditions out = new KilledByArrowConditions();
		out.firedFromWeapon = firedFromWeapon;
		if (victims != null) out.victims.addAll(List.of(victims));
		return out;
	}

	public KilledByArrowConditions player(Condition player) { this.player = player; return this; }

	public KilledByArrowConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public List<EntityPredicate> getVictims() { return List.copyOf(victims); }
	public IntBound getUniqueEntityTypes() { return uniqueEntityTypes; }
	public ItemPredicate getFiredFromWeapon() { return firedFromWeapon; }
}
