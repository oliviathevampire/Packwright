package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.Condition;

public record NumberDispatcherCase(Condition condition, NumberProvider numberProvider) {
	public static final Codec<NumberDispatcherCase> CODEC = RecordCodecBuilder.create(i -> i.group(
			Condition.TYPE_CODEC.fieldOf("condition").forGetter(NumberDispatcherCase::condition),
			NumberProvider.CODEC.fieldOf("number_provider").forGetter(NumberDispatcherCase::numberProvider)
	).apply(i, NumberDispatcherCase::new));
}
