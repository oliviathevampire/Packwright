package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.Condition;

import java.util.Optional;

/**
 * an effect that targets {@code enchanted}/{@code affected} entities (attacker/damaging
 * entity/victim), used by {@code post_attack}; only applies when the optional
 * {@code requirements} condition passes
 */
public record TargetedConditionalEffect<T>(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect, Optional<Condition> requirements) {
	public static <T> Codec<TargetedConditionalEffect<T>> codec(Codec<T> effectCodec) {
		return RecordCodecBuilder.create(i -> i.group(
				EnchantmentTarget.CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted),
				EnchantmentTarget.CODEC.fieldOf("affected").forGetter(TargetedConditionalEffect::affected),
				effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect),
				Condition.CODEC.optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)
		).apply(i, TargetedConditionalEffect::new));
	}

	/**
	 * {@code equipment_drops} only serializes {@code enchanted} (it always targets the victim);
	 * {@code affected} is fixed to {@link EnchantmentTarget#VICTIM} and not written to JSON.
	 */
	public static <T> Codec<TargetedConditionalEffect<T>> equipmentDropsCodec(Codec<T> effectCodec) {
		return RecordCodecBuilder.create(i -> i.group(
				EnchantmentTarget.CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted),
				effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect),
				Condition.CODEC.optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)
		).apply(i, (enchanted, effect, requirements) -> new TargetedConditionalEffect<>(enchanted, EnchantmentTarget.VICTIM, effect, requirements)));
	}

	public static <T> TargetedConditionalEffect<T> of(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect) {
		return new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty());
	}

	public static <T> TargetedConditionalEffect<T> of(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect, Condition requirements) {
		return new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.ofNullable(requirements));
	}
}
