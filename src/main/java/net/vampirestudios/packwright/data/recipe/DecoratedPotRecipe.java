package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * {@code minecraft:crafting_decorated_pot}: a special crafting recipe (fixed 3x3 back/left/right/front
 * wall pattern). Like all {@code crafting_special_*}/special recipes, vanilla hardcodes
 * {@code group}/{@code category}/{@code show_notification} rather than exposing them as JSON fields.
 */
public class DecoratedPotRecipe extends Recipe {
	public static final Codec<DecoratedPotRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("back").forGetter(DecoratedPotRecipe::getBack),
			Ingredient.CODEC.fieldOf("left").forGetter(DecoratedPotRecipe::getLeft),
			Ingredient.CODEC.fieldOf("right").forGetter(DecoratedPotRecipe::getRight),
			Ingredient.CODEC.fieldOf("front").forGetter(DecoratedPotRecipe::getFront),
			Result.CODEC.fieldOf("result").forGetter(DecoratedPotRecipe::getResult)
	).apply(instance, DecoratedPotRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_decorated_pot"), CODEC);
	}

	private final Ingredient back;
	private final Ingredient left;
	private final Ingredient right;
	private final Ingredient front;
	private final Result result;

	DecoratedPotRecipe(Ingredient back, Ingredient left, Ingredient right, Ingredient front, Result result) {
		super(Identifier.withDefaultNamespace("crafting_decorated_pot"));
		this.back = back;
		this.left = left;
		this.right = right;
		this.front = front;
		this.result = result;
	}

	public Ingredient getBack() {
		return back;
	}

	public Ingredient getLeft() {
		return left;
	}

	public Ingredient getRight() {
		return right;
	}

	public Ingredient getFront() {
		return front;
	}

	public Result getResult() {
		return result;
	}
}
