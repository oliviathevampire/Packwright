package net.vampirestudios.packwright.data.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * A data-driven brewing stand recipe ({@code minecraft:brewing}, since 26.3).
 * Any items may be used in any of the three slots.
 * <p>
 * {@code input} is the item in a bottle slot, {@code reagent} the item in the top slot,
 * and {@code output} the resulting item stack.
 */
public final class BrewingRecipe extends Recipe {
	public static final Codec<BrewingRecipe> CODEC = RecordCodecBuilder.create(i -> i.group(
			PotionIngredient.CODEC.fieldOf("input").forGetter(BrewingRecipe::getInput),
			PotionIngredient.CODEC.fieldOf("reagent").forGetter(BrewingRecipe::getReagent),
			Result.CODEC.fieldOf("output").forGetter(BrewingRecipe::getOutput)
	).apply(i, BrewingRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("brewing"), CODEC);
	}

	private final PotionIngredient input;
	private final PotionIngredient reagent;
	private final Result output;

	BrewingRecipe(PotionIngredient input, PotionIngredient reagent, Result output) {
		super(Identifier.withDefaultNamespace("brewing"));
		this.input = input;
		this.reagent = reagent;
		this.output = output;
	}

	public PotionIngredient getInput() {
		return input;
	}

	public PotionIngredient getReagent() {
		return reagent;
	}

	public Result getOutput() {
		return output;
	}

	/**
	 * An item with an optional {@code potion_contents} data component predicate,
	 * e.g. to match only water potions in the input slot.
	 */
	public static final class PotionIngredient {
		public static final Codec<PotionIngredient> CODEC = RecordCodecBuilder.create(i -> i.group(
				Identifier.CODEC.fieldOf("item").forGetter(x -> x.item),
				Result.JSON_OBJECT_CODEC.optionalFieldOf("potion_contents").forGetter(x -> Optional.ofNullable(x.potionContents))
		).apply(i, (item, potionContents) -> new PotionIngredient(item).potionContents(potionContents.orElse(null))));

		private final Identifier item;
		private JsonObject potionContents;

		private PotionIngredient(Identifier item) {
			this.item = item;
		}

		public static PotionIngredient of(Identifier item) {
			return new PotionIngredient(item);
		}

		public static PotionIngredient of(String item) {
			return new PotionIngredient(Identifier.tryParse(item));
		}

		/**
		 * a {@code minecraft:potion_contents} data component predicate the item must match
		 */
		public PotionIngredient potionContents(JsonObject potionContents) {
			this.potionContents = potionContents == null ? null : potionContents.deepCopy();
			return this;
		}

		/**
		 * shortcut for matching a single potion type, e.g. {@code minecraft:water}
		 */
		public PotionIngredient potion(Identifier potion) {
			JsonObject contents = new JsonObject();
			contents.addProperty("potion", potion.toString());
			return potionContents(contents);
		}

		public Identifier getItem() {
			return item;
		}

		public JsonObject getPotionContents() {
			return potionContents == null ? null : potionContents.deepCopy();
		}
	}
}
