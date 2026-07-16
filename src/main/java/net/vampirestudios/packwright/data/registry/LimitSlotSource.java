package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** {@code minecraft:limit_slots}: keeps only the first {@code limit} slots of {@code slot_source} */
public record LimitSlotSource(SlotSource slotSource, int limit) implements SlotSource {
	public static final MapCodec<LimitSlotSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:limit_slots"),
			SlotSource.CODEC.fieldOf("slot_source").forGetter(LimitSlotSource::slotSource),
			Codec.INT.fieldOf("limit").forGetter(LimitSlotSource::limit)
	).apply(i, (type, slotSource, limit) -> new LimitSlotSource(slotSource, limit)));
}
