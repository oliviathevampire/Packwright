package net.vampirestudios.arrp.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class TransmuteRecipe extends ResultRecipe {
	public static final Codec<TransmuteRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Result.CODEC.fieldOf("result").forGetter(TransmuteRecipe::getResult),
			Ingredient.CODEC.fieldOf("input").forGetter(TransmuteRecipe::getInput),
			Ingredient.CODEC.fieldOf("material").forGetter(TransmuteRecipe::getMaterial)
	).apply(instance, TransmuteRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_transmute"), CODEC);
	}

	private Ingredient input;
	private Ingredient material;

	TransmuteRecipe(Result result, Ingredient input, Ingredient material) {
		super(Identifier.withDefaultNamespace("crafting_transmute"), result);
		this.input = input;
		this.material = material;
	}

	public TransmuteRecipe input(Ingredient input) {
		this.input = input;
		return this;
	}

	public TransmuteRecipe material(Ingredient material) {
		this.material = material;
		return this;
	}

	@Override
	public TransmuteRecipe group(final String group) {
		return (TransmuteRecipe) super.group(group);
	}

	public Ingredient getInput() {
		return input;
	}

	public Ingredient getMaterial() {
		return material;
	}
}
