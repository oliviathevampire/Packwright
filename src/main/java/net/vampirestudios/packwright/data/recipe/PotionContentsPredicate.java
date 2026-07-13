package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * A predicate over the {@code minecraft:potion_contents} data component: matches items
 * carrying the given potion and/or a custom color. Custom effects are not modeled yet.
 */
public record PotionContentsPredicate(Optional<Identifier> potion, Optional<Integer> customColor) {
	public static final Codec<PotionContentsPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.optionalFieldOf("potion").forGetter(PotionContentsPredicate::potion),
			Codec.INT.optionalFieldOf("custom_color").forGetter(PotionContentsPredicate::customColor)
	).apply(i, PotionContentsPredicate::new));

	public static PotionContentsPredicate potion(Identifier potion) {
		return new PotionContentsPredicate(Optional.of(potion), Optional.empty());
	}

	public static PotionContentsPredicate customColor(int rgb) {
		return new PotionContentsPredicate(Optional.empty(), Optional.of(rgb));
	}
}
