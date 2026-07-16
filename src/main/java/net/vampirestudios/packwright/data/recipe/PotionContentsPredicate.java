package net.vampirestudios.packwright.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.entity.IdOrTag;

import java.util.List;
import java.util.Optional;

/**
 * A predicate over the {@code minecraft:potion_contents} data component (vanilla's
 * {@code PotionsPredicate}): matches items carrying one of the given potions.
 * <p>
 * {@code effects} (a {@code CollectionPredicate<MobEffectsPredicate>} matching the item's
 * resolved potion effects) is not modeled yet — TODO if per-effect matching is ever needed.
 */
public record PotionContentsPredicate(Optional<List<IdOrTag>> potions) {
	public static final Codec<PotionContentsPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
			IdOrTag.LIST_CODEC.optionalFieldOf("potions").forGetter(PotionContentsPredicate::potions)
	).apply(i, PotionContentsPredicate::new));

	public static PotionContentsPredicate potion(Identifier potion) {
		return new PotionContentsPredicate(Optional.of(List.of(IdOrTag.id(potion))));
	}

	public static PotionContentsPredicate potions(List<Identifier> potions) {
		return new PotionContentsPredicate(Optional.of(potions.stream().map(IdOrTag::id).toList()));
	}

	public static PotionContentsPredicate tag(Identifier tag) {
		return new PotionContentsPredicate(Optional.of(List.of(IdOrTag.tag(tag))));
	}
}
