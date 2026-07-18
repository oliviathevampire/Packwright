package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Arrays;

/**
 * The recipe-book category a crafting-table recipe (shaped/shapeless/transmute/dye/imbue/...)
 * is shown under. Mirrors vanilla's {@code CraftingBookCategory}.
 */
public enum CraftingBookCategory {
	BUILDING("building"),
	REDSTONE("redstone"),
	EQUIPMENT("equipment"),
	MISC("misc");

	public static final Codec<CraftingBookCategory> CODEC = Codec.STRING.comapFlatMap(
			id -> Arrays.stream(values())
					.filter(category -> category.id.equals(id))
					.findFirst()
					.map(DataResult::success)
					.orElseGet(() -> DataResult.error(() -> "Unknown crafting book category: " + id)),
			CraftingBookCategory::getTypeId
	);

	private final String id;

	CraftingBookCategory(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return id;
	}

	public static CraftingBookCategory fromIdOrDefault(String id, CraftingBookCategory fallback) {
		for (CraftingBookCategory value : values()) {
			if (value.id.equals(id)) return value;
		}
		return fallback;
	}

	@Override
	public String toString() {
		return id;
	}
}
