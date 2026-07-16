package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * {@code minecraft:slot_range}: a named slot or slot range, e.g. {@code "hotbar.*"}, read from
 * {@code source} (a loot-context target: {@code "container"} (default), an entity target, or a
 * block-entity target)
 */
public record SlotRangeSource(String source, String slots) implements SlotSource {
	public static final MapCodec<SlotRangeSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:slot_range"),
			Codec.STRING.fieldOf("source").orElse("container").forGetter(SlotRangeSource::source),
			Codec.STRING.fieldOf("slots").forGetter(SlotRangeSource::slots)
	).apply(i, (type, source, slots) -> new SlotRangeSource(source, slots)));

	public SlotRangeSource(String slots) {
		this("container", slots);
	}
}
