package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * {@code minecraft:contents}: expands each slot of {@code slot_source} into the slots of its
 * container-like data component, e.g. {@code "minecraft:container"} or {@code "minecraft:bundle_contents"}
 */
public record ContentsSlotSource(SlotSource slotSource, String component) implements SlotSource {
	public static final MapCodec<ContentsSlotSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:contents"),
			SlotSource.CODEC.fieldOf("slot_source").forGetter(ContentsSlotSource::slotSource),
			Codec.STRING.fieldOf("component").forGetter(ContentsSlotSource::component)
	).apply(i, (type, slotSource, component) -> new ContentsSlotSource(slotSource, component)));
}
