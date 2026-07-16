package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** {@code minecraft:empty}: always provides zero slots */
public record EmptySlotSource() implements SlotSource {
	public static final MapCodec<EmptySlotSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:empty")
	).apply(i, type -> new EmptySlotSource()));

	public static final EmptySlotSource INSTANCE = new EmptySlotSource();
}
