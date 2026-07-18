package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:recipe_unlocked} */
public final class RecipeUnlockedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("recipe_unlocked");

	public static final MapCodec<RecipeUnlockedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Identifier.CODEC.fieldOf("recipes").forGetter(x -> x.recipe)
	).apply(i, (player, recipe) -> {
		RecipeUnlockedConditions out = new RecipeUnlockedConditions();
		out.player = player.orElse(null);
		out.recipe = recipe;
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private Condition player;
	private Identifier recipe;

	public RecipeUnlockedConditions() {
		super(TYPE.toString());
	}

	public static RecipeUnlockedConditions recipeUnlocked(Identifier recipeId) {
		RecipeUnlockedConditions out = new RecipeUnlockedConditions();
		out.recipe = recipeId;
		return out;
	}

	public RecipeUnlockedConditions player(Condition player) { this.player = player; return this; }

	public RecipeUnlockedConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public Identifier getRecipe() {
		return recipe;
	}
}
