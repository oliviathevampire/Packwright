package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:crafting_special_mapextending}: zooms out a filled map by surrounding it with paper. */
public class MapExtendingRecipe extends Recipe {
	public static final Codec<MapExtendingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("map").forGetter(MapExtendingRecipe::getMap),
			Ingredient.CODEC.fieldOf("material").forGetter(MapExtendingRecipe::getMaterial),
			Result.CODEC.fieldOf("result").forGetter(MapExtendingRecipe::getResult)
	).apply(instance, MapExtendingRecipe::new));

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_mapextending"), CODEC);
	}

	private final Ingredient map;
	private final Ingredient material;
	private final Result result;

	MapExtendingRecipe(Ingredient map, Ingredient material, Result result) {
		super(Identifier.withDefaultNamespace("crafting_special_mapextending"));
		this.map = map;
		this.material = material;
		this.result = result;
	}

	public Ingredient getMap() {
		return map;
	}

	public Ingredient getMaterial() {
		return material;
	}

	public Result getResult() {
		return result;
	}
}
