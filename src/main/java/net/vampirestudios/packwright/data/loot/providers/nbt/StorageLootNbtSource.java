package net.vampirestudios.packwright.data.loot.providers.nbt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record StorageLootNbtSource(Identifier source) implements LootNbtSource {
	public static final MapCodec<StorageLootNbtSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:storage"),
			Identifier.CODEC.fieldOf("source").forGetter(StorageLootNbtSource::source)
	).apply(i, (type, source) -> new StorageLootNbtSource(source)));
}
