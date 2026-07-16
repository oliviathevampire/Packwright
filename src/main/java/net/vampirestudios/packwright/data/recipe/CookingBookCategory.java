package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Arrays;

/**
 * The recipe-book category a cooking recipe (smelting/blasting/smoking/campfire) is shown
 * under. Mirrors vanilla's {@code CookingBookCategory} — a distinct enum from
 * {@link CraftingBookCategory}, do not confuse the two.
 */
public enum CookingBookCategory {
	FOOD("food"),
	BLOCKS("blocks"),
	MISC("misc");

	public static final Codec<CookingBookCategory> CODEC = Codec.STRING.comapFlatMap(
			id -> Arrays.stream(values())
					.filter(category -> category.id.equals(id))
					.findFirst()
					.map(DataResult::success)
					.orElseGet(() -> DataResult.error(() -> "Unknown cooking book category: " + id)),
			CookingBookCategory::getTypeId
	);

	private final String id;

	CookingBookCategory(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return id;
	}

	public static CookingBookCategory fromIdOrDefault(String id, CookingBookCategory fallback) {
		for (CookingBookCategory value : values()) {
			if (value.id.equals(id)) return value;
		}
		return fallback;
	}

	@Override
	public String toString() {
		return id;
	}
}
