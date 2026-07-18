package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.Condition;

import java.util.Optional;

/** an effect that only applies when its (optional) loot {@code requirements} condition passes */
public record ConditionalEffect<T>(T effect, Optional<Condition> requirements) {
	public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec) {
		return RecordCodecBuilder.create(i -> i.group(
				effectCodec.fieldOf("effect").forGetter(ConditionalEffect::effect),
				Condition.TYPE_CODEC.optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements)
		).apply(i, ConditionalEffect::new));
	}

	public static <T> ConditionalEffect<T> of(T effect) {
		return new ConditionalEffect<>(effect, Optional.empty());
	}

	public static <T> ConditionalEffect<T> of(T effect, Condition requirements) {
		return new ConditionalEffect<>(effect, Optional.ofNullable(requirements));
	}
}
