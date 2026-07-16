package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:recipe_unlocked} */
public final class RecipeUnlockedConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("recipe_unlocked");

	public static final MapCodec<RecipeUnlockedConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Identifier.CODEC.fieldOf("recipe").forGetter(x -> x.recipe)
	).apply(i, (player, recipe) -> {
		RecipeUnlockedConditions out = new RecipeUnlockedConditions();
		out.player = player.orElse(null);
		out.recipe = recipe;
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private Identifier recipe;

	public RecipeUnlockedConditions() {
		super(TYPE.toString());
	}

	public static RecipeUnlockedConditions recipeUnlocked(Identifier recipeId) {
		RecipeUnlockedConditions out = new RecipeUnlockedConditions();
		out.recipe = recipeId;
		return out;
	}

	public RecipeUnlockedConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public Identifier getRecipe() {
		return recipe;
	}
}
