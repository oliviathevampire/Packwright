package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.recipe.PotionContentsPredicate;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/**
 * conditions for {@code minecraft:brewed_potion}. Vanilla's "potion" field is a
 * {@code Holder<Potion>}; represented here as its registry id.
 */
public final class BrewedPotionConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("brewed_potion");
	private static final com.mojang.serialization.Codec<Identifier> POTION_PREDICATE_CODEC = PotionContentsPredicate.CODEC.xmap(
			predicate -> predicate.potions().orElseThrow().getFirst().getId(),
			PotionContentsPredicate::potion
	);

	public static final MapCodec<BrewedPotionConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			POTION_PREDICATE_CODEC.optionalFieldOf("potion").forGetter(x -> Optional.ofNullable(x.potion))
	).apply(i, (player, potion) -> {
		BrewedPotionConditions out = new BrewedPotionConditions();
		out.player = player.orElse(null);
		out.potion = potion.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private Identifier potion;

	public BrewedPotionConditions() {
		super(TYPE.toString());
	}

	public static BrewedPotionConditions brewedPotion() {
		return new BrewedPotionConditions();
	}

	public static BrewedPotionConditions brewedPotion(Identifier potion) {
		BrewedPotionConditions out = new BrewedPotionConditions();
		out.potion = potion;
		return out;
	}

	public BrewedPotionConditions player(Condition player) { this.player = player; return this; }

	public BrewedPotionConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public Identifier getPotion() { return potion; }
}
