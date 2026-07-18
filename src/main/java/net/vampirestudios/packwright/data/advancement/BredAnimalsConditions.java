package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:bred_animals} */
public final class BredAnimalsConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("bred_animals");

	public static final MapCodec<BredAnimalsConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("parent").forGetter(x -> Optional.ofNullable(x.parent)),
			AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("partner").forGetter(x -> Optional.ofNullable(x.partner)),
			AdvancementPredicates.ENTITY_CODEC.optionalFieldOf("child").forGetter(x -> Optional.ofNullable(x.child))
	).apply(i, (player, parent, partner, child) -> {
		BredAnimalsConditions out = new BredAnimalsConditions();
		out.player = player.orElse(null);
		out.parent = parent.orElse(null);
		out.partner = partner.orElse(null);
		out.child = child.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private EntityPredicate parent;
	private EntityPredicate partner;
	private EntityPredicate child;

	public BredAnimalsConditions() {
		super(TYPE.toString());
	}

	public static BredAnimalsConditions bredAnimals() {
		return new BredAnimalsConditions();
	}

	public static BredAnimalsConditions bredAnimals(EntityPredicate parent, EntityPredicate partner, EntityPredicate child) {
		BredAnimalsConditions out = new BredAnimalsConditions();
		out.parent = parent;
		out.partner = partner;
		out.child = child;
		return out;
	}

	public BredAnimalsConditions player(Condition player) { this.player = player; return this; }

	public BredAnimalsConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public EntityPredicate getParent() { return parent; }
	public EntityPredicate getPartner() { return partner; }
	public EntityPredicate getChild() { return child; }
}
