package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** {@code minecraft:slot_range}: a named slot or slot range, e.g. {@code "hotbar.*"} */
public record SlotRangeSource(String slots) implements SlotSource {
	public static final MapCodec<SlotRangeSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:slot_range"),
			Codec.STRING.fieldOf("slots").forGetter(SlotRangeSource::slots)
	).apply(i, (type, slots) -> new SlotRangeSource(slots)));
}
