package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record StorageNumberProvider(Identifier storage, String path) implements NumberProvider {
	public static final MapCodec<StorageNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:storage"),
			Identifier.CODEC.fieldOf("storage").forGetter(StorageNumberProvider::storage),
			Codec.STRING.fieldOf("path").forGetter(StorageNumberProvider::path)
	).apply(i, (type, storage, path) -> new StorageNumberProvider(storage, path)));
}
