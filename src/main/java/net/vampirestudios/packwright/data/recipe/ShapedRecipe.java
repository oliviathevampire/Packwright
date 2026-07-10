package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class ShapedRecipe extends ResultRecipe {
	public static final Codec<ShapedRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Pattern.CODEC.fieldOf("pattern").forGetter(ShapedRecipe::getPattern),
			Keys.CODEC.fieldOf("key").forGetter(ShapedRecipe::getKey),
			Result.CODEC.fieldOf("result").forGetter(ShapedRecipe::getResult)
	).apply(instance, ShapedRecipe::new));

	static {
		Recipe.register(Identifier.parse("crafting_shaped"), CODEC);
	}

	protected final Pattern pattern;
	protected final Keys key;

	ShapedRecipe(Pattern pattern, Keys keys, Result result) {
		super(Identifier.withDefaultNamespace("crafting_shaped"), result);

		this.pattern = pattern;
		this.key = keys;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Keys getKey() {
		return key;
	}

	@Override
	public ShapedRecipe group(final String group) {
		return (ShapedRecipe) super.group(group);
	}
}
