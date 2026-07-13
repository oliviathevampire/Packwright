package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/** {@code minecraft:reference}: delegates to another {@code slot_source} registry entry */
public record ReferenceSlotSource(Identifier name) implements SlotSource {
	public static final MapCodec<ReferenceSlotSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:reference"),
			Identifier.CODEC.fieldOf("name").forGetter(ReferenceSlotSource::name)
	).apply(i, (type, name) -> new ReferenceSlotSource(name)));
}
