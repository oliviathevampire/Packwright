package net.vampirestudios.packwright.data.loot.providers.nbt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.loot.EntityTarget;

public record ContextLootNbtSource(EntityTarget target) implements LootNbtSource {
	public static final MapCodec<ContextLootNbtSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:context"),
			EntityTarget.CODEC.fieldOf("target").forGetter(ContextLootNbtSource::target)
	).apply(i, (type, target) -> new ContextLootNbtSource(target)));
}
