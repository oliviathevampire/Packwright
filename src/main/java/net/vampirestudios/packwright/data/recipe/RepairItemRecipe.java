package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/**
 * {@code minecraft:crafting_special_repairitem}: combines two damageable items of the same
 * type into one with combined durability. Has no configurable fields at all.
 */
public final class RepairItemRecipe extends Recipe {
	public static final RepairItemRecipe INSTANCE = new RepairItemRecipe();
	public static final Codec<RepairItemRecipe> CODEC = MapCodec.unit(INSTANCE).codec();

	static {
		Recipe.register(Identifier.withDefaultNamespace("crafting_special_repairitem"), CODEC);
	}

	private RepairItemRecipe() {
		super(Identifier.withDefaultNamespace("crafting_special_repairitem"));
	}
}
