package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.Condition;

public record ConditionalNumberProvider(Condition condition, NumberProvider onTrue, NumberProvider onFalse) implements NumberProvider {
	public static final MapCodec<ConditionalNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:conditional"),
			Condition.CODEC.fieldOf("condition").forGetter(ConditionalNumberProvider::condition),
			NumberProvider.CODEC.fieldOf("on_true").forGetter(ConditionalNumberProvider::onTrue),
			NumberProvider.CODEC.optionalFieldOf("on_false", NumberProvider.constant(0.0F)).forGetter(ConditionalNumberProvider::onFalse)
	).apply(i, (type, condition, onTrue, onFalse) -> new ConditionalNumberProvider(condition, onTrue, onFalse)));
}
