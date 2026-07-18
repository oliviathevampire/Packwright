package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_special_firework_star_fade}: adds a fade color to a firework star with dye. */
public class FireworkStarFadeRecipe extends Recipe {
	public static final Codec<FireworkStarFadeRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("target").forGetter(FireworkStarFadeRecipe::getTarget),
			Ingredient.CODEC.fieldOf("dye").forGetter(FireworkStarFadeRecipe::getDye),
			Result.CODEC.fieldOf("result").forGetter(FireworkStarFadeRecipe::getResult)
	).apply(instance, FireworkStarFadeRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_firework_star_fade"), CODEC);
	}

	private final Ingredient target;
	private final Ingredient dye;
	private final Result result;

	FireworkStarFadeRecipe(Ingredient target, Ingredient dye, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_firework_star_fade"));
		this.target = target;
		this.dye = dye;
		this.result = result;
	}

	public Ingredient getTarget() {
		return target;
	}

	public Ingredient getDye() {
		return dye;
	}

	public Result getResult() {
		return result;
	}
}
