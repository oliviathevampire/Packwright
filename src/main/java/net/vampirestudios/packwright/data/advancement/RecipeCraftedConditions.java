package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * conditions for {@code minecraft:recipe_crafted} and {@code minecraft:crafter_recipe_crafted}
 * — both backed by {@code RecipeCraftedTrigger}, sharing the exact same
 * {@code (player, recipe_id, ingredients)} shape
 */
public final class RecipeCraftedConditions extends CriterionConditions {
	public static final Identifier RECIPE_CRAFTED = Identifier.withDefaultNamespace("recipe_crafted");
	public static final Identifier CRAFTER_RECIPE_CRAFTED = Identifier.withDefaultNamespace("crafter_recipe_crafted");

	static {
		CriterionConditions.register(RECIPE_CRAFTED.toString(), mapCodec(RECIPE_CRAFTED).codec());
		CriterionConditions.register(CRAFTER_RECIPE_CRAFTED.toString(), mapCodec(CRAFTER_RECIPE_CRAFTED).codec());
	}

	private static MapCodec<RecipeCraftedConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				AdvancementPredicates.CONDITION_CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				Identifier.CODEC.fieldOf("recipe_id").forGetter(x -> x.recipeId),
				ItemPredicate.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(x -> x.ingredients)
		).apply(i, (player, recipeId, ingredients) -> {
			RecipeCraftedConditions out = new RecipeCraftedConditions(trigger);
			out.player = player.orElse(null);
			out.recipeId = recipeId;
			out.ingredients.addAll(ingredients);
			return out;
		}));
	}

	private Condition player;
	private Identifier recipeId;
	private final List<ItemPredicate> ingredients = new ArrayList<>();

	private RecipeCraftedConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static RecipeCraftedConditions craftedItem(Identifier recipeId, ItemPredicate... ingredients) {
		RecipeCraftedConditions out = new RecipeCraftedConditions(RECIPE_CRAFTED);
		out.recipeId = recipeId;
		if (ingredients != null) out.ingredients.addAll(List.of(ingredients));
		return out;
	}

	public static RecipeCraftedConditions crafterCraftedItem(Identifier recipeId) {
		RecipeCraftedConditions out = new RecipeCraftedConditions(CRAFTER_RECIPE_CRAFTED);
		out.recipeId = recipeId;
		return out;
	}

	public RecipeCraftedConditions player(Condition player) { this.player = player; return this; }

	public RecipeCraftedConditions player(EntityPredicate predicate) { return player(Condition.entityProperties(EntityTarget.THIS, predicate)); }

	public Condition getPlayer() { return player; }
	public Identifier getRecipeId() { return recipeId; }
	public List<ItemPredicate> getIngredients() { return List.copyOf(ingredients); }
}
