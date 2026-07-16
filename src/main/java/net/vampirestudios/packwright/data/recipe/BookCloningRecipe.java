package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_special_bookcloning}: copies a written book onto blank books. */
public class BookCloningRecipe extends Recipe {
	private static final IntRange DEFAULT_ALLOWED_GENERATIONS = IntRange.between(0, 1);

	public static final Codec<BookCloningRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("source").forGetter(BookCloningRecipe::getSource),
			Ingredient.CODEC.fieldOf("material").forGetter(BookCloningRecipe::getMaterial),
			IntRange.CODEC.fieldOf("allowed_generations").orElse(DEFAULT_ALLOWED_GENERATIONS).forGetter(BookCloningRecipe::getAllowedGenerations),
			Result.CODEC.fieldOf("result").forGetter(BookCloningRecipe::getResult)
	).apply(instance, BookCloningRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_bookcloning"), CODEC);
	}

	private final Ingredient source;
	private final Ingredient material;
	private IntRange allowedGenerations = DEFAULT_ALLOWED_GENERATIONS;
	private final Result result;

	BookCloningRecipe(Ingredient source, Ingredient material, IntRange allowedGenerations, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_bookcloning"));
		this.source = source;
		this.material = material;
		this.allowedGenerations = allowedGenerations;
		this.result = result;
	}

	BookCloningRecipe(Ingredient source, Ingredient material, Result result) {
		this(source, material, DEFAULT_ALLOWED_GENERATIONS, result);
	}

	/** which book generations ({@code 0} original .. {@code 2} copy of copy) may be cloned; vanilla default is 0..1 */
	public BookCloningRecipe allowedGenerations(IntRange allowedGenerations) {
		this.allowedGenerations = allowedGenerations;
		return this;
	}

	public Ingredient getSource() {
		return source;
	}

	public Ingredient getMaterial() {
		return material;
	}

	public IntRange getAllowedGenerations() {
		return allowedGenerations;
	}

	public Result getResult() {
		return result;
	}
}
