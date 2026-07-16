package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_special_firework_rocket}: paper + gunpowder (+ up to 3 stars) -> firework rocket. */
public class FireworkRocketRecipe extends Recipe {
	public static final Codec<FireworkRocketRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("shell").forGetter(FireworkRocketRecipe::getShell),
			Ingredient.CODEC.fieldOf("fuel").forGetter(FireworkRocketRecipe::getFuel),
			Ingredient.CODEC.fieldOf("star").forGetter(FireworkRocketRecipe::getStar),
			Result.CODEC.fieldOf("result").forGetter(FireworkRocketRecipe::getResult)
	).apply(instance, FireworkRocketRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_firework_rocket"), CODEC);
	}

	private final Ingredient shell;
	private final Ingredient fuel;
	private final Ingredient star;
	private final Result result;

	FireworkRocketRecipe(Ingredient shell, Ingredient fuel, Ingredient star, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_firework_rocket"));
		this.shell = shell;
		this.fuel = fuel;
		this.star = star;
		this.result = result;
	}

	public Ingredient getShell() {
		return shell;
	}

	public Ingredient getFuel() {
		return fuel;
	}

	public Ingredient getStar() {
		return star;
	}

	public Result getResult() {
		return result;
	}
}
