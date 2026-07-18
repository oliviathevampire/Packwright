/*
package net.vampirestudios.packwright;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.vampirestudios.packwright.api.ResourceEncoder;

public record ResourceType<T>(
        PackType packType,
        ResourcePathResolver pathResolver,
        ResourceEncoder<T> encoder
) {

    public Identifier resolve(Identifier id) {
        return this.pathResolver.resolve(id);
    }

    public byte[] encode(T value) {
        return this.encoder.encode(value);
    }

    public static <T> ResourceType<T> jsonData(
            String directory,
            Codec<T> codec
    ) {
        return new ResourceType<>(
                PackType.SERVER_DATA,
                ResourcePathResolver.extension(directory, ".json"),
                ResourceEncoder.codec(codec)
        );
    }
}*/
