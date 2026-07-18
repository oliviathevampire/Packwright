package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_special_bannerduplicate}: copies a patterned banner's patterns onto a plain banner of the same color. */
public class BannerDuplicateRecipe extends Recipe {
	public static final Codec<BannerDuplicateRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("banner").forGetter(BannerDuplicateRecipe::getBanner),
			Result.CODEC.fieldOf("result").forGetter(BannerDuplicateRecipe::getResult)
	).apply(instance, BannerDuplicateRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_bannerduplicate"), CODEC);
	}

	private final Ingredient banner;
	private final Result result;

	BannerDuplicateRecipe(Ingredient banner, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_bannerduplicate"));
		this.banner = banner;
		this.result = result;
	}

	public Ingredient getBanner() {
		return banner;
	}

	public Result getResult() {
		return result;
	}
}
