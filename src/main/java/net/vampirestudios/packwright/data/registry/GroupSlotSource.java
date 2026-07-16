package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * {@code minecraft:group}: concatenates several nested slot sources. Vanilla also accepts a bare
 * JSON array as shorthand for this (no {@code type}/{@code terms} wrapper); {@link SlotSource#CODEC}
 * decodes that shorthand transparently into a {@code GroupSlotSource}, but always encodes the
 * explicit {@code {"type":"minecraft:group","terms":[...]}} form.
 */
public record GroupSlotSource(List<SlotSource> terms) implements SlotSource {
	public static final MapCodec<GroupSlotSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:group"),
			SlotSource.CODEC.listOf().fieldOf("terms").forGetter(GroupSlotSource::terms)
	).apply(i, (type, terms) -> new GroupSlotSource(terms)));
}
