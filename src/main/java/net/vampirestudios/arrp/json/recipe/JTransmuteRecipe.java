package net.vampirestudios.arrp.json.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class JTransmuteRecipe extends JResultRecipe {
	public static final Codec<JTransmuteRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			JResult.CODEC.fieldOf("result").forGetter(JTransmuteRecipe::getResult),
			JIngredient.CODEC.fieldOf("input").forGetter(JTransmuteRecipe::getInput),
			JIngredient.CODEC.fieldOf("material").forGetter(JTransmuteRecipe::getMaterial)
	).apply(instance, JTransmuteRecipe::new));

	static {
		JRecipe.register(Identifier.withDefaultNamespace("crafting_transmute"), CODEC);
	}

	private JIngredient input;
	private JIngredient material;

	JTransmuteRecipe(JResult result, JIngredient input, JIngredient material) {
		super(Identifier.withDefaultNamespace("crafting_transmute"), result);
		this.input = input;
		this.material = material;
	}

	public JTransmuteRecipe input(JIngredient input) {
		this.input = input;
		return this;
	}

	public JTransmuteRecipe material(JIngredient material) {
		this.material = material;
		return this;
	}

	@Override
	public JTransmuteRecipe group(final String group) {
		return (JTransmuteRecipe) super.group(group);
	}

	public JIngredient getInput() {
		return input;
	}

	public JIngredient getMaterial() {
		return material;
	}
}
