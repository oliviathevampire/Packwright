package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.predicate.ItemPredicate;

/** {@code minecraft:filtered}: keeps only the slots of {@code slot_source} whose item matches {@code item_filter} */
public record FilteredSlotSource(SlotSource slotSource, ItemPredicate itemFilter) implements SlotSource {
	public static final MapCodec<FilteredSlotSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:filtered"),
			SlotSource.CODEC.fieldOf("slot_source").forGetter(FilteredSlotSource::slotSource),
			ItemPredicate.CODEC.fieldOf("item_filter").forGetter(FilteredSlotSource::itemFilter)
	).apply(i, (type, slotSource, itemFilter) -> new FilteredSlotSource(slotSource, itemFilter)));
}
