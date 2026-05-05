package net.vampirestudios.arrp.json.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.codec.Codecs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JIngredients {
	public static final Codec<JIngredients> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codecs.oneOrList(JIngredient.CODEC).fieldOf("ingredients").forGetter(JIngredients::getIngredients)
	).apply(i, JIngredients::new));

	protected final List<JIngredient> ingredients;

	JIngredients() {
		this.ingredients = new ArrayList<>();
	}

	public JIngredients(List<JIngredient> ingredients) {
		this.ingredients = ingredients;
	}

	public static JIngredients ingredients() {
		return new JIngredients();
	}

	public JIngredients add(final JIngredient ingredient) {
		this.ingredients.add(ingredient);

		return this;
	}

	public JIngredients addAll(final List<JIngredient> ingredients) {
		ingredients.forEach(this::add);
		return this;
	}

	public List<JIngredient> getIngredients() {
		return ingredients;
	}

	public static class Serializer implements JsonSerializer<JIngredients> {
		@Override
		public JsonElement serialize(final JIngredients src,
				final Type typeOfSrc,
				final JsonSerializationContext context) {
			return context.serialize(src.ingredients);
		}
	}
}
