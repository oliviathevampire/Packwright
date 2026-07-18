package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_special_shielddecoration}: applies a patterned banner's pattern to a plain shield. */
public class ShieldDecorationRecipe extends Recipe {
	public static final Codec<ShieldDecorationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("banner").forGetter(ShieldDecorationRecipe::getBanner),
			Ingredient.CODEC.fieldOf("target").forGetter(ShieldDecorationRecipe::getTarget),
			Result.CODEC.fieldOf("result").forGetter(ShieldDecorationRecipe::getResult)
	).apply(instance, ShieldDecorationRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_shielddecoration"), CODEC);
	}

	private final Ingredient banner;
	private final Ingredient target;
	private final Result result;

	ShieldDecorationRecipe(Ingredient banner, Ingredient target, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_shielddecoration"));
		this.banner = banner;
		this.target = target;
		this.result = result;
	}

	public Ingredient getBanner() {
		return banner;
	}

	public Ingredient getTarget() {
		return target;
	}

	public Result getResult() {
		return result;
	}
}
