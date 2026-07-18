package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.providers.number.NumberProvider;
import net.vampirestudios.packwright.data.loot.util.LootValue;

public record StewEffectEntry(Identifier type, NumberProvider duration) {
	public static final Codec<StewEffectEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("type").forGetter(StewEffectEntry::type),
			NumberProvider.CODEC.fieldOf("duration").forGetter(StewEffectEntry::duration)
	).apply(i, StewEffectEntry::new));

	public Object value() {
		return LootValue.encode(CODEC, this);
	}
}
